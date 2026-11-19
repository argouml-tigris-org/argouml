# Testes de caracterização — argouml-core-umlpropertypanels

T6, etapa 2. Os testes registram o comportamento atual de `GetterSetterManagerImpl` e `UMLModelElementListModel` sem alterar o código de produção. Quando o comportamento parece incorreto, o teste afirma o comportamento observado e o caso é listado abaixo como anomalia.

## Arquivos

| Arquivo | Testes | Alvo |
|---|---:|---|
| `tests/org/argouml/core/propertypanels/ModelTestSupport.java` | — | Inicializa o repositório MDR uma vez por JVM |
| `tests/org/argouml/core/propertypanels/model/TestGetterSetterManagerImpl.java` | 42 | `GetterSetterManagerImpl` |
| `tests/org/argouml/core/propertypanels/ui/TestUMLModelElementListModel.java` | 29 | `UMLModelElementListModel` |

## Cobertura das classes-alvo

| Classe (com classes internas) | Instruções | Branches |
|---|---:|---:|
| `GetterSetterManagerImpl` | 2.479 / 3.476 (71,3%) | 120 / 168 (71,4%) |
| `UMLModelElementListModel` | 1.038 / 1.095 (94,8%) | 89 / 96 (92,7%) |
| Módulo inteiro | 3.573 / 24.730 (14,4%) | 209 / 2.439 (8,6%) |

O restante de `GetterSetterManagerImpl` não coberto são os `AddCommand.execute()`, que abrem um `UMLAddDialog` modal e chamam `ProjectManager.getManager().getCurrentProject()`. Não são testáveis sem interface gráfica nem projeto carregado — é a dependência que a T7 remove.

## Comportamento congelado — GetterSetterManagerImpl

| Propriedade / operação | Comportamento atual |
|---|---|
| Registro | 52 nomes de propriedade registrados; em UML 1.4 `association` usa `AssociationEndGetterSetter` e `ownedOperation` não existe |
| Propriedade desconhecida | `get`, `getOptions`, `create`, `getMetaType`, `getAddCommand`, `getRemoveCommand` e `getAdditionalCommands` retornam `null`; `set` não faz nada |
| `isAbstract`, `isLeaf`, `isRoot`, `isActive`, `isQuery` | Ida e volta de `Boolean` |
| `ownerScope`, `targetScope` | Ambos leem e escrevem o mesmo indicador `isStatic` |
| `navigable`, `isNavigable` | Mesma implementação |
| `visibility` | Opções `public, package, protected, private`; valor desconhecido grava `private` |
| `aggregation` | Opções `aggregate, composite, none`; valor desconhecido grava `none` |
| `kind` | Opções `in, out, inout, return`; parâmetro de retorno criado por `buildOperation2` vem como `return` |
| `concurrency` | Padrão `sequential` |
| `changeability` | Opções `addonly, changeable, frozen`; valor desconhecido grava `frozen` |
| `derived` | Guardado como tagged value `derived` com `"true"`/`"false"`; o mesmo tagged value é reutilizado |
| `body` | `create` devolve uma `ProcedureExpression` com linguagem e corpo |
| Propriedades de lista | `get` sempre `null`, `set` não faz nada |
| `subvertex` | `getAdditionalCommands` devolve 7 comandos de criação de pseudoestado |
| `elementImport`, `residentElement` | `isFullBuildOnly` verdadeiro |
| `RemoveCommand` | `baseClass`, `elementImport`, `deferrableEvent` e `residentElement` removem o item do modelo |

## Anomalias registradas

