# Secure_password_vault

## Projeto para guardar senhas
![GitHub repo size](https://img.shields.io/github/repo-size/GabrielH89/secure-password-vault)
![GitHub language count](https://img.shields.io/github/languages/count/GabrielH89/secure-password-vault)

![project_image](https://github.com/user-attachments/assets/95881b72-731f-4a8c-921a-5569c95b3ecb)

## Tecnologias usadas no projeto: 
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Javascript](https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black)
![React](https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB)
![Vite](https://img.shields.io/badge/vite-%23646CFF.svg?style=for-the-badge&logo=vite&logoColor=white)
![CSS3](https://img.shields.io/badge/css3-%231572B6.svg?style=for-the-badge&logo=css3&logoColor=white)

## Descrição
Este projeto permite que o usuário crie uma conta e faça login no sistema. Após autenticado, o usuário pode cadastrar credenciais de acesso (senhas) vinculadas a sistemas ou plataformas específicas. 

As credenciais podem ser atualizadas, sendo registrada a data da última modificação. O usuário também pode atualizar seus próprios dados, como foto de perfil, e-mail e senha a qualquer momento.

## ✅ Funcionalidades

- ✅ Cadastro e login de usuários
- ✅ Cadastro de credenciais (nome do sistema + senha)
- ✅ Edição e exclusão de credenciais
- ✅ Registro da data da última atualização
- ✅ Atualização de perfil (e-mail, senha, foto)
- ✅ Armazenamento de imagem local (upload)
- ✅ Frontend moderno com React + Vite


## Requisitos
Ter o java, npm e o mysql instalados na máquina

## Instalação e execução do projeto na máquina local
1. Execute o comando: git clone git@github.com:GabrielH89/secure-password-vault.git

#### No diretório backend (raiz)
1. Importe esse diretório na sua IDE

2. Crie um arquivo .env e insira, nele, as variáveis do arquivo .env.example, que está na raíz do diretório backend. Obs: ainda vou criar os dados para o arquivo .env, então não precisa se preocupar em colocar as credenciais do sistema nesse arquivo

3. Para criar o banco de dados execute o seguinte comando: $mysql -u seu_usuario_mysql -p -e "CREATE DATABASE IF NOT EXISTS nomedobanco" 

4. Na raiz do diretório backend, crie o diretório /uploads, para que possa armazenar as imagens na máquina local.

5. Após isso, rode o aplicação no backend

#### No diretório frontend
1. Dentro do diretório frontend, execute o comando $ npm install.   

2. No arquivo .env, coloque a variável de ambiente que está no .env.example, essa variável tem que ser http://localhost:(aqui você coloca a mesma porta lá do backend). 

3. Após as dependências serem instaladas, através do comando anterior, o projeto está pronto para funcionar em sua própria máquina, com o comando $ npm run dev, que mostrará em qual porta está rodando a aplicação, no lado do cliente, geralmente a localhost:5173.
