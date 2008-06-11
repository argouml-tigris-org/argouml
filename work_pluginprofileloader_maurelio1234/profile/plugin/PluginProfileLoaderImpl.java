package org.argouml.profile.plugin;

import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.swing.ImageIcon;

import org.apache.log4j.Logger;
import org.argouml.cognitive.Agency;
import org.argouml.model.Model;
import org.argouml.profile.FigNodeStrategy;
import org.argouml.profile.Profile;
import org.argouml.profile.ProfileException;
import org.argouml.profile.ProfileFacade;
import org.argouml.profile.ProfileManager;
import org.argouml.profile.ProfileModelLoader;
import org.argouml.profile.ProfileReference;
import org.argouml.profile.ResourceModelLoader;
import org.argouml.uml.cognitive.critics.CrProfile;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * Reads the profile descriptor from a file called "profile.xml" in the same
 * directory as the reference class
 * 
 * @author maas
 */
public class PluginProfileLoaderImpl extends DefaultHandler implements
        PluginProfileLoader {
    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(PluginProfileImpl.class);

    PluginProfileImpl plugin = null;

    Collection profileModel = null;

    ProfileFromPlugin profile = new ProfileFromPlugin();

    private class ProfileFromPlugin extends Profile {
        
        private FigNodeStrategyFromPlugin figNodeStrat = new FigNodeStrategyFromPlugin();
        
        @Override
        public String getDisplayName() {
            return plugin.getName();
        }

        @Override
        public Collection getProfilePackages() throws ProfileException {
            return profileModel;
        }

        public void addDependency(Profile p) {
            super.addProfileDependency(p);
        }
        
        public FigNodeStrategy getFigureStrategy() {
            return figNodeStrat;
        }
        
    };

    private class FigNodeStrategyFromPlugin implements FigNodeStrategy {

        private HashMap<String, Image> images = new HashMap<String, Image>();

        public Image getIconForStereotype(Object stereotype) {
            return images.get(Model.getFacade().getName(stereotype));
        }

        public void addDesrciptor(FigNodeDescriptor fnd) {
            images.put(fnd.stereotype, fnd.img);
        }
    }

    private class FigNodeDescriptor {
        String stereotype;

        Image img;

        String src;

        int length;

        public boolean isValid() {
            return stereotype != null && src != null && length > 0;
        }
    }

    private class JavaCriticDescriptor {
        String crClassName;
        String dmClassName;
        
        public boolean isValid() {
            return crClassName != null && dmClassName != null;
        }
    }
    
    private Class referenceClass;

    private interface Tags {
        static final String PROFILE = "profile";

        static final String MODEL = "model";

        static final String DEPENDENCY = "dependency";

        static final String FIGNODE = "fignode";

        static final String CRITIC = "critic";

    }

    private interface Atts {
        static final String PROFILE_AUTHOR = "author";

        static final String PROFILE_DESCRIPTION = "description";

        static final String PROFILE_NAME = "name";

        static final String PROFILE_VERSION = "version";

        static final String PROFILE_DOWNLOADSITE = "downloadsite";

        static final String MODEL_XMI = "xmi";

        static final String DEPENDENCY_PROFILE = "profile";

        static final String FIGNODE_STEREOTYPE = "stereotype";

        static final String FIGNODE_IMAGE = "image";

        static final String FIGNODE_LENGTH = "length";

        static final String CRITIC_LANGUAGE = "language";
    
        static final String CRITIC_JAVA_CLASS_NAME = "crClassName";

        static final String CRITIC_JAVA_MATERIAL_NAME = "dmClassName";
        
    }

    /**
     * Reads the profile plugin from an XML file <code>profile.xml</code> 
     * in the root directory (relatively to <code>cl</code> 
     * 
     * @param cl the class
     * @return the plugin profile
     * @throws ErrorLoadingPluginProfile error reading XML
     * 
     * @see org.argouml.profile.plugin.PluginProfileLoader#loadProfile(java.lang.Class)
     */
    public PluginProfile loadProfile(Class cl) throws ErrorLoadingPluginProfile {
        try {
            this.referenceClass = cl;

            XMLReader xr = XMLReaderFactory.createXMLReader();

            xr.setContentHandler(this);
            xr.setErrorHandler(this);

            xr.parse(new InputSource(new InputStreamReader(cl
                    .getResourceAsStream("profile.xml"))));

        } catch (SAXException e) {
            throw new ErrorLoadingPluginProfile(e);
        } catch (IOException e) {
            throw new ErrorLoadingPluginProfile(e);
        }

        return plugin;
    }

    /**
     * @see org.xml.sax.helpers.DefaultHandler#startDocument()
     */
    public void startDocument() {
        plugin = new PluginProfileImpl();
    }

    /**
     * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
     */
    public void startElement(String uri, String name, String qName,
            Attributes atts) {

        FigNodeDescriptor desc = null;
        JavaCriticDescriptor cdesc = null;
        
        if (name.equalsIgnoreCase(Tags.FIGNODE)) {
            desc = new FigNodeDescriptor();
        }

        if (name.equalsIgnoreCase(Tags.CRITIC)) {
            cdesc = new JavaCriticDescriptor();
        }

        for (int i = 0; i < atts.getLength(); ++i) {
            if (name.equalsIgnoreCase(Tags.PROFILE)) {
                if (atts.getLocalName(i).equalsIgnoreCase(Atts.PROFILE_AUTHOR)) {
                    plugin.setAuthor(atts.getValue(i));
                } else if (atts.getLocalName(i).equalsIgnoreCase(
                        Atts.PROFILE_DESCRIPTION)) {
                    plugin.setDescription(atts.getValue(i));
                } else if (atts.getLocalName(i).equalsIgnoreCase(
                        Atts.PROFILE_NAME)) {
                    plugin.setName(atts.getValue(i));
                } else if (atts.getLocalName(i).equalsIgnoreCase(
                        Atts.PROFILE_VERSION)) {
                    plugin.setVersion(atts.getValue(i));
                } else if (atts.getLocalName(i).equalsIgnoreCase(
                        Atts.PROFILE_DOWNLOADSITE)) {
                    plugin.setDownloadSite(atts.getValue(i));
                }
            } else if (name.equalsIgnoreCase(Tags.MODEL)) {
                if (atts.getLocalName(i).equalsIgnoreCase(Atts.MODEL_XMI)) {
                    try {
                        loadProfileModel(atts.getValue(i));
                    } catch (ProfileException e) {
                        e.printStackTrace();
                    }
                }
            } else if (name.equalsIgnoreCase(Tags.DEPENDENCY)) {
                if (atts.getLocalName(i).equalsIgnoreCase(
                        Atts.DEPENDENCY_PROFILE)) {
                    Profile dep = lookForRegisteredProfile(atts.getValue(i));

                    if (dep != null) {
                        profile.addDependency(dep);
                    }
                }
            } else if (name.equalsIgnoreCase(Tags.FIGNODE)) {
                if (atts.getLocalName(i).equalsIgnoreCase(
                        Atts.FIGNODE_STEREOTYPE)) {
                    desc.stereotype = atts.getValue(i);
                } else if (atts.getLocalName(i).equalsIgnoreCase(
                        Atts.FIGNODE_IMAGE)) {
                    desc.src = atts.getValue(i);
                } else if (atts.getLocalName(i).equalsIgnoreCase(
                        Atts.FIGNODE_LENGTH)) {
                    desc.length = Integer.parseInt(atts.getValue(i));
                }
            } else if (name.equalsIgnoreCase(Tags.CRITIC)) {
                if (atts.getLocalName(i).equalsIgnoreCase(
                        Atts.CRITIC_LANGUAGE)) {
                    
                } else if (atts.getLocalName(i).equalsIgnoreCase(
                        Atts.CRITIC_JAVA_CLASS_NAME)) {
                    cdesc.crClassName = atts.getValue(i); 
                } else if (atts.getLocalName(i).equalsIgnoreCase(
                        Atts.CRITIC_JAVA_MATERIAL_NAME)) {
                    cdesc.dmClassName = atts.getValue(i); 
                }
            }
        }

        if (desc != null && desc.isValid()) {
            loadImage(desc);
            profile.figNodeStrat.addDesrciptor(desc);
        }
        
        if (cdesc != null && cdesc.isValid()) {
            Class criticClass;
            try {
                criticClass = referenceClass.getClassLoader().loadClass(cdesc.crClassName);
                CrProfile criticObject = (CrProfile) criticClass.newInstance();
                Class dmClass = Class.forName(cdesc.dmClassName);
                
                Agency.register(criticObject, dmClass);
            } catch (Exception e) {
                LOG.error("Exception", e);                
            }
        }
    }

    private void loadImage(FigNodeDescriptor desc) {
        BufferedInputStream bis = new BufferedInputStream(this.referenceClass
                .getResourceAsStream(desc.src));

        byte[] buf = new byte[desc.length];
        try {
            bis.read(buf);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // LOG.info("IMAGE READ: " + fileName);
        desc.img = new ImageIcon(buf).getImage();// Toolkit.getDefaultToolkit().createImage(buf);

    }

    private Profile lookForRegisteredProfile(String value) {
        ProfileManager man = ProfileFacade.getManager();
        List<Profile> regs = man.getRegisteredProfiles();

        for (Profile profile : regs) {
            if (profile.getDisplayName().equalsIgnoreCase(value)) {
                return profile;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private void loadProfileModel(String profile) throws ProfileException {
        ProfileModelLoader profileModelLoader = new ResourceModelLoader(
                referenceClass);
        try {
            profileModel = profileModelLoader.loadModel(new ProfileReference(
                    profile, new URL("http://example.com/argouml/userprofiles/"
                            + profile)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (profileModel == null) {
            profileModel = new ArrayList();
            profileModel.add(Model.getModelManagementFactory().createModel());
        }
    }

    /**
     * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
     */
    public void endElement(String uri, String name, String qName) {
        if (name.equalsIgnoreCase(Tags.PROFILE)) {
            plugin.setProfile(profile);
        }
    }

    private static PluginProfileLoaderImpl instance = null;

    /**
     * @return the unique instance for this loader
     */
    public static PluginProfileLoader getInstance() {
        if (instance == null) {
            instance = new PluginProfileLoaderImpl();
        }
        return instance;
    }

}