| # | Onde | Comportamento | Teste |
|---|---|---|---|
| A1 | `ChangeabilityGetterSetter.set` | Com o valor atual `addonly`, escolher `changeable` é ignorado: o setter chama `setReadOnly(false)` e o `UndoCoreHelperDecorator` só aplica a mudança quando `isReadOnly` muda, mas `addonly` já tem `isReadOnly == false` | `testChangeableIsIgnoredWhenCurrentValueIsAddOnly` |
| A2 | `DerivedGetterSetter.set` | Sem uma `TagDefinition` "derived" existente e sem modelo raiz, lança `IllegalArgumentException("namespace: null")` | `testDerivedWithoutTagDefinitionAndRootModelFails` |
| A3 | `RegionGetterSetter.getMetaType` | Registrado também em UML 1.4, mas lança `NotImplementedException("Not a UML 1.4 element")` | `testRegionMetaTypeIsNotAvailableInUml14` |
| A4 | `AssociationEndGetterSetter.getOptions` | A propriedade `association` só aceita classificador; com uma `Association` lança `IllegalArgumentException` | `testAssociationOptionsListClassifierEndsInUml14` |
| A5 | `getMetaType` | `feature` e `ownedElement` devolvem `Operation`; `sender`, `receiver` e `qualifier` devolvem `Attribute` — tipos que não correspondem ao conteúdo das listas | `testListMetaTypesThatDoNotMatchTheirContent` |
| A6 | `isValidElement` de todas as listas | Compara o elemento com as opções do próprio elemento (`getOptions(element).contains(element)`), o que na prática é sempre falso | `testIsValidElementComparesElementWithItsOwnOptions` |
| A7 | `entry`, `exit`, `doActivity`, `guard`, `effect`, `trigger` | Sem valor definido, `getOptions` devolve uma lista com um único `null` em vez de lista vazia | `testSingleValuedListOptionsContainNullWhenUnset` |
| A8 | `FeatureGetterSetter.getOptions` | Pedir apenas `Operation` (sem `Reception`) devolve `Collections.EMPTY_LIST` | `testFeatureOptionsDependOnRequestedTypes` |

## Comportamento congelado — UMLModelElementListModel

| Operação | Comportamento atual |
|---|---|
| `addElement` | Ignora `null` e duplicados |
| `addAll(vazia)` | Não dispara evento |
| `addAll` em lista vazia | Um único `intervalAdded(0, n-1)` |
| `setAllElements` | Substitui todo o conteúdo |
| `contains(Collection)` | Verdadeiro se todos os itens estiverem na lista |
| `setBuildingModel(true)` | Suprime todos os eventos de lista |
| `getChangedElement` | `AssociationChangeEvent` → valor alterado; `AttributeChangeEvent` → fonte; outros → `newValue` |
| `isValidEvent` | Remoção (`newValue == null`, `oldValue != null`) é sempre válida; coleção vazia só é válida se `oldValue` for coleção não vazia |
| `propertyChange` | Processado no EDT via `invokeLater`; adiciona/remove conforme o evento; listas `Ordered` inserem na posição do modelo; `InvalidElementException` é engolida |
| `setTarget` | Registra no event pump, chama `buildModelList` uma vez e dispara `intervalAdded`; chamar duas vezes viola a `assert` |

## Anomalias registradas

| # | Onde | Comportamento | Teste |
|---|---|---|---|
| B1 | `addAll(null)` | `NullPointerException` em `col.size()` — o bug `javabugs:S2259` da baseline (linha 469) | `testAddAllWithNullThrowsNullPointerException` |
| B2 | `addAll` em lista não vazia | O intervalo começa no último índice antigo, não no primeiro novo: lista `[a, b]` + `[c, d]` dispara `added 1-3` em vez de `2-3` | `testAddAllOnFilledListStartsIntervalAtPreviousLastIndex` |
| B3 | `addAll` só com duplicados | Dispara `intervalAdded` mesmo sem nada adicionado | `testAddAllOfExistingElementsStillFiresInterval` |
| B4 | `contains(Collection)` | Coleção vazia é considerada contida, inclusive em lista vazia | `testContainsWithCollections` |
| B5 | `setTarget` com falha em `buildModelList` | Mantém os elementos parciais e dispara evento para eles | `testSetTargetKeepsPartialListWhenBuildFails` |

Quando uma dessas anomalias for corrigida (T2, T7), o teste correspondente deve ser atualizado no mesmo commit, deixando explícita a mudança de comportamento.
