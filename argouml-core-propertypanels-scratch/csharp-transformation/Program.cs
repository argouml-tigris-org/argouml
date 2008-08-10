using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Xml.Linq;
using System.Collections;
using System.IO;

namespace ArgoUML_XMLTransformation
{
    class Program
    {
        private static string SourceFile =
            @"D:\workspaces\Argo\argouml-core-propertypanels-scratch\xslt\01-02-15.xml";

        private static string TargetFile =
            @"D:\workspaces\Argo\argouml-core-propertypanels-scratch\src\org\argouml\core\propertypanels\xml\panels.xml";

        private static Dictionary<String, List<MyAttribute>> AttributesCache = new Dictionary<string, List<MyAttribute>>();


        private static XDocument doc;

        public static void Main(string[] args)
        {
            XDocument finalDoc = new XDocument();
            finalDoc.Declaration = new XDeclaration("1.0", "UTF-8", "yes");

            XElement parent = new XElement(XName.Get("panels"));

            doc = XDocument.Load(SourceFile);

            //var core_package = (from p in doc.Descendants("XMI.content").Descendants()
            //                    where p.Name.Equals(XName.Get("{omg.org/mof.Model/1.3}Package"))
            //                    where p.Attribute("name") != null
            //                    where p.Attribute("name").Value == "Core"
            //                    select p).First();

            var model_elements = from element in doc.Descendants("XMI.content").Descendants()
                                 where element.Name.Equals(XName.Get("{omg.org/mof.Model/1.3}Class"))
                                 select new PropPanel
                                 {
                                     Name = element.Attribute("name").Value,
                                     Attributes = GetAttributes(element)
                                 };

            DumpObject(model_elements);

            WriteXML(model_elements);
        }

        private static void WriteXML(IEnumerable<PropPanel> model_elements)
        {
            XDocument finalDoc = new XDocument();
            finalDoc.Declaration = new XDeclaration("1.0", "UTF-8", "yes");

            XElement parent = new XElement(XName.Get("panels"));

            foreach (PropPanel p in model_elements)
            {

                XElement panel = new XElement("panel");
                panel.Add(new XAttribute("name", p.Name));

                int nelements = 0;
                foreach (MyAttribute a in p.Attributes)
                {
                    nelements++;
                    if (nelements % 10 == 0)
                    {
                        panel.Add(new XElement(XName.Get("separator")));
                    }
                    if (!ShouldBeIgnored(a))
                    {
                        if (!IsGrouped(a))
                        {
                            XElement attrElement = new XElement(GetAttributeType(a));
                            attrElement.Add(new XAttribute(XName.Get("name"), a.Name));
                            attrElement.Add(new XAttribute(XName.Get("type"), a.Type));
                            panel.Add(attrElement);
                        }
                        else
                        {
                            var modifiers = from x in panel.Descendants()
                                            where x.Attribute(XName.Get("name")) != null
                                                && x.Attribute(XName.Get("name")).Value.Equals("modifiers")
                                            select x;
                            XElement modifier = null;
                            if (modifiers.Count() == 0)
                            {
                                // create the modifiers
                                XElement attrElement = new XElement("checkgroup");
                                attrElement.Add(new XAttribute(XName.Get("name"), "modifiers"));
                                panel.Add(attrElement);
                                modifier = attrElement;
                            }
                            else
                            {
                                modifier = modifiers.Single();
                            }
                            // add to the modifiers
                            XElement modi = new XElement(GetAttributeType(a));
                            modi.Add(new XAttribute(XName.Get("name"), a.Name));
                            modi.Add(new XAttribute(XName.Get("type"), a.Type));
                            modifier.Add(modi);
                        }
                    }
                }

                parent.Add(panel);
            }
            finalDoc.Add(parent);
            finalDoc.Save(TargetFile);
        }

        private static bool IsGrouped(MyAttribute a)
        {
            return a.Type.Equals("Boolean");
        }

        private static bool ShouldBeIgnored(MyAttribute a)
        {
            return a.Type.Equals("Stereotype")
                || a.Type.Equals("TaggedValue")
                || a.Type.Equals("Constraint")
                ;
        }

        private static XName GetAttributeType(MyAttribute a)
        {
            string type = "attribute";
            if (a.Type.Equals("Name"))
            {
                type = "text";
            }
            if (a.Type.Equals("Namespace"))
            {
                type = "combo";
            }
            if (a.Type.Equals("Boolean"))
            {
                type = "checkbox";
            }
            if (a.Type.Equals("VisibilityKind"))
            {
                type = "optionbox";
            }
            // TODO: This is false, lists are build from associations
            if (a.Multiplicity.Upper == -1)
            {
                type = "list";
            }
            return XName.Get(type);
        }

