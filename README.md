## Passo a passo para testar a aplicação com Docker Compose
Após clonar o repositório, os passos para subir o container são:
1. Construir a imagem da aplicação
docker build -t asten-task-app .
3. Subir os containers (banco + app)
docker compose up -d

Este projeto foi desenvolvido utilizando Java 24 e Spring 3.5.4

Mais informações no arquivo documentacao.pdf
