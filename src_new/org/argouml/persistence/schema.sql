\u uml;
#----------------
# data types
#----------------
DROP TABLE tAggregationKind;
CREATE TABLE tAggregationKind(
    AggregationKindId INTEGER(5) NOT NULL,
    PRIMARY KEY(AggregationKindId)); 
 
DROP TABLE tBoolean;
CREATE TABLE tBoolean(
    BooleanId INTEGER(5) NOT NULL,
    PRIMARY KEY(BooleanId)); 
 
DROP TABLE tChangeableKind;
CREATE TABLE tChangeableKind(
    ChangeableKindId INTEGER(5) NOT NULL,
    PRIMARY KEY(ChangeableKindId)); 
 
DROP TABLE tExpression;
CREATE TABLE tExpression(
    language VARCHAR(20),
    body VARCHAR(100),
    ExpressionId INTEGER(5) NOT NULL,
    PRIMARY KEY(ExpressionId)); 

DROP TABLE tBooleanExpression;
CREATE TABLE tBooleanExpression(
    BooleanExpressionId INTEGER(5) NOT NULL,
    PRIMARY KEY(BooleanExpressionId));

DROP TABLE tProcedureExpression;
CREATE TABLE tProcedureExpression(
    ProcedureExpressionId INTEGER(5) NOT NULL,
    PRIMARY KEY(ProcedureExpressionId));

DROP TABLE tParameterDirectionKind;
CREATE TABLE tParameterDirectionKind(
    ParameterDirectionKindId INTEGER(5) NOT NULL,
    PRIMARY KEY(ParameterDirectionKindId)); 
 
DROP TABLE tMessageDirectionKind;
CREATE TABLE tMessageDirectionKind(
    MessageDirectionKindId INTEGER(5) NOT NULL,
    PRIMARY KEY(MessageDirectionKindId)); 
 
DROP TABLE tScopeKind;
CREATE TABLE tScopeKind(
    ScopeKindId INTEGER(5) NOT NULL,
    PRIMARY KEY(ScopeKindId)); 
 
DROP TABLE tVisibilityKind;
CREATE TABLE tVisibilityKind(
    VisibilityKindId INTEGER(5) NOT NULL,
    PRIMARY KEY(VisibilityKindId)); 
 
DROP TABLE tCallConcurrencyKind;
CREATE TABLE tCallConcurrencyKind(
    CallConcurrencyKindId INTEGER(5) NOT NULL,
    PRIMARY KEY(CallConcurrencyKindId)); 
 
DROP TABLE tMultiplicityRange;
CREATE TABLE tMultiplicityRange(
    lower INTEGER,
    upper INTEGER,
    MultiplicityId INTEGER(5) NOT NULL, # REFERENCES tMultiplicity(MultiplicityId),
    MultiplicityRangeId INTEGER(5) NOT NULL,
    PRIMARY KEY(MultiplicityRangeId)); 
 
DROP TABLE tMultiplicity;
CREATE TABLE tMultiplicity(
    range INTEGER(5) NOT NULL, # REFERENCES tMultiplicityRange(MultiplicityRangeId),
    MultiplicityId INTEGER(5) NOT NULL,
    PRIMARY KEY(MultiplicityId)); 
 
DROP TABLE tMapping;
CREATE TABLE tMapping(
    body VARCHAR(100),
    MappingId INTEGER(5) NOT NULL,
    PRIMARY KEY(MappingId)); 
 
DROP TABLE tOrderingKind;
CREATE TABLE tOrderingKind(
    OrderingKindId INTEGER(5) NOT NULL,
    PRIMARY KEY(OrderingKindId)); 