        private static void DumpObject(IEnumerable<PropPanel> model_elements)
        {
            using (TextWriter writer = new StreamWriter("dump.txt"))
            {
                foreach (var item in model_elements)
                {
                    writer.WriteLine("Panel=" + item.Name);
                    writer.WriteLine("   Attrs=" + item.Attributes.Count);
                    foreach (MyAttribute a in item.Attributes)
                    {
                        writer.WriteLine("   Attr: " + a.Name + " type:" + a.Type + " mult:" + a.Multiplicity);
                    }
                }
                writer.WriteLine(model_elements.Count() + " model elements.");
            }
        }

        private static List<MyAttribute> GetAttributes(XElement element)
        {
            if (AttributesCache.ContainsKey(element.Attribute("name").Value))
            {
                return AttributesCache[element.Attribute("name").Value];
            }


            List<MyAttribute> attrs = new List<MyAttribute>();

            var supertypes = element.Attribute("supertypes");
            if (supertypes != null)
            {
                string[] types = supertypes.Value.Split();
                foreach (string type in types)
                {
                    XElement x = GetTypeByXmiId(type);
                    AddElement(attrs, (GetAttributes(x)));
                }
            }

            // TODO: Look at the Associations
            string id = element.Attribute(XName.Get("xmi.id")).Value;
            var associations = from a in doc.Descendants("XMI.content").Descendants()
                               where a.Name.LocalName.Equals("Association")
                               select a;
            var associationEnd = (from x in associations.DescendantsAndSelf()
                                  where x.Name != null
                                    && x.Name.LocalName.Equals("AssociationEnd")
                                    && x.Attribute(XName.Get("type")).Value.Equals(id)
                                  select new MyAttribute
                                   {
                                       Name = GetNameOfAssociation(x),
                                       Type = GetTypeByXmiId(x.Attribute("type").Value).Attribute("name").Value,
                                       Multiplicity = GetMultiplicityOfAssociation(x)
                                   }
                                 ).Distinct();

            var ats = (from a in element.Descendants()
                       where a.Name != null
                         && a.Name.Equals(XName.Get("{omg.org/mof.Model/1.3}Attribute"))
                         || a.Name.Equals(XName.Get("{omg.org/mof.Model/1.3}Reference"))
                       select new MyAttribute
                       {
                           Name = a.Attribute("name").Value,
                           Type = GetTypeByXmiId(a.Attribute("type").Value).Attribute("name").Value,
                           Multiplicity = GetMultiplicity(a)
                       }).Distinct();
            AddElement(attrs, ats);
            AddElement(attrs, associationEnd);

            AttributesCache.Add(element.Attribute("name").Value, attrs);


            return attrs;
        }

        private static Multiplicity GetMultiplicityOfAssociation(XElement x)
        {
            var node = (from t in x.Parent.Descendants()
                        where t != x
                           && t.Name.LocalName.Equals("AssociationEnd")
                        select t).Single();
            return GetMultiplicity(node);
        }

        private static string GetNameOfAssociation(XElement x)
        {
            var name = (from t in x.Parent.Descendants()
                        where t != x
                         && t.Attribute("name") != null
                        select t.Attribute("name").Value).Single();
            return name;
        }



        private static Multiplicity GetMultiplicity(XElement a)
        {
            var mults = a.Elements().Single().Elements();
            return new Multiplicity()
            {
                Lower = int.Parse(mults.ElementAt(0).Value),
                Upper = int.Parse(mults.ElementAt(1).Value)
            };
        }

        private static void AddElement(List<MyAttribute> attrs, IEnumerable<MyAttribute> refs)
        {
            foreach (var item in refs)
            {
                bool found = false;
                foreach (var prev_item in attrs)
                {
                    if (prev_item.Name.Trim().ToLower().Equals(item.Name.Trim().ToLower()))
                    {
                        found = true;
                        break;
                    }
                }
                if (!found)
                {
                    attrs.Add(item);
                }
            }

        }

        private static XElement GetTypeByXmiId(String type)
        {
            var model_elements = from element in doc.Descendants()
                                 where element.Name.Equals(XName.Get("{omg.org/mof.Model/1.3}Class"))
                                    || element.Name.Equals(XName.Get("{omg.org/mof.Model/1.3}DataType"))
                                 where element.Attribute("xmi.id") != null
                                 where element.Attribute("xmi.id").Value.Equals(type)
                                 select element;
            return model_elements.Single();

        }
    }

    public class PropPanel
    {
        public string Name { get; set; }
        public List<MyAttribute> Attributes { get; set; }
    }

    public class MyAttribute
    {
        public string Name { get; set; }
        public string Type { get; set; }
        public Multiplicity Multiplicity { get; set; }
    }

    public class Multiplicity
    {
        public int Lower { get; set; }
        public int Upper { get; set; }

        public override string ToString()
        {
            if (Lower == Upper && Upper == 1)
            {
                return "1";
            }
            else if (Upper == -1)
            {
                return Lower + " - *";
            }
            return Lower + " - " + Upper;
        }
    }
}
