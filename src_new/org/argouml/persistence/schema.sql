\u uml;
#----------------
# data types
#----------------

# IMPORTANT:
# all the REFERENCES are wrong, the field name should always be
# "uuid"
# and not
# "XxxId"!
# when ever anyone wants to include these REFERENCES, make sure you fix this.
#


DROP TABLE tAggregationKind;
CREATE TABLE tAggregationKind(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid)); 
 
DROP TABLE tBoolean;
CREATE TABLE tBoolean(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid)); 
 
DROP TABLE tChangeableKind;
CREATE TABLE tChangeableKind(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid)); 
 
DROP TABLE tExpression;
CREATE TABLE tExpression(
    language VARCHAR(20),
    body VARCHAR(100),
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid)); 

DROP TABLE tBooleanExpression;
CREATE TABLE tBooleanExpression(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid));

DROP TABLE tProcedureExpression;
CREATE TABLE tProcedureExpression(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid));

DROP TABLE tParameterDirectionKind;
CREATE TABLE tParameterDirectionKind(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid)); 
 
DROP TABLE tMessageDirectionKind;
CREATE TABLE tMessageDirectionKind(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid));
 
DROP TABLE tScopeKind;
CREATE TABLE tScopeKind(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid)); 
 
DROP TABLE tVisibilityKind;
CREATE TABLE tVisibilityKind(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid)); 
 
DROP TABLE tCallConcurrencyKind;
CREATE TABLE tCallConcurrencyKind(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid)); 
 
DROP TABLE tMultiplicityRange;
CREATE TABLE tMultiplicityRange(
    lower INTEGER,
    upper INTEGER,
    MultiplicityId INTEGER(5) NOT NULL, # REFERENCES tMultiplicity(MultiplicityId),
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid)); 
 
DROP TABLE tMultiplicity;
CREATE TABLE tMultiplicity(
    range INTEGER(5) NOT NULL, # REFERENCES tMultiplicityRange(MultiplicityRangeId),
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid)); 
 
DROP TABLE tMapping;
CREATE TABLE tMapping(
    body VARCHAR(100),
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid)); 
 
DROP TABLE tOrderingKind;
CREATE TABLE tOrderingKind(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid)); 

#---------------------
# extension mechanisms
#---------------------
DROP TABLE tStereotype;
CREATE TABLE tStereotype(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid),
    icon BLOB,
    baseClass VARCHAR(100),
    TaggedValueId INTEGER(5) , # REFERENCES tTaggedValue(TaggedValueId),
    extendedElement INTEGER(5) , # REFERENCES tModelElement(ModelElementId),
    ConstraintId INTEGER(5) ); # REFERENCES tConstraint(ConstraintId));
 
DROP TABLE tTaggedValue;
CREATE TABLE tTaggedValue(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid),
    tag VARCHAR(100),
    value VARCHAR(100),
    ModelElementId INTEGER(5) , # REFERENCES tModelElement(ModelElementId),
    StereotypeId INTEGER(5) ); # REFERENCES tStereotype(StereotypeId));

#------------------
# core
#-----------------

DROP TABLE tAttribute;
CREATE TABLE tAttribute(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid),
    initialValue VARCHAR(100));
 
DROP TABLE tOperation;
CREATE TABLE tOperation(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid),
    concurrency VARCHAR(100),
    isRoot BIT,
    isLeaf BIT,
    isAbstract BIT,
    specification VARCHAR(100),
    MethodId INTEGER(5) ); # REFERENCES tMethod(MethodId)); # NOT USED!!!
 
DROP TABLE tMethod;
CREATE TABLE tMethod(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid),
    body VARCHAR(100),
    specification INTEGER(5) ); # REFERENCES tOperation(OperationId)); 
 
DROP TABLE tStructuralFeature;
CREATE TABLE tStructuralFeature(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid),
    multiplicity INTEGER(5), # REFERENCES tMultiplicity(MultiplicityId),
    changeability  INTEGER(5), # REFERENCES tChangeableKind(ChangeableKindId),
    targetScope INTEGER(5), # REFERENCES tScopeKind(ScopeKindId),
    type INTEGER(5) ); # REFERENCES tClassifier(ClassifierId));

DROP TABLE tBehavioralFeature;
CREATE TABLE tBehavioralFeature(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid),
    isQuery BIT,
    parameter INTEGER(5) ); # REFERENCES tParameter(ParameterId));
 