#---------------------
# extension mechanisms
#---------------------
DROP TABLE tStereotype;
CREATE TABLE tStereotype(
    StereotypeId INTEGER(5) NOT NULL,
    PRIMARY KEY(StereotypeID),
    icon BLOB,
    baseClass VARCHAR(100),
    TaggedValueId INTEGER(5) , # REFERENCES tTaggedValue(TaggedValueId),
    extendedElement INTEGER(5) , # REFERENCES tModelElement(ModelElementId),
    ConstraintId INTEGER(5) ); # REFERENCES tConstraint(ConstraintId));
 
DROP TABLE tTaggedValue;
CREATE TABLE tTaggedValue(
    TaggedValueId INTEGER(5) NOT NULL,
    PRIMARY KEY(TaggedValueId),
    tag VARCHAR(100),
    value VARCHAR(100),
    ModelElementId INTEGER(5) , # REFERENCES tModelElement(ModelElementId),
    StereotypeId INTEGER(5) ); # REFERENCES tStereotype(StereotypeId));

#------------------
# core
#-----------------

DROP TABLE tAttribute;
CREATE TABLE tAttribute(
        AttributeId INTEGER(5) NOT NULL,
        PRIMARY KEY(AttributeId),
        initialValue VARCHAR(100));
 
DROP TABLE tOperation;
CREATE TABLE tOperation(
    OperationId INTEGER(5) NOT NULL,
    PRIMARY KEY(OperationId),
    concurrency VARCHAR(100),
    isRoot BIT,
    isLeaf BIT,
    isAbstract BIT,
    specification VARCHAR(100),
    MethodId INTEGER(5) ); # REFERENCES tMethod(MethodId)); # NOT USED!!!
 
DROP TABLE tMethod;
CREATE TABLE tMethod(
    MethodId INTEGER(5) NOT NULL,
    PRIMARY KEY(MethodId),
    body VARCHAR(100),
    specification INTEGER(5) ); # REFERENCES tOperation(OperationId)); 
 
DROP TABLE tStructuralFeature;
CREATE TABLE tStructuralFeature(
    StructuralFeatureId INTEGER(5) NOT NULL,
    PRIMARY KEY(StructuralFeatureId),
    multiplicity INTEGER(5), # REFERENCES tMultiplicity(MultiplicityId),
    changeability  INTEGER(5), # REFERENCES tChangeableKind(ChangeableKindId),
    targetScope INTEGER(5), # REFERENCES tScopeKind(ScopeKindId),
    type INTEGER(5) ); # REFERENCES tClassifier(ClassifierId));

DROP TABLE tBehavioralFeature;
CREATE TABLE tBehavioralFeature(
    BehavioralFeatureId INTEGER(5) NOT NULL,
    PRIMARY KEY(BehavioralFeatureId),
    isQuery BIT,
    parameter INTEGER(5) ); # REFERENCES tParameter(ParameterId));
 
DROP TABLE tClassifier;
CREATE TABLE tClassifier(
    ClassifierId INTEGER(5) NOT NULL,
    PRIMARY KEY(ClassifierId),
    feature INTEGER(5) ); # REFERENCES tFeature(FeatureId)); # NOT USED!!!
 
DROP TABLE tFeature;
CREATE TABLE tFeature(
    FeatureId INTEGER(5) NOT NULL,
    PRIMARY KEY(FeatureId),
    ownerScope INTEGER(5), # REFERENCES tScopeKind(ScopeKindId),
    visibility INTEGER(5), # REFERENCES tVisibilityKind(VisibilityKindId),
    owner INTEGER(5) ); # REFERENCES tClassifier(ClassifierId));

DROP TABLE tNamespace;
CREATE TABLE tNamespace(
    NamespaceId INTEGER(5) NOT NULL,
    PRIMARY KEY(NamespaceId),
    ownedElement INTEGER(5) ); # REFERENCES tModelElement(ModelElementId)); # NOT USED!!!

DROP TABLE tGeneralizableElement;
CREATE TABLE tGeneralizableElement(
    GeneralizableElementId INTEGER(5) NOT NULL,
    PRIMARY KEY(GeneralizableElementId),
    isRoot BIT,
    isLeaf BIT,
    isAbstract BIT);

