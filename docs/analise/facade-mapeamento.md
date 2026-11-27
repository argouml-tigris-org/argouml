# Mapeamento dos métodos da Facade

Os **393 métodos** foram classificados em **11 subdomínios UML**, de acordo com suas responsabilidades. Cada seção explica o grupo e apresenta uma descrição breve dos métodos que compõem a subinterface proposta.

## Métricas de apoio

| Arquivo | Linhas físicas do arquivo | Métodos declarados |
|---|---:|---:|
| Facade.java | 3.631 | 393 |
| FacadeMDRImpl.java | 4.779 | 409 |
| FacadeEUMLImpl.java | 2.824 | 399 |

**Critério de contagem:** LOC física corresponde a todas as linhas do arquivo, incluindo comentários e linhas em branco. A quantidade de métodos considera as declarações diretamente no tipo principal, de todas as visibilidades, contando cada sobrecarga separadamente e excluindo construtores e métodos herdados.

O levantamento encontrou **393 métodos ativos na Facade**.

## Visão geral da classificação

| Subdomínio UML | Subinterface proposta | Métodos |
|---|---|---:|
| Estrutura e núcleo UML | [`CoreFacade`](#1-corefacade) | 132 |
| Organização do modelo | [`ModelManagementFacade`](#2-modelmanagementfacade) | 14 |
| Casos de uso | [`UseCasesFacade`](#3-usecasesfacade) | 16 |
| Máquinas de estados | [`StateMachinesFacade`](#4-statemachinesfacade) | 54 |
| Grafos de atividades | [`ActivityGraphsFacade`](#5-activitygraphsfacade) | 18 |
| Colaborações e interações | [`CollaborationsFacade`](#6-collaborationsfacade) | 34 |
| Comportamento comum | [`CommonBehaviorFacade`](#7-commonbehaviorfacade) | 51 |
| Tipos de dados auxiliares | [`DataTypesFacade`](#8-datatypesfacade) | 13 |
| Extensão e perfis UML | [`ExtensionMechanismsFacade`](#9-extensionmechanismsfacade) | 30 |
| Introspecção do modelo | [`ModelIntrospectionFacade`](#10-modelintrospectionfacade) | 8 |
| Navegação e consultas transversais | [`ModelNavigationFacade`](#11-modelnavigationfacade) | 23 |
| **Total** | **11 subinterfaces** | **393** |

## 1. CoreFacade

**Estrutura e núcleo UML · 132 métodos**

Reúne as consultas da estrutura básica do modelo: classificadores, atributos, operações, associações, dependências, generalizações e templates. Também inclui propriedades estruturais, como nome, visibilidade, agregação e escopo.

| Método | Descrição | Subinterface proposta |
|---|---|---|
| `boolean isAAbstraction(Object handle)` | Reconhece elementos correspondentes a Abstraction no modelo UML. | `CoreFacade` |
| `boolean isAArtifact(Object handle)` | Reconhece elementos correspondentes a Artifact no modelo UML. | `CoreFacade` |
| `boolean isAAssociation(Object handle)` | Reconhece elementos correspondentes a Association no modelo UML. | `CoreFacade` |
| `boolean isAAssociationEnd(Object handle)` | Reconhece elementos correspondentes a AssociationEnd no modelo UML. | `CoreFacade` |
| `boolean isAAttribute(Object handle)` | Reconhece elementos correspondentes a Attribute no modelo UML. | `CoreFacade` |
| `boolean isAbstract(Object handle)` | Consulta o indicador de abstração de classes ou operações. | `CoreFacade` |
| `boolean isABehavioralFeature(Object handle)` | Reconhece elementos correspondentes a BehavioralFeature no modelo UML. | `CoreFacade` |
| `boolean isABinding(Object handle)` | Reconhece elementos correspondentes a Binding no modelo UML. | `CoreFacade` |
| `boolean isAClass(Object handle)` | Reconhece elementos correspondentes a Class no modelo UML. | `CoreFacade` |
| `boolean isAAssociationClass(Object handle)` | Reconhece elementos correspondentes a AssociationClass no modelo UML. | `CoreFacade` |
| `boolean isAClassifier(Object handle)` | Reconhece elementos correspondentes a Classifier no modelo UML. | `CoreFacade` |
| `boolean isAComment(Object handle)` | Reconhece elementos correspondentes a Comment no modelo UML. | `CoreFacade` |
| `boolean isAComponent(Object handle)` | Reconhece elementos correspondentes a Component no modelo UML. | `CoreFacade` |
| `boolean isAComponentRealization(Object handle)` | Reconhece elementos correspondentes a ComponentRealization no modelo UML. | `CoreFacade` |
| `boolean isAConnector(Object handle)` | Reconhece elementos correspondentes a Connector no modelo UML. | `CoreFacade` |
| `boolean isAConnectorEnd(Object handle)` | Reconhece elementos correspondentes a ConnectorEnd no modelo UML. | `CoreFacade` |
| `boolean isAConstraint(Object handle)` | Reconhece elementos correspondentes a Constraint no modelo UML. | `CoreFacade` |
| `boolean isADataType(Object handle)` | Reconhece elementos correspondentes a DataType no modelo UML. | `CoreFacade` |
| `boolean isADependency(Object handle)` | Reconhece elementos correspondentes a Dependency no modelo UML. | `CoreFacade` |
| `boolean isADirectedRelationship(Object handle)` | Reconhece elementos correspondentes a DirectedRelationship no modelo UML. | `CoreFacade` |
| `boolean isAElement(Object handle)` | Reconhece elementos correspondentes a Element no modelo UML. | `CoreFacade` |
| `boolean isAElementResidence(Object handle)` | Reconhece elementos correspondentes a ElementResidence no modelo UML. | `CoreFacade` |
| `boolean isAEnumeration(Object handle)` | Reconhece elementos correspondentes a Enumeration no modelo UML. | `CoreFacade` |
| `boolean isAEnumerationLiteral(Object handle)` | Reconhece elementos correspondentes a EnumerationLiteral no modelo UML. | `CoreFacade` |
| `boolean isAFeature(Object handle)` | Reconhece elementos correspondentes a Feature no modelo UML. | `CoreFacade` |
| `boolean isAFlow(Object handle)` | Reconhece elementos correspondentes a Flow no modelo UML. | `CoreFacade` |
| `boolean isAGeneralizableElement(Object handle)` | Reconhece elementos correspondentes a GeneralizableElement no modelo UML. | `CoreFacade` |
| `boolean isAGeneralization(Object handle)` | Reconhece elementos correspondentes a Generalization no modelo UML. | `CoreFacade` |
| `boolean isAInterface(Object handle)` | Reconhece elementos correspondentes a Interface no modelo UML. | `CoreFacade` |
| `boolean isAMethod(Object handle)` | Reconhece elementos correspondentes a Method no modelo UML. | `CoreFacade` |
| `boolean isAModelElement(Object handle)` | Reconhece elementos correspondentes a ModelElement no modelo UML. | `CoreFacade` |
| `boolean isANamedElement(Object handle)` | Reconhece elementos correspondentes a NamedElement no modelo UML. | `CoreFacade` |
| `boolean isANamespace(Object handle)` | Reconhece elementos correspondentes a Namespace no modelo UML. | `CoreFacade` |
| `boolean isANaryAssociation(Object handle)` | Identifica uma associação com múltiplas extremidades. | `CoreFacade` |
| `boolean isANode(Object handle)` | Reconhece elementos correspondentes a Node no modelo UML. | `CoreFacade` |
| `boolean isAOperation(Object handle)` | Reconhece elementos correspondentes a Operation no modelo UML. | `CoreFacade` |
| `boolean isAParameter(Object handle)` | Reconhece elementos correspondentes a Parameter no modelo UML. | `CoreFacade` |
| `boolean isAPort(Object handle)` | Reconhece elementos correspondentes a Port no modelo UML. | `CoreFacade` |
| `boolean isAPrimitiveType(Object handle)` | Reconhece elementos correspondentes a PrimitiveType no modelo UML. | `CoreFacade` |
| `boolean isAProperty(Object handle)` | Reconhece elementos correspondentes a Property no modelo UML. | `CoreFacade` |
| `boolean isARelationship(Object handle)` | Reconhece elementos correspondentes a Relationship no modelo UML. | `CoreFacade` |
| `boolean isAStructuralFeature(Object handle)` | Reconhece elementos correspondentes a StructuralFeature no modelo UML. | `CoreFacade` |
| `boolean isATemplateArgument(Object handle)` | Reconhece elementos correspondentes a TemplateArgument no modelo UML. | `CoreFacade` |
| `boolean isATemplateParameter(Object handle)` | Reconhece elementos correspondentes a TemplateParameter no modelo UML. | `CoreFacade` |
| `boolean isAUsage(Object handle)` | Reconhece elementos correspondentes a Usage no modelo UML. | `CoreFacade` |
| `boolean isActive(Object handle)` | Consulta o indicador de classe ativa. | `CoreFacade` |
| `boolean isConstructor(Object handle)` | Reconhece uma operação ou um método que representa um construtor. | `CoreFacade` |
| `boolean isFrozen(Object handle)` | Verifica se o elemento possui a condição frozen. | `CoreFacade` |
| `boolean isComposite(Object handle)` | Verifica a condição de composição na agregação. | `CoreFacade` |
| `boolean isAggregate(Object handle)` | Verifica a condição de agregação compartilhada. | `CoreFacade` |
| `boolean isInitialized(Object handle)` | Verifica se um atributo possui expressão de valor inicial com corpo preenchido. | `CoreFacade` |
| `boolean isLeaf(Object handle)` | Consulta o indicador leaf do elemento generalizável. | `CoreFacade` |
| `boolean isRoot(Object handle)` | Consulta o indicador root do elemento generalizável. | `CoreFacade` |
| `boolean isSpecification(Object handle)` | Consulta o atributo isSpecification do elemento. | `CoreFacade` |
| `boolean isNavigable(Object handle)` | Verifica se uma extremidade de associação é navegável. | `CoreFacade` |
| `boolean isPackage(Object handle)` | Verifica se a visibilidade corresponde a package. | `CoreFacade` |
| `boolean isPrivate(Object handle)` | Verifica se a visibilidade corresponde a private. | `CoreFacade` |
| `boolean isPublic(Object handle)` | Verifica se a visibilidade corresponde a public. | `CoreFacade` |
| `boolean isQuery(Object handle)` | Consulta o indicador de operação de consulta. | `CoreFacade` |
| `boolean isProtected(Object handle)` | Verifica se a visibilidade corresponde a protected. | `CoreFacade` |
| `boolean isReturn(Object handle)` | Verifica se um parâmetro tem direção de retorno. | `CoreFacade` |
| `Object getAssociation(Object handle)` | Retorna a associação de uma extremidade de associação ou de uma ligação. | `CoreFacade` |
| `Collection getAssociations(Object handle)` | Consulta as associações vinculadas ao elemento. | `CoreFacade` |
| `Object getAssociationEnd(Object classifier, Object association)` | Localiza a extremidade entre o classificador e a associação informados. | `CoreFacade` |
| `Collection getAssociationEnds(Object handle)` | Consulta as extremidades de associação do elemento. | `CoreFacade` |
| `List getAttributes(Object handle)` | Lista os atributos do classificador. | `CoreFacade` |
| `Object getBehavioralFeature(Object handle)` | Retorna a característica comportamental à qual pertence um parâmetro. | `CoreFacade` |
| `Object getChangeability(Object handle)` | Consulta o valor de mutabilidade da característica estrutural. Método com indicação de depreciação no contrato original. | `CoreFacade` |
| `Object getSpecific(Object handle)` | Consulta o elemento específico de uma generalização. | `CoreFacade` |
| `Collection getChildren(Object handle)` | Lista os descendentes de um elemento generalizável. | `CoreFacade` |
| `Object getClassifier(Object handle)` | Retorna o classificador participante de uma extremidade de associação. | `CoreFacade` |
| `Collection getClients(Object handle)` | Lista os clientes de uma dependência. | `CoreFacade` |
| `Collection getClientDependencies(Object handle)` | Lista as dependências em que o elemento participa como cliente. | `CoreFacade` |
| `Object getConcurrency(Object handle)` | Consulta o valor de concorrência da operação. | `CoreFacade` |
| `Collection getElementResidences(Object handle)` | Consulta as relações de residência de elementos. | `CoreFacade` |
| `Object getEnumeration(Object handle)` | Retorna a enumeração à qual pertence um literal. | `CoreFacade` |
| `List getEnumerationLiterals(Object handle)` | Lista os literais de uma enumeração. | `CoreFacade` |
| `List getFeatures(Object handle)` | Lista as características do classificador. | `CoreFacade` |
| `Object getGeneralization(Object handle, Object parent)` | Localiza a generalização entre o elemento e o pai informados. | `CoreFacade` |
| `Collection getGeneralizations(Object handle)` | Lista as generalizações do elemento. | `CoreFacade` |
| `Object getInitialValue(Object handle)` | Consulta a expressão de valor inicial do atributo. | `CoreFacade` |
| `Collection getComments(Object handle)` | Lista os comentários associados ao elemento. | `CoreFacade` |
| `Collection getAnnotatedElements(Object handle)` | Lista os elementos anotados por um comentário. | `CoreFacade` |
| `List getConstrainedElements(Object handle)` | Lista os elementos sujeitos a uma restrição. | `CoreFacade` |
| `Collection getConstraints(Object handle)` | Lista as restrições associadas ao elemento. | `CoreFacade` |
| `Object getDefaultValue(Object handle)` | Consulta o valor padrão de um parâmetro. | `CoreFacade` |
| `Collection getDeployedComponents(Object handle)` | Lista os componentes implantados no elemento. | `CoreFacade` |
| `Collection getDeploymentLocations(Object handle)` | Consulta os locais de implantação do elemento. | `CoreFacade` |
| `Object getDiscriminator(Object handle)` | Consulta o discriminador de uma generalização. Método com indicação de depreciação no contrato original. | `CoreFacade` |
| `Collection getMethods(Object handle)` | Lista os métodos associados ao elemento. | `CoreFacade` |
| `Object getNamespace(Object handle)` | Retorna o espaço de nomes do elemento. | `CoreFacade` |
| `List getOperations(Object handle)` | Lista as operações do classificador. | `CoreFacade` |
| `List getOperationsAndReceptions(Object handle)` | Lista operações e recepções do classificador. | `CoreFacade` |
| `Object getNextEnd(Object handle)` | Retorna a próxima extremidade da associação, com retorno ao início da lista. | `CoreFacade` |
| `Object getOrdering(Object handle)` | Consulta o valor de ordenação de uma extremidade de associação. | `CoreFacade` |
| `Collection getOtherAssociationEnds(Object handle)` | Lista as demais extremidades da associação. | `CoreFacade` |
| `Collection getOwnedElements(Object handle)` | Lista os elementos pertencentes ao elemento informado. | `CoreFacade` |
| `Object getPowertype(Object handle)` | Consulta o classificador powertype da generalização. | `CoreFacade` |
| `Collection getPowertypeRanges(Object handle)` | Consulta as generalizações relacionadas pelo powertype. | `CoreFacade` |
| `List getQualifiers(Object handle)` | Lista os atributos qualificadores da extremidade de associação. | `CoreFacade` |
| `boolean hasReturnParameterDirectionKind(Object handle)` | Verifica a presença da direção de parâmetro return. | `CoreFacade` |
| `Object getParameter(Object handle)` | Retorna o elemento associado a um TemplateParameter. | `CoreFacade` |
| `Object getGeneral(Object handle)` | Consulta o elemento geral de uma generalização. | `CoreFacade` |
| `Collection getRaisedSignals(Object handle)` | Lista os sinais que podem ser emitidos pelo elemento. Método com indicação de depreciação no contrato original. | `CoreFacade` |
| `Collection getRaisedExceptions(Object handle)` | Lista as exceções declaradas pelo elemento. | `CoreFacade` |
| `Object getResident(Object handle)` | Retorna o elemento residente de uma relação de residência. | `CoreFacade` |
| `Collection getResidentElements(Object handle)` | Lista os elementos residentes associados ao elemento. | `CoreFacade` |
| `Collection getResidents(Object handle)` | Consulta os residentes do elemento conforme o contrato legado. | `CoreFacade` |
| `Collection getSources(Object handle)` | Lista as origens de um fluxo ou relacionamento direcionado. | `CoreFacade` |
| `Collection getSourceFlows(Object handle)` | Lista os fluxos em que o elemento participa como origem. | `CoreFacade` |
| `Collection getSpecializations(Object handle)` | Lista as especializações do elemento. | `CoreFacade` |
| `Collection getSupplierDependencies(Object handle)` | Lista as dependências em que o elemento participa como fornecedor. | `CoreFacade` |
| `Collection getTargets(Object handle)` | Lista os destinos de um relacionamento direcionado. | `CoreFacade` |
| `Object getTargetScope(Object handle)` | Consulta o escopo de destino do elemento. Método com indicação de depreciação no contrato original. | `CoreFacade` |
| `Collection getTargetFlows(Object handle)` | Lista os fluxos em que o elemento participa como destino. | `CoreFacade` |
| `List getTemplateParameters(Object handle)` | Lista os parâmetros de template do elemento. | `CoreFacade` |
| `Object getDefaultElement(Object handle)` | Consulta o elemento padrão de um parâmetro de template. | `CoreFacade` |
| `Object getTemplate(Object handle)` | Retorna o template associado ao elemento. | `CoreFacade` |
| `Object getBinding(Object handle)` | Retorna a vinculação associada ao argumento de template. | `CoreFacade` |
| `List getArguments(Object handle)` | Lista os argumentos de template associados a uma Binding. | `CoreFacade` |
| `List getStructuralFeatures(Object handle)` | Lista as características estruturais do classificador. | `CoreFacade` |
| `Collection getSpecifications(Object handle)` | Consulta as especificações de uma extremidade ou as interfaces realizadas por um classificador. | `CoreFacade` |
| `Collection getSuppliers(Object handle)` | Lista os fornecedores de uma dependência. | `CoreFacade` |
| `Object getAggregation(Object handle)` | Consulta a agregação associada ao elemento. | `CoreFacade` |
| `Object getAggregation1(Object handle)` | Consulta a agregação de uma extremidade segundo a convenção UML 1. | `CoreFacade` |
| `Object getAggregation2(Object handle)` | Consulta a agregação de uma extremidade segundo a convenção UML 2. | `CoreFacade` |
| `Collection getAssociatedClasses(Object handle)` | Lista as classes conectadas ao classificador por associações. | `CoreFacade` |
| `String getName(Object handle)` | Consulta o nome do elemento. | `CoreFacade` |
| `Object getVisibility(Object handle)` | Consulta a visibilidade do elemento. | `CoreFacade` |
| `Object lookupIn(Object handle, String name)` | Procura um elemento pelo nome no contexto informado. | `CoreFacade` |
| `boolean isStatic(Object handle)` | Verifica se o elemento tem escopo estático. | `CoreFacade` |
| `boolean isReadOnly(Object handle)` | Consulta a condição de somente leitura do elemento. | `CoreFacade` |

---

## 2. ModelManagementFacade

**Organização do modelo · 14 métodos**

Agrupa as consultas que organizam o modelo em pacotes, modelos e subsistemas. Inclui a localização de elementos raiz e a navegação pelas importações entre pacotes.

| Método | Descrição | Subinterface proposta |
|---|---|---|
| `boolean isAElementImport(Object handle)` | Reconhece elementos correspondentes a ElementImport no modelo UML. | `ModelManagementFacade` |
| `boolean isAModel(Object handle)` | Reconhece elementos correspondentes a Model no modelo UML. | `ModelManagementFacade` |
| `boolean isAPackageImport(Object handle)` | Reconhece elementos correspondentes a PackageImport no modelo UML. | `ModelManagementFacade` |
| `boolean isAPackage(Object handle)` | Reconhece elementos correspondentes a Package no modelo UML. | `ModelManagementFacade` |
| `boolean isASubsystem(Object handle)` | Reconhece elementos correspondentes a Subsystem no modelo UML. | `ModelManagementFacade` |
| `Collection getElementImports2(Object handle)` | Lista as relações de importação que referenciam este elemento como elemento importado. | `ModelManagementFacade` |
| `Collection getElementImports(Object handle)` | Consulta as importações de elementos do pacote. | `ModelManagementFacade` |
| `Object getInnerContainingModel(Object handle)` | Localiza o modelo contêiner mais próximo do elemento. | `ModelManagementFacade` |
| `Object getRoot(Object handle)` | Localiza o elemento superior que não possui proprietário. | `ModelManagementFacade` |
| `Collection getRootElements()` | Lista os elementos raiz disponíveis no modelo. | `ModelManagementFacade` |
| `Collection getImportedElements(Object pack)` | Lista os elementos importados por um pacote. | `ModelManagementFacade` |
| `Object getImportedElement(Object elementImport)` | Retorna o elemento referenciado por uma importação. | `ModelManagementFacade` |
| `Object getPackage(Object handle)` | Retorna o pacote associado a um ElementImport. | `ModelManagementFacade` |
| `String getAlias(Object handle)` | Consulta o alias de um ElementImport. | `ModelManagementFacade` |

---

## 3. UseCasesFacade

**Casos de uso · 16 métodos**

Concentra o reconhecimento de atores e casos de uso e as consultas às relações include e extend. Também reúne os pontos de extensão e suas propriedades.

| Método | Descrição | Subinterface proposta |
|---|---|---|
| `boolean isAActor(Object handle)` | Reconhece elementos correspondentes a Actor no modelo UML. | `UseCasesFacade` |
| `boolean isAExtend(Object handle)` | Reconhece elementos correspondentes a Extend no modelo UML. | `UseCasesFacade` |
| `boolean isAExtensionPoint(Object handle)` | Reconhece elementos correspondentes a ExtensionPoint no modelo UML. | `UseCasesFacade` |
| `boolean isAInclude(Object handle)` | Reconhece elementos correspondentes a Include no modelo UML. | `UseCasesFacade` |
| `boolean isAUseCase(Object handle)` | Reconhece elementos correspondentes a UseCase no modelo UML. | `UseCasesFacade` |
| `Object getCondition(Object handle)` | Consulta a condição de uma relação Extend. | `UseCasesFacade` |
| `Collection getExtends(Object handle)` | Lista as relações Extend vinculadas ao caso de uso. | `UseCasesFacade` |
| `Collection getExtenders(Object handle)` | Consulta os casos de uso que estendem o caso de uso informado. | `UseCasesFacade` |
| `Object getExtension(Object handle)` | Retorna o caso de uso que atua como extensão em uma relação Extend. | `UseCasesFacade` |
| `Object getExtensionPoint(Object handle, int index)` | Consulta um ponto de extensão pelo índice informado. | `UseCasesFacade` |
| `Collection getExtensionPoints(Object handle)` | Lista os pontos de extensão associados ao elemento. | `UseCasesFacade` |
| `Collection getIncludes(Object handle)` | Lista as relações Include vinculadas ao caso de uso. | `UseCasesFacade` |
| `Collection getIncluders(Object handle)` | Consulta os casos de uso que incluem o caso de uso informado. | `UseCasesFacade` |
| `String getLocation(Object handle)` | Consulta a localização de um ponto de extensão de caso de uso. | `UseCasesFacade` |
| `Object getUseCase(Object handle)` | Retorna o caso de uso associado ao elemento. | `UseCasesFacade` |
| `Object getAddition(Object handle)` | Retorna o caso de uso incluído por uma relação Include. | `UseCasesFacade` |

---

## 4. StateMachinesFacade

**Máquinas de estados · 54 métodos**

Reúne estados, vértices, regiões e transições, além dos eventos, guardas e efeitos ligados às máquinas de estados. Os métodos permitem reconhecer esses elementos e consultar suas conexões.

| Método | Descrição | Subinterface proposta |
|---|---|---|
| `boolean isACallEvent(Object handle)` | Reconhece elementos correspondentes a CallEvent no modelo UML. | `StateMachinesFacade` |
| `boolean isAChangeEvent(Object handle)` | Reconhece elementos correspondentes a ChangeEvent no modelo UML. | `StateMachinesFacade` |
| `boolean isACompositeState(Object handle)` | Reconhece elementos correspondentes a CompositeState no modelo UML. | `StateMachinesFacade` |
| `boolean isAEvent(Object handle)` | Reconhece elementos correspondentes a Event no modelo UML. | `StateMachinesFacade` |
| `boolean isAFinalState(Object handle)` | Reconhece elementos correspondentes a FinalState no modelo UML. | `StateMachinesFacade` |
| `boolean isAGuard(Object handle)` | Reconhece elementos correspondentes a Guard no modelo UML. | `StateMachinesFacade` |
| `boolean isAPseudostate(Object handle)` | Reconhece elementos correspondentes a Pseudostate no modelo UML. | `StateMachinesFacade` |
| `boolean equalsPseudostateKind(Object ps1, Object ps2)` | Compara dois valores de tipo de pseudoestado. | `StateMachinesFacade` |
| `boolean isARegion(Object handle)` | Reconhece elementos correspondentes a Region no modelo UML. | `StateMachinesFacade` |
| `boolean isASignalEvent(Object handle)` | Reconhece elementos correspondentes a SignalEvent no modelo UML. | `StateMachinesFacade` |
| `boolean isASimpleState(Object handle)` | Reconhece elementos correspondentes a SimpleState no modelo UML. | `StateMachinesFacade` |
| `boolean isAStateMachine(Object handle)` | Reconhece elementos correspondentes a StateMachine no modelo UML. | `StateMachinesFacade` |
| `boolean isAStateVertex(Object handle)` | Reconhece elementos correspondentes a StateVertex no modelo UML. | `StateMachinesFacade` |
| `boolean isAVertex(Object handle)` | Reconhece elementos correspondentes a Vertex no modelo UML. | `StateMachinesFacade` |
| `boolean isAState(Object handle)` | Reconhece elementos correspondentes a State no modelo UML. | `StateMachinesFacade` |
| `boolean isAStubState(Object handle)` | Reconhece elementos correspondentes a StubState no modelo UML. | `StateMachinesFacade` |
| `boolean isASubmachineState(Object handle)` | Reconhece elementos correspondentes a SubmachineState no modelo UML. | `StateMachinesFacade` |
| `boolean isASynchState(Object handle)` | Reconhece elementos correspondentes a SynchState no modelo UML. | `StateMachinesFacade` |
| `boolean isATransition(Object handle)` | Reconhece elementos correspondentes a Transition no modelo UML. | `StateMachinesFacade` |
| `boolean isATrigger(Object handle)` | Reconhece elementos correspondentes a Trigger no modelo UML. | `StateMachinesFacade` |
| `boolean isATimeEvent(Object handle)` | Reconhece elementos correspondentes a TimeEvent no modelo UML. | `StateMachinesFacade` |
| `boolean isConcurrent(Object handle)` | Verifica a condição de concorrência do elemento de estado. | `StateMachinesFacade` |
| `boolean isAConcurrentRegion(Object handle)` | Identifica uma região concorrente. | `StateMachinesFacade` |
| `boolean isInternal(Object handle)` | Identifica uma transição interna do estado. | `StateMachinesFacade` |
| `boolean isOrthogonal(Object handle)` | Consulta a condição de ortogonalidade do estado. | `StateMachinesFacade` |
| `boolean isTop(Object handle)` | Verifica se um estado composto é o estado superior. | `StateMachinesFacade` |
| `Collection getBehaviors(Object handle)` | Lista as máquinas de estados que definem o comportamento do elemento. | `StateMachinesFacade` |
| `int getBound(Object handle)` | Consulta o limite de um SynchState. | `StateMachinesFacade` |
| `Object getEffect(Object handle)` | Retorna o efeito associado à transição. | `StateMachinesFacade` |
| `Object getEntry(Object handle)` | Retorna o comportamento de entrada do estado. | `StateMachinesFacade` |
| `Object getExit(Object handle)` | Retorna o comportamento de saída do estado. | `StateMachinesFacade` |
| `Object getGuard(Object handle)` | Retorna a guarda associada à transição. | `StateMachinesFacade` |
| `Collection getIncomings(Object handle)` | Consulta as transições de entrada do elemento ou do estado associado. | `StateMachinesFacade` |
| `Collection getInternalTransitions(Object handle)` | Lista as transições internas do estado. | `StateMachinesFacade` |
| `Collection getDeferrableEvents(Object handle)` | Lista os eventos adiáveis do estado. | `StateMachinesFacade` |
| `Object getDoActivity(Object handle)` | Retorna a atividade executada durante a permanência no estado. | `StateMachinesFacade` |
| `Collection getOccurrences(Object handle)` | Lista os eventos de chamada que referenciam uma operação. | `StateMachinesFacade` |
| `Collection getOutgoings(Object handle)` | Consulta as transições de saída do elemento ou do estado associado. | `StateMachinesFacade` |
| `Object getSource(Object handle)` | Retorna o vértice de origem de uma transição. | `StateMachinesFacade` |
| `Object getStateMachine(Object handle)` | Retorna a máquina de estados associada ao elemento. | `StateMachinesFacade` |
| `Object getState(Object handle)` | Retorna o estado associado ao elemento. | `StateMachinesFacade` |
| `Collection getStates(Object handle)` | Lista os estados relacionados ao elemento. | `StateMachinesFacade` |
| `Collection getSubvertices(Object handle)` | Lista os subvértices de um estado ou região. | `StateMachinesFacade` |
| `Object getSubmachine(Object handle)` | Retorna a submáquina associada ao estado. | `StateMachinesFacade` |
| `Collection getSubmachineStates(Object handle)` | Lista os estados que referenciam a máquina como submáquina. | `StateMachinesFacade` |
| `Object getTop(Object handle)` | Retorna o estado superior da máquina de estados. | `StateMachinesFacade` |
| `Object getTransition(Object handle)` | Retorna a transição associada ao elemento. | `StateMachinesFacade` |
| `Object getTrigger(Object handle)` | Consulta o gatilho associado à transição. Método com indicação de depreciação no contrato original. | `StateMachinesFacade` |
| `List getTriggers(Object handle)` | Lista os gatilhos associados à transição. | `StateMachinesFacade` |
| `Object getTarget(Object handle)` | Retorna o vértice de destino de uma transição. | `StateMachinesFacade` |
| `Collection getTransitions(Object handle)` | Lista transições de máquinas de estados, estados, regiões, vértices ou eventos. Método com indicação de depreciação no contrato original. | `StateMachinesFacade` |
| `String getReferenceState(Object o)` | Consulta a referência de estado de um StubState. | `StateMachinesFacade` |
| `Object getWhen(Object target)` | Consulta a expressão temporal de um TimeEvent. | `StateMachinesFacade` |
| `Object getChangeExpression(Object target)` | Consulta a expressão de mudança de um ChangeEvent. | `StateMachinesFacade` |

---

## 5. ActivityGraphsFacade

**Grafos de atividades · 18 métodos**

Agrupa os elementos específicos dos grafos de atividades, como nós, fluxos de objetos, partições e classificadores em estado. Inclui as consultas ao conteúdo das partições e às atividades associadas.

| Método | Descrição | Subinterface proposta |
|---|---|---|
| `boolean isAActionState(Object handle)` | Reconhece elementos correspondentes a ActionState no modelo UML. | `ActivityGraphsFacade` |
| `boolean isACallState(Object handle)` | Reconhece elementos correspondentes a CallState no modelo UML. | `ActivityGraphsFacade` |
| `boolean isAObjectFlowState(Object handle)` | Reconhece elementos correspondentes a ObjectFlowState no modelo UML. | `ActivityGraphsFacade` |
| `boolean isAObjectNode(Object handle)` | Reconhece elementos correspondentes a ObjectNode no modelo UML. | `ActivityGraphsFacade` |
| `boolean isASubactivityState(Object handle)` | Reconhece elementos correspondentes a SubactivityState no modelo UML. | `ActivityGraphsFacade` |
| `boolean isAActivityEdge(Object handle)` | Reconhece elementos correspondentes a ActivityEdge no modelo UML. | `ActivityGraphsFacade` |
| `boolean isAActivityGraph(Object handle)` | Reconhece elementos correspondentes a ActivityGraph no modelo UML. | `ActivityGraphsFacade` |
| `boolean isAActivityNode(Object handle)` | Reconhece elementos correspondentes a ActivityNode no modelo UML. | `ActivityGraphsFacade` |
| `boolean isAClassifierInState(Object handle)` | Reconhece elementos correspondentes a ClassifierInState no modelo UML. | `ActivityGraphsFacade` |
| `boolean isAPartition(Object handle)` | Reconhece elementos correspondentes a Partition no modelo UML. | `ActivityGraphsFacade` |
| `Collection getClassifiersInState(Object handle)` | Consulta os elementos ClassifierInState relacionados ao elemento. | `ActivityGraphsFacade` |
| `Collection getInStates(Object handle)` | Consulta os estados associados a um ClassifierInState. | `ActivityGraphsFacade` |
| `Collection getContents(Object handle)` | Lista os elementos contidos em uma Partition. | `ActivityGraphsFacade` |
| `Collection getObjectFlowStates(Object handle)` | Lista os estados de fluxo de objetos relacionados ao elemento. | `ActivityGraphsFacade` |
| `Object getActivityGraph(Object handle)` | Retorna o grafo de atividades associado ao elemento. | `ActivityGraphsFacade` |
| `Object getActivity(Object handle)` | Retorna a atividade associada ao elemento. | `ActivityGraphsFacade` |
| `Collection getPartitions(Object container)` | Lista as partições do contêiner informado. | `ActivityGraphsFacade` |
| `boolean isSynch(Object handle)` | Consulta o atributo isSynch de um ObjectFlowState. | `ActivityGraphsFacade` |

---

## 6. CollaborationsFacade

**Colaborações e interações · 34 métodos**

Reúne colaborações, interações, papéis, mensagens e linhas de vida. Inclui a navegação entre mensagens, seus vínculos de ordenação e os elementos representados pela colaboração.

| Método | Descrição | Subinterface proposta |
|---|---|---|
| `boolean isAAssociationRole(Object handle)` | Reconhece elementos correspondentes a AssociationRole no modelo UML. | `CollaborationsFacade` |
| `boolean isAAssociationEndRole(Object handle)` | Reconhece elementos correspondentes a AssociationEndRole no modelo UML. | `CollaborationsFacade` |
| `boolean isAASynchCallMessage(Object handle)` | Reconhece uma mensagem de chamada assíncrona. | `CollaborationsFacade` |
| `boolean isAASynchSignalMessage(Object handle)` | Reconhece uma mensagem de envio assíncrono de sinal. | `CollaborationsFacade` |
| `boolean isADeleteMessage(Object handle)` | Reconhece uma mensagem de destruição. | `CollaborationsFacade` |
| `boolean isACreateMessage(Object handle)` | Reconhece uma mensagem de criação. | `CollaborationsFacade` |
| `boolean isASynchCallMessage(Object handle)` | Reconhece uma mensagem de chamada síncrona. | `CollaborationsFacade` |
| `boolean isAReplyMessage(Object handle)` | Reconhece uma mensagem de resposta. | `CollaborationsFacade` |
| `boolean isAClassifierRole(Object handle)` | Reconhece elementos correspondentes a ClassifierRole no modelo UML. | `CollaborationsFacade` |
| `boolean isACollaboration(Object handle)` | Reconhece elementos correspondentes a Collaboration no modelo UML. | `CollaborationsFacade` |
| `boolean isACollaborationInstanceSet(Object handle)` | Reconhece elementos correspondentes a CollaborationInstanceSet no modelo UML. | `CollaborationsFacade` |
| `boolean isAInteraction(Object handle)` | Reconhece elementos correspondentes a Interaction no modelo UML. | `CollaborationsFacade` |
| `boolean isAInteractionInstanceSet(Object handle)` | Reconhece elementos correspondentes a InteractionInstanceSet no modelo UML. | `CollaborationsFacade` |
| `boolean isALifeline(Object handle)` | Reconhece elementos correspondentes a Lifeline no modelo UML. | `CollaborationsFacade` |
| `boolean isAMessage(Object handle)` | Reconhece elementos correspondentes a Message no modelo UML. | `CollaborationsFacade` |
| `Object getLifeline(Object handle)` | Consulta a linha de vida associada ao elemento. | `CollaborationsFacade` |
| `Collection getAssociationRoles(Object handle)` | Consulta os papéis de associação relacionados ao elemento. | `CollaborationsFacade` |
| `Collection getBases(Object handle)` | Consulta as bases de um ClassifierRole. | `CollaborationsFacade` |
| `Collection getClassifierRoles(Object handle)` | Consulta os papéis de classificador vinculados ao elemento. | `CollaborationsFacade` |
| `Object getInteraction(Object handle)` | Retorna a interação à qual pertence o elemento. | `CollaborationsFacade` |
| `Collection getInteractions(Object handle)` | Lista as interações relacionadas ao elemento. | `CollaborationsFacade` |
| `Collection getMessages(Object handle)` | Lista as mensagens vinculadas ao elemento de interação. | `CollaborationsFacade` |
| `Object getMessageSort(Object handle)` | Consulta a categoria de uma mensagem. | `CollaborationsFacade` |
| `Collection getSuccessors(Object handle)` | Consulta as mensagens sucessoras. | `CollaborationsFacade` |
| `Collection getActivatedMessages(Object handle)` | Lista as mensagens ativadas pela mensagem informada. | `CollaborationsFacade` |
| `Collection getReceivedMessages(Object handle)` | Lista as mensagens recebidas pelo elemento. | `CollaborationsFacade` |
| `Collection getSentMessages(Object handle)` | Lista as mensagens enviadas pelo elemento. | `CollaborationsFacade` |
| `Object getCommunicationConnection(Object handle)` | Retorna a conexão de comunicação de uma mensagem. | `CollaborationsFacade` |
| `Collection getCollaborations(Object handle)` | Consulta as colaborações relacionadas ao elemento. | `CollaborationsFacade` |
| `Collection getConstrainingElements(Object handle)` | Lista os elementos que restringem uma colaboração. | `CollaborationsFacade` |
| `Collection getPredecessors(Object handle)` | Lista as mensagens predecessoras. | `CollaborationsFacade` |
| `Object getRepresentedClassifier(Object handle)` | Retorna o classificador representado pela colaboração. | `CollaborationsFacade` |
| `Object getRepresentedOperation(Object handle)` | Retorna a operação representada pela colaboração. | `CollaborationsFacade` |
| `Object getActivator(Object handle)` | Retorna a mensagem que ativa a mensagem informada. | `CollaborationsFacade` |

---

## 7. CommonBehaviorFacade

**Comportamento comum · 51 métodos**

Agrupa ações, instâncias, ligações, estímulos, sinais e recepções. O foco está no comportamento comum dos objetos e nas informações associadas à execução e à comunicação.

| Método | Descrição | Subinterface proposta |
|---|---|---|
| `boolean isAAction(Object handle)` | Reconhece elementos correspondentes a Action no modelo UML. | `CommonBehaviorFacade` |
| `boolean isAAcceptEventAction(Object handle)` | Reconhece elementos correspondentes a AcceptEventAction no modelo UML. | `CommonBehaviorFacade` |
| `boolean isAActionSequence(Object handle)` | Reconhece elementos correspondentes a ActionSequence no modelo UML. | `CommonBehaviorFacade` |
| `boolean isAAttributeLink(Object handle)` | Reconhece elementos correspondentes a AttributeLink no modelo UML. | `CommonBehaviorFacade` |
| `boolean isAsynchronous(Object handle)` | Verifica se uma ação é assíncrona. | `CommonBehaviorFacade` |
| `boolean isACallAction(Object handle)` | Reconhece elementos correspondentes a CallAction no modelo UML. | `CommonBehaviorFacade` |
| `boolean isAComponentInstance(Object handle)` | Reconhece elementos correspondentes a ComponentInstance no modelo UML. | `CommonBehaviorFacade` |
| `boolean isACreateAction(Object handle)` | Reconhece elementos correspondentes a CreateAction no modelo UML. | `CommonBehaviorFacade` |
| `boolean isADataValue(Object handle)` | Reconhece elementos correspondentes a DataValue no modelo UML. | `CommonBehaviorFacade` |
| `boolean isADestroyAction(Object handle)` | Reconhece elementos correspondentes a DestroyAction no modelo UML. | `CommonBehaviorFacade` |
| `boolean isAException(Object handle)` | Reconhece elementos correspondentes a Exception no modelo UML. | `CommonBehaviorFacade` |
| `boolean isAInstance(Object handle)` | Reconhece elementos correspondentes a Instance no modelo UML. | `CommonBehaviorFacade` |
| `boolean isAInstanceSpecification(Object handle)` | Reconhece elementos correspondentes a InstanceSpecification no modelo UML. | `CommonBehaviorFacade` |
| `boolean isALink(Object handle)` | Reconhece elementos correspondentes a Link no modelo UML. | `CommonBehaviorFacade` |
| `boolean isALinkEnd(Object handle)` | Reconhece elementos correspondentes a LinkEnd no modelo UML. | `CommonBehaviorFacade` |
| `boolean isALinkObject(Object handle)` | Reconhece elementos correspondentes a LinkObject no modelo UML. | `CommonBehaviorFacade` |
| `boolean isANodeInstance(Object handle)` | Reconhece elementos correspondentes a NodeInstance no modelo UML. | `CommonBehaviorFacade` |
| `boolean isAObject(Object handle)` | Reconhece elementos correspondentes a Object no modelo UML. | `CommonBehaviorFacade` |
| `Object getLink(Object handle)` | Retorna a ligação à qual pertence um LinkEnd. | `CommonBehaviorFacade` |
| `boolean isAReception(Object handle)` | Reconhece elementos correspondentes a Reception no modelo UML. | `CommonBehaviorFacade` |
| `boolean isAReturnAction(Object handle)` | Reconhece elementos correspondentes a ReturnAction no modelo UML. | `CommonBehaviorFacade` |
| `boolean isASendAction(Object handle)` | Reconhece elementos correspondentes a SendAction no modelo UML. | `CommonBehaviorFacade` |
| `boolean isASendObjectAction(Object handle)` | Reconhece elementos correspondentes a SendObjectAction no modelo UML. | `CommonBehaviorFacade` |
| `boolean isASendSignalAction(Object handle)` | Reconhece elementos correspondentes a SendSignalAction no modelo UML. | `CommonBehaviorFacade` |
| `boolean isASignal(Object handle)` | Reconhece elementos correspondentes a Signal no modelo UML. | `CommonBehaviorFacade` |
| `boolean isAStimulus(Object handle)` | Reconhece elementos correspondentes a Stimulus no modelo UML. | `CommonBehaviorFacade` |
| `boolean isASubsystemInstance(Object handle)` | Reconhece elementos correspondentes a SubsystemInstance no modelo UML. | `CommonBehaviorFacade` |
| `boolean isATerminateAction(Object handle)` | Reconhece elementos correspondentes a TerminateAction no modelo UML. | `CommonBehaviorFacade` |
| `boolean isAUninterpretedAction(Object handle)` | Reconhece elementos correspondentes a UninterpretedAction no modelo UML. | `CommonBehaviorFacade` |
| `Collection getClassifiers(Object handle)` | Lista os classificadores de uma instância. | `CommonBehaviorFacade` |
| `Object getInstance(Object handle)` | Retorna a instância associada ao elemento. | `CommonBehaviorFacade` |
| `Collection getInstances(Object handle)` | Lista as instâncias relacionadas ao elemento. | `CommonBehaviorFacade` |
| `Object getCommunicationLink(Object handle)` | Retorna a ligação de comunicação de um estímulo. | `CommonBehaviorFacade` |
| `Object getComponentInstance(Object handle)` | Retorna a instância de componente associada ao elemento. | `CommonBehaviorFacade` |
| `Collection getContexts(Object handle)` | Lista os contextos comportamentais de um sinal. | `CommonBehaviorFacade` |
| `Collection getCreateActions(Object handle)` | Consulta as ações de criação relacionadas ao elemento. | `CommonBehaviorFacade` |
| `Object getDispatchAction(Object handle)` | Retorna a ação de despacho de um estímulo. | `CommonBehaviorFacade` |
| `Collection getLinks(Object handle)` | Lista as ligações relacionadas ao elemento. | `CommonBehaviorFacade` |
| `Collection getLinkEnds(Object handle)` | Consulta as extremidades de ligação do elemento. | `CommonBehaviorFacade` |
| `Object getNodeInstance(Object handle)` | Retorna a instância de nó associada ao elemento. | `CommonBehaviorFacade` |
| `Collection getOtherLinkEnds(Object handle)` | Lista as demais extremidades da ligação. | `CommonBehaviorFacade` |
| `Collection getReceptions(Object handle)` | Lista as recepções de um sinal. | `CommonBehaviorFacade` |
| `Object getRecurrence(Object handle)` | Consulta a recorrência de uma ação. | `CommonBehaviorFacade` |
| `Object getScript(Object handle)` | Consulta o script associado à ação. | `CommonBehaviorFacade` |
| `Collection getStimuli(Object handle)` | Lista os estímulos relacionados ao elemento. | `CommonBehaviorFacade` |
| `Collection getReceivedStimuli(Object handle)` | Lista os estímulos recebidos pelo elemento. | `CommonBehaviorFacade` |
| `Collection getSentStimuli(Object handle)` | Lista os estímulos enviados pelo elemento. | `CommonBehaviorFacade` |
| `List getActions(Object handle)` | Lista as ações de uma sequência de ações. | `CommonBehaviorFacade` |
| `Object getActionSequence(Object handle)` | Retorna a sequência à qual pertence uma ação. | `CommonBehaviorFacade` |
| `List getActualArguments(Object handle)` | Lista os argumentos reais de uma ação. | `CommonBehaviorFacade` |
| `boolean isAArgument(Object modelElement)` | Reconhece elementos correspondentes a Argument no modelo UML. | `CommonBehaviorFacade` |

---

## 8. DataTypesFacade

**Tipos de dados auxiliares · 13 métodos**

Reúne expressões, literais, multiplicidades e valores enumerados auxiliares. O reconhecimento dos classificadores UML DataType e Enumeration está em CoreFacade, por fazer parte da estrutura do modelo.

| Método | Descrição | Subinterface proposta |
|---|---|---|
| `boolean isAAggregationKind(Object handle)` | Reconhece elementos correspondentes a AggregationKind no modelo UML. | `DataTypesFacade` |
| `boolean isAExpression(Object handle)` | Reconhece elementos correspondentes a Expression no modelo UML. | `DataTypesFacade` |
| `boolean isALiteralBoolean(Object handle)` | Reconhece elementos correspondentes a LiteralBoolean no modelo UML. | `DataTypesFacade` |
| `boolean isALiteralInteger(Object handle)` | Reconhece elementos correspondentes a LiteralInteger no modelo UML. | `DataTypesFacade` |
| `boolean isALiteralString(Object handle)` | Reconhece elementos correspondentes a LiteralString no modelo UML. | `DataTypesFacade` |
| `boolean isAMultiplicity(Object handle)` | Reconhece elementos correspondentes a Multiplicity no modelo UML. | `DataTypesFacade` |
| `boolean isAMultiplicityRange(Object handle)` | Reconhece elementos correspondentes a MultiplicityRange no modelo UML. | `DataTypesFacade` |
| `boolean isAOpaqueExpression(Object handle)` | Reconhece elementos correspondentes a OpaqueExpression no modelo UML. | `DataTypesFacade` |
| `boolean isAPseudostateKind(Object handle)` | Reconhece elementos correspondentes a PseudostateKind no modelo UML. | `DataTypesFacade` |
| `boolean isAVisibilityKind(Object handle)` | Reconhece elementos correspondentes a VisibilityKind no modelo UML. | `DataTypesFacade` |
| `Object getMultiplicity(Object handle)` | Consulta a multiplicidade associada ao elemento. | `DataTypesFacade` |
| `int getUpper(Object handle)` | Consulta o limite superior da multiplicidade. | `DataTypesFacade` |
| `int getLower(Object handle)` | Consulta o limite inferior da multiplicidade. | `DataTypesFacade` |

---

## 9. ExtensionMechanismsFacade

**Extensão e perfis UML · 30 métodos**

Concentra estereótipos, perfis, extensões e tagged values. Também inclui os predicados que reconhecem convenções baseadas em estereótipos, como singleton, utility e realize.

| Método | Descrição | Subinterface proposta |
|---|---|---|
| `boolean isAAppliedProfileElement(Object handle)` | Reconhece elementos correspondentes a AppliedProfileElement no modelo UML. | `ExtensionMechanismsFacade` |
| `boolean isAExtension(Object handle)` | Reconhece elementos correspondentes a Extension no modelo UML. | `ExtensionMechanismsFacade` |
| `boolean isAProfile(Object handle)` | Reconhece elementos correspondentes a Profile no modelo UML. | `ExtensionMechanismsFacade` |
| `boolean isAProfileApplication(Object handle)` | Reconhece elementos correspondentes a ProfileApplication no modelo UML. | `ExtensionMechanismsFacade` |
| `boolean isAStereotype(Object handle)` | Reconhece elementos correspondentes a Stereotype no modelo UML. | `ExtensionMechanismsFacade` |
| `boolean isATaggedValue(Object handle)` | Reconhece elementos correspondentes a TaggedValue no modelo UML. | `ExtensionMechanismsFacade` |
| `boolean isPrimaryObject(Object handle)` | Identifica objetos criados diretamente pelo usuário ou pelo importador. | `ExtensionMechanismsFacade` |
| `boolean isRealize(Object handle)` | Reconhece a aplicação do estereótipo realize. | `ExtensionMechanismsFacade` |
| `boolean isSingleton(Object handle)` | Reconhece a aplicação do estereótipo singleton. | `ExtensionMechanismsFacade` |
| `boolean isStereotype(Object handle, String stereotypename)` | Verifica se o elemento possui o estereótipo informado. | `ExtensionMechanismsFacade` |
| `boolean isType(Object handle)` | Reconhece a aplicação do estereótipo type. | `ExtensionMechanismsFacade` |
| `boolean isUtility(Object handle)` | Reconhece a aplicação do estereótipo utility. | `ExtensionMechanismsFacade` |
| `Collection<String> getBaseClasses(Object handle)` | Consulta as classes-base declaradas por um estereótipo. | `ExtensionMechanismsFacade` |
| `Collection getExtendedElements(Object handle)` | Lista os elementos do modelo aos quais o estereótipo está aplicado. | `ExtensionMechanismsFacade` |
| `Object getIcon(Object handle)` | Consulta o ícone do estereótipo. | `ExtensionMechanismsFacade` |
| `Collection getStereotypes(Object handle)` | Lista os estereótipos aplicados ao elemento. | `ExtensionMechanismsFacade` |
| `Collection getTypedValues(Object handle)` | Consulta os valores tipados associados ao elemento de extensão. | `ExtensionMechanismsFacade` |
| `String getTag(Object handle)` | Consulta a identificação da tag do elemento. | `ExtensionMechanismsFacade` |
| `Iterator getTaggedValues(Object handle)` | Obtém um iterador dos tagged values do elemento. | `ExtensionMechanismsFacade` |
| `Collection getTaggedValuesCollection(Object handle)` | Lista os tagged values do elemento. | `ExtensionMechanismsFacade` |
| `Object getTaggedValue(Object handle, String name)` | Localiza um tagged value pelo nome informado. | `ExtensionMechanismsFacade` |
| `String getTaggedValueValue(Object handle, String name)` | Consulta o valor textual de um tagged value pelo nome. | `ExtensionMechanismsFacade` |
| `String getTagOfTag(Object handle)` | Consulta a identificação da tag de um tagged value. | `ExtensionMechanismsFacade` |
| `String getValueOfTag(Object handle)` | Consulta o valor textual de um tagged value UML 1. | `ExtensionMechanismsFacade` |
| `Object getValueOfTag(Object handle, Object property)` | Consulta o valor de uma propriedade de tag no elemento proprietário. | `ExtensionMechanismsFacade` |
| `Collection getReferenceValue(Object taggedValue)` | Lista os valores de referência de um tagged value. | `ExtensionMechanismsFacade` |
| `Collection getDataValue(Object taggedValue)` | Lista os valores de dados de um tagged value. | `ExtensionMechanismsFacade` |
| `boolean isATagDefinition(Object handle)` | Reconhece elementos correspondentes a TagDefinition no modelo UML. | `ExtensionMechanismsFacade` |
| `Collection getTagDefinitions(Object handle)` | Lista as definições de tag associadas ao elemento. | `ExtensionMechanismsFacade` |
| `Object getTagDefinition(Object handle)` | Retorna a definição de um tagged value. | `ExtensionMechanismsFacade` |

---

## 10. ModelIntrospectionFacade

**Introspecção do modelo · 8 métodos**

Agrupa as consultas genéricas sobre o próprio modelo, como versão UML, nomes de metatipos, identificação e representação textual. Essas operações apoiam a inspeção dos elementos pela aplicação.

| Método | Descrição | Subinterface proposta |
|---|---|---|
| `String getUmlVersion()` | Informa a versão do metamodelo UML usada pela implementação. | `ModelIntrospectionFacade` |
| `boolean isAUMLElement(Object handle)` | Reconhece elementos correspondentes a UMLElement no modelo UML. | `ModelIntrospectionFacade` |
| `String getUUID(Object element)` | Consulta o identificador único do elemento. | `ModelIntrospectionFacade` |
| `String getUMLClassName(Object handle)` | Consulta o nome da classe UML do elemento. | `ModelIntrospectionFacade` |
| `String getTipString(Object modelElement)` | Obtém o texto de dica associado ao elemento. | `ModelIntrospectionFacade` |
| `String toString(Object modelElement)` | Obtém a representação textual do elemento do modelo. | `ModelIntrospectionFacade` |
| `String[] getMetatypeNames()` | Lista os nomes dos metatipos disponibilizados pela implementação. | `ModelIntrospectionFacade` |
| `boolean isA(String metatypeName, Object element)` | Verifica se o elemento corresponde ao nome de metatipo informado. | `ModelIntrospectionFacade` |

---

## 11. ModelNavigationFacade

**Navegação e consultas transversais · 23 métodos**

Reúne operações cujo contrato aceita elementos de diferentes subdomínios UML. Por exemplo, getKind atende tanto Pseudostate quanto Parameter; a classificação preserva esse alcance compartilhado.

| Método | Descrição | Subinterface proposta |
|---|---|---|
| `Object getKind(Object handle)` | Consulta o tipo de um Pseudostate ou a direção de um Parameter. | `ModelNavigationFacade` |
| `Object getReceiver(Object handle)` | Retorna o receptor de um estímulo ou de uma mensagem. | `ModelNavigationFacade` |
| `Object getBase(Object handle)` | Consulta a base de papéis de colaboração ou de relações Extend e Include. | `ModelNavigationFacade` |
| `Object getBody(Object handle)` | Consulta o corpo de Method, Constraint, Expression ou Comment. | `ModelNavigationFacade` |
| `Collection getConnections(Object handle)` | Consulta as extremidades de uma Association ou de um Link. | `ModelNavigationFacade` |
| `Object getExpression(Object handle)` | Consulta a expressão de Argument, Guard, ChangeEvent ou TimeEvent. | `ModelNavigationFacade` |
| `Object getModelElement(Object handle)` | Consulta o ModelElement ligado a importações, tags ou parâmetros de template. | `ModelNavigationFacade` |
| `Object getModelElementContainer(Object handle)` | Consulta o contêiner ou proprietário de um elemento do modelo. | `ModelNavigationFacade` |
| `List getModelElementContents(Object handle)` | Lista os componentes contidos por composição no elemento. | `ModelNavigationFacade` |
| `List getModelElementAssociated(Object handle)` | Lista os elementos associados por relações do metamodelo MOF. | `ModelNavigationFacade` |
| `Object getContainer(Object handle)` | Consulta o contêiner de StateVertex ou ElementResidence. | `ModelNavigationFacade` |
| `Object getContext(Object handle)` | Consulta o contexto de uma StateMachine ou de uma Interaction. | `ModelNavigationFacade` |
| `Object getOperation(Object handle)` | Retorna a operação de uma CallAction ou de um CallEvent. | `ModelNavigationFacade` |
| `Object getParameter(Object handle, int n)` | Consulta o parâmetro pelo índice de uma BehavioralFeature ou de um Event. | `ModelNavigationFacade` |
| `Collection getParameters(Object handle)` | Consulta parâmetros de características comportamentais, eventos e outros elementos aceitos. | `ModelNavigationFacade` |
| `List getParametersList(Object handle)` | Lista, em ordem, os parâmetros de uma BehavioralFeature ou de um Event. | `ModelNavigationFacade` |
| `Object getSender(Object handle)` | Retorna o emissor de um estímulo ou de uma mensagem. | `ModelNavigationFacade` |
| `Object getSignal(Object handle)` | Retorna o sinal de uma SendAction ou de um SignalEvent. | `ModelNavigationFacade` |
| `Object getType(Object handle)` | Consulta o tipo de atributos, parâmetros, extremidades, tags e outros elementos aceitos. | `ModelNavigationFacade` |
| `String getSpecification(Object handle)` | Consulta o texto da especificação de uma Reception ou Operation. | `ModelNavigationFacade` |
| `Object getAction(Object handle)` | Retorna a ação associada a uma mensagem ou a um argumento. | `ModelNavigationFacade` |
| `Object getOwner(Object handle)` | Consulta o proprietário de características, atributos, tags ou parâmetros de template. | `ModelNavigationFacade` |
| `Object getValue(Object handle)` | Consulta o valor de expressões, instâncias, tagged values e outros elementos aceitos. | `ModelNavigationFacade` |
