# Cobertura inicial — argouml-core-umlpropertypanels

Relatório JaCoCo do módulo antes da escrita dos testes de caracterização (T6, etapa 1).

## Ambiente

| Item | Valor |
|---|---|
| JDK | 11.0.33 |
| Maven | 3.9.11 |
| JaCoCo | 0.8.12 (herdado do `parentpom`) |
| Comando | `mvn verify -pl src/argouml-core-umlpropertypanels` |
| Relatório HTML | `src/argouml-core-umlpropertypanels/target/site/jacoco/index.html` |
| Dados brutos | `docs/baseline/jacoco-umlpropertypanels-antes.csv` |

## Infraestrutura habilitada

| Mudança | Motivo |
|---|---|
| Dependência `junit` (escopo `test`) no `pom.xml` do módulo | O módulo não declarava nenhuma dependência de teste. |
| Diretório `tests/` | Convenção do `parentpom` (`testSourceDirectory = ${project.basedir}/tests`). Não existia no módulo. |
| `TestPropertyPanelsClasspath` | Garante que o surefire execute no módulo e que o agente do JaCoCo grave `target/jacoco.exec`. Sem execução de testes o goal `report` é ignorado e nenhum relatório é gerado. O teste apenas localiza os arquivos `.class` das classes-alvo, sem carregá-las, para não alterar a medição inicial. |

## Totais do módulo

| Contador | Coberto | Total | Cobertura |
|---|---:|---:|---:|
| Instruções | 0 | 24.694 | 0,0% |
| Branches | 0 | 2.439 | 0,0% |
| Linhas | 0 | 6.772 | 0,0% |
| Complexidade | 0 | 2.692 | 0,0% |
| Métodos | 0 | 1.471 | 0,0% |
| Classes | 0 | 312 | 0,0% |

## Por pacote (instruções)

| Pacote | Instruções |
|---|---:|
| `org.argouml.core.propertypanels.ui` | 20.574 |
| `org.argouml.core.propertypanels.model` | 4.070 |
| `org.argouml.core.propertypanels.module` | 50 |

## Classes-alvo da T6 (incluindo classes internas)

| Classe | Classes | Instruções | Branches | Linhas | Métodos |
|---|---:|---:|---:|---:|---:|
| `GetterSetterManagerImpl` | 68 | 3.477 | 168 | 866 | 348 |
| `UMLModelElementListModel` | 2 | 1.097 | 96 | 252 | 46 |

## Meta

≥ 30% de cobertura de instrução no módulo: ao menos 7.409 das 24.694 instruções cobertas.