DROP TABLE tParameter;
CREATE TABLE tParameter(
    ParameterId INTEGER(5) NOT NULL,
    PRIMARY KEY(ParameterId),
    defaultValue VARCHAR(100),
    kind INTEGER(5),  # REFERENCES tParameterDirectionKind(ParameterDirectionKindId),
    BehavioralFeatureId INTEGER(5), # REFERENCES tBehavioralFeature(BehavioralFeatureId),
    type INTEGER(5) ); # REFERENCES tClassifier(ClassifierId));

DROP TABLE tConstraint;
CREATE TABLE tConstraint(
    ConstraintId INTEGER(5) NOT NULL,
    PRIMARY KEY(ConstraintId),
    body VARCHAR(100),
    constrainedElement INTEGER(5) ); # REFERENCES tModelElement(ModelElementId));

DROP TABLE tModelElement;
CREATE TABLE tModelElement(
    ModelElementId INTEGER(5) NOT NULL,
    PRIMARY KEY(ModelElementId),
    name VARCHAR(100),
    namespace INTEGER(5), # REFERENCES tNamespace(NamespaceId),
    constraint INTEGER(5), # REFERENCES tConstraint(ConstraintId),
    PackageId INTEGER(5) ); # REFERENCES tPackage(PackageId));

DROP TABLE tAssociation;
CREATE TABLE tAssociation(
    AssociationId INTEGER(5) NOT NULL,
    PRIMARY KEY(AssociationId),
    classifier1 INTEGER(5), # REFERENCES tClassifier(ClassifierId));
    classifier2 INTEGER(5), # REFERENCES tClassifier(ClassifierId));
    associationEnd1 INTEGER(5), # REFERENCES tAssociationEnd(AssociationEndId));
    associationEnd2 INTEGER(5)); # REFERENCES tAssociationEnd(AssociationEndId));

DROP TABLE tAssociationEnd;
CREATE TABLE tAssociationEnd(
    AssociationEndId INTEGER(5) NOT NULL,
    PRIMARY KEY(AssociationEndId),
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
    ClassId INTEGER(5) NOT NULL,
    PRIMARY KEY(ClassId),
    isActive BIT);

DROP TABLE tDependency;
CREATE TABLE tDependency(
    DependencyId INTEGER(5) NOT NULL,
    PRIMARY KEY(DependencyId),
    supplier INTEGER(5), # REFERENCES tModelElement(ModelElementId),
    client INTEGER(5)); # REFERENCES tModelElement(ModelElementId));

DROP TABLE tBinding;
CREATE TABLE tBinding(
    BindingId INTEGER(5) NOT NULL,
    PRIMARY KEY(BindingId),
    argument INTEGER(5)); # REFERENCES tModelElement(ModelElementId));

DROP TABLE tUsage;
CREATE TABLE tUsage(
    UsageId INTEGER(5) NOT NULL,
    PRIMARY KEY(UsageId));

DROP TABLE tPermission;
CREATE TABLE tPermission(
    PermissionId INTEGER(5) NOT NULL,
    PRIMARY KEY(PermissionId));

DROP TABLE tAbstraction;
CREATE TABLE tAbstraction(
    AbstractionId INTEGER(5) NOT NULL,
    PRIMARY KEY(AbstractionId),
    mapping VARCHAR(100));

DROP TABLE tInterface;
CREATE TABLE tInterface(
    InterfaceId INTEGER(5) NOT NULL,
    PRIMARY KEY(InterfaceId));

#------------------------
# model management
#------------------------

DROP TABLE tModel;
CREATE TABLE tModel(
    ModelId INTEGER(5) NOT NULL,
    PRIMARY KEY(ModelId));

DROP TABLE tPackage;
CREATE TABLE tPackage(
    PackageId INTEGER(5) NOT NULL,
    PRIMARY KEY(PackageId));

