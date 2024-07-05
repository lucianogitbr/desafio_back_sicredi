## Cenário de Negócio
Todo dia útil, por volta das 6 horas da manhã, um colaborador da retaguarda do Sicredi recebe e organiza as informações de 
contas para enviar ao Banco Central.

Todas agências e cooperativas enviam arquivos Excel à Retaguarda. Hoje, o Sicredi já possuí mais de 4 milhões de contas ativas.

Esse usuário da retaguarda exporta manualmente os dados em um arquivo CSV para ser enviada para a Receita Federal, 
antes das 10:00 da manhã na abertura das agências.

## Solução

Você deve implementar uma solução que leia um arquivo Excel e realize a atualização das contas junto ao Banco Central.

Em suma, a solução deverá ser uma aplicação Spring _standalone_ que receba como argumento o diretório do arquivo CSV contendo as contas.

`Exemplo: java -jar SincronizadorBacen <input-file>`

A solução deve processar cada conta do arquivo, enviando-a ao Banco Central por meio da classe BancoCentralService.

Ao fim do processamento deve-se retornar um arquivo contendo o resultado do envio da atualização. Mesclando o conteúdo original e uma nova coluna chamada `status`.


### Requisitos
- Utilizar o projeto _scaffold_ localizado na pasta backend.
- Utilizar a classe `BancoCentralService` para realizar o "envio" dos dados.
- A implementação principal da lógica deve ser feita no método syncAccounts da classe SincronizacaoService, a fim de garantir o funcionamento do coletor de métricas de execução.
- Pode-se utilizar bibliotecas externas ao ecossistema Spring, desde que sejam devidamente importadas no projeto via Gradle.
- Deve-se utilizar o arquivo DATA.csv como fonte dos dados, pois este será utilizado para validação da solução final.
- A estrutura principal do projeto pode ser modificada, tendo cuidado apenas para não alterar a classe `BancoCentralService`

### O que será avaliado
- Organização geral do código. 
- Aplicação de boas práticas comuns do mercado.
- Criatividade da solução.
- Performance da solução.
- Testes e documentações não são obrigatórios, mas serão considerados na avaliação.

### Instruções gerais

O prazo de envio do teste será até às 23:59:59 do dia 26/05/2024.

O código deve ser enviado para os emails `vinnicius.santos@dbccompany.com.br` e `evelin_prado@sicredi.com.br`. Se hospedado em plataformas de versionamento garantir que o repositório esteja acessível.

Dúvidas gerais podem ser encaminhadas aos emails acima.