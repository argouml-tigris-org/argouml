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


CREATE TABLE tAggregationKind(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid)); 
 
CREATE TABLE tBoolean(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid)); 
 
CREATE TABLE tChangeableKind(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid)); 
 
CREATE TABLE tExpression(
    language VARCHAR(20),
    body VARCHAR(100),
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid)); 

CREATE TABLE tBooleanExpression(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid));

CREATE TABLE tProcedureExpression(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid));

CREATE TABLE tParameterDirectionKind(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid)); 
 
CREATE TABLE tMessageDirectionKind(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid));
 
CREATE TABLE tScopeKind(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid)); 
 
CREATE TABLE tVisibilityKind(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid)); 
 
CREATE TABLE tCallConcurrencyKind(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid)); 
 
CREATE TABLE tMultiplicityRange(
    lower INTEGER,
    upper INTEGER,
    MultiplicityId INTEGER(5) NOT NULL, # REFERENCES tMultiplicity(MultiplicityId),
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid)); 
 
CREATE TABLE tMultiplicity(
    range INTEGER(5) NOT NULL, # REFERENCES tMultiplicityRange(MultiplicityRangeId),
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid)); 
 
CREATE TABLE tMapping(
    body VARCHAR(100),
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid)); 
 
CREATE TABLE tOrderingKind(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid)); 

#---------------------
# extension mechanisms
#---------------------
CREATE TABLE tStereotype(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid),
    icon BLOB,
    baseClass VARCHAR(100),
    TaggedValueId INTEGER(5) , # REFERENCES tTaggedValue(TaggedValueId),
    extendedElement INTEGER(5) , # REFERENCES tModelElement(ModelElementId),
    ConstraintId INTEGER(5) ); # REFERENCES tConstraint(ConstraintId));
 
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

CREATE TABLE tAttribute(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid),
    initialValue VARCHAR(100));
 
CREATE TABLE tOperation(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid),
    concurrency VARCHAR(100),
    isRoot BIT,
    isLeaf BIT,
    isAbstract BIT,
    specification VARCHAR(100),
    MethodId INTEGER(5) ); # REFERENCES tMethod(MethodId)); # NOT USED!!!
 
CREATE TABLE tMethod(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid),
    body VARCHAR(100),
    specification INTEGER(5) ); # REFERENCES tOperation(OperationId)); 
 
CREATE TABLE tStructuralFeature(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid),
    multiplicity INTEGER(5), # REFERENCES tMultiplicity(MultiplicityId),
    changeability  INTEGER(5), # REFERENCES tChangeableKind(ChangeableKindId),
    targetScope INTEGER(5), # REFERENCES tScopeKind(ScopeKindId),
    type INTEGER(5) ); # REFERENCES tClassifier(ClassifierId));

CREATE TABLE tBehavioralFeature(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid),
    isQuery BIT,
    parameter INTEGER(5) ); # REFERENCES tParameter(ParameterId));
 
CREATE TABLE tClassifier(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid),
    feature INTEGER(5) ); # REFERENCES tFeature(FeatureId)); # NOT USED!!!
 
CREATE TABLE tFeature(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid),
    ownerScope INTEGER(5), # REFERENCES tScopeKind(ScopeKindId),
    visibility INTEGER(5), # REFERENCES tVisibilityKind(VisibilityKindId),
    owner INTEGER(5) ); # REFERENCES tClassifier(ClassifierId));

CREATE TABLE tNamespace(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid),
    ownedElement INTEGER(5) ); # REFERENCES tModelElement(ModelElementId)); # NOT USED!!!

CREATE TABLE tGeneralizableElement(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid),
    isRoot BIT,
    isLeaf BIT,
    isAbstract BIT);

CREATE TABLE tParameter(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid),
    defaultValue VARCHAR(100),
    kind INTEGER(5),  # REFERENCES tParameterDirectionKind(ParameterDirectionKindId),
    BehavioralFeatureId INTEGER(5), # REFERENCES tBehavioralFeature(BehavioralFeatureId),
    type INTEGER(5) ); # REFERENCES tClassifier(ClassifierId));

CREATE TABLE tConstraint(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid),
    body VARCHAR(100),
    constrainedElement INTEGER(5) ); # REFERENCES tModelElement(ModelElementId));

CREATE TABLE tModelElement(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid),
    name VARCHAR(100),
    namespace INTEGER(5), # REFERENCES tNamespace(NamespaceId),
    stereotype INTEGER(5), # REFERENCES tStereotype(StereotypeId),
    PackageId INTEGER(5), # REFERENCES tPackage(PackageId),
    UMLClassName VARCHAR(100));

CREATE TABLE tAssociation(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid),
    classifier1 INTEGER(5), # REFERENCES tClassifier(ClassifierId));
    classifier2 INTEGER(5), # REFERENCES tClassifier(ClassifierId));
    associationEnd1 INTEGER(5), # REFERENCES tAssociationEnd(AssociationEndId));
    associationEnd2 INTEGER(5)); # REFERENCES tAssociationEnd(AssociationEndId));

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

CREATE TABLE tClass(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid),
    isActive BIT);

CREATE TABLE tDependency(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid),
    supplier INTEGER(5), # REFERENCES tModelElement(ModelElementId),
    client INTEGER(5)); # REFERENCES tModelElement(ModelElementId));

CREATE TABLE tBinding(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid),
    argument INTEGER(5)); # REFERENCES tModelElement(ModelElementId));

CREATE TABLE tUsage(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid));

CREATE TABLE tPermission(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid));

CREATE TABLE tAbstraction(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid),
    mapping VARCHAR(100));

CREATE TABLE tInterface(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid));

#------------------------
# model management
#------------------------

CREATE TABLE tModel(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid));

CREATE TABLE tPackage(
    uuid VARCHAR(50) NOT NULL,
    PRIMARY KEY(uuid));