DROP TABLE tClassifier;
CREATE TABLE tClassifier(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid),
    feature INTEGER(5) ); # REFERENCES tFeature(FeatureId)); # NOT USED!!!
 
DROP TABLE tFeature;
CREATE TABLE tFeature(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid),
    ownerScope INTEGER(5), # REFERENCES tScopeKind(ScopeKindId),
    visibility INTEGER(5), # REFERENCES tVisibilityKind(VisibilityKindId),
    owner INTEGER(5) ); # REFERENCES tClassifier(ClassifierId));

DROP TABLE tNamespace;
CREATE TABLE tNamespace(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid),
    ownedElement INTEGER(5) ); # REFERENCES tModelElement(ModelElementId)); # NOT USED!!!

DROP TABLE tGeneralizableElement;
CREATE TABLE tGeneralizableElement(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid),
    isRoot BIT,
    isLeaf BIT,
    isAbstract BIT);

DROP TABLE tParameter;
CREATE TABLE tParameter(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid),
    defaultValue VARCHAR(100),
    kind INTEGER(5),  # REFERENCES tParameterDirectionKind(ParameterDirectionKindId),
    BehavioralFeatureId INTEGER(5), # REFERENCES tBehavioralFeature(BehavioralFeatureId),
    type INTEGER(5) ); # REFERENCES tClassifier(ClassifierId));

DROP TABLE tConstraint;
CREATE TABLE tConstraint(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid),
    body VARCHAR(100),
    constrainedElement INTEGER(5) ); # REFERENCES tModelElement(ModelElementId));

DROP TABLE tModelElement;
CREATE TABLE tModelElement(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid),
    name VARCHAR(100),
    namespace INTEGER(5), # REFERENCES tNamespace(NamespaceId),
    stereotype INTEGER(5), # REFERENCES tStereotype(StereotypeId),
    PackageId INTEGER(5)); # REFERENCES tPackage(PackageId),
    UMLClassName VARCHAR(100));

DROP TABLE tAssociation;
CREATE TABLE tAssociation(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid),
    classifier1 INTEGER(5), # REFERENCES tClassifier(ClassifierId));
    classifier2 INTEGER(5), # REFERENCES tClassifier(ClassifierId));
    associationEnd1 INTEGER(5), # REFERENCES tAssociationEnd(AssociationEndId));
    associationEnd2 INTEGER(5)); # REFERENCES tAssociationEnd(AssociationEndId));

DROP TABLE tAssociationEnd;
CREATE TABLE tAssociationEnd(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid),
    isNavigable BIT,
    ordering INTEGER(5), # REFERENCES tOrderingKind(OrderingKindId),
    aggregation INTEGER(5), # REFERENCES tAggregationKind(AggregationKindId),
    targetScope INTEGER(5), # REFERENCES tScopeKind(ScopeKindId),
    multiplicity INTEGER(5), # REFERENCES tMultiplicity(MultiplicityId),
    changeability INTEGER(5), # REFERENCES tChangeableKind(ChangeableKindId),
    visibility INTEGER(5), # REFERENCES tVisibilityKind(VisibilityKindId),
    type INTEGER(5), # REFERENCES tClassifier(ClassifierId),
    AssociationId INTEGER(5) ); # REFERENCES tAssociation(AssociationId));

DROP TABLE tClass;
CREATE TABLE tClass(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid),
    isActive BIT);

DROP TABLE tDependency;
CREATE TABLE tDependency(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid),
    supplier INTEGER(5), # REFERENCES tModelElement(ModelElementId),
    client INTEGER(5)); # REFERENCES tModelElement(ModelElementId));

DROP TABLE tBinding;
CREATE TABLE tBinding(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid),
    argument INTEGER(5)); # REFERENCES tModelElement(ModelElementId));

DROP TABLE tUsage;
CREATE TABLE tUsage(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid));

DROP TABLE tPermission;
CREATE TABLE tPermission(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid));

DROP TABLE tAbstraction;
CREATE TABLE tAbstraction(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid),
    mapping VARCHAR(100));

DROP TABLE tInterface;
CREATE TABLE tInterface(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid));

#------------------------
# model management
#------------------------

DROP TABLE tModel;
CREATE TABLE tModel(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid));

DROP TABLE tPackage;
CREATE TABLE tPackage(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid));

