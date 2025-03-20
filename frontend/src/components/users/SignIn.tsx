import { useEffect } from "react"
import axios from "axios"

function SignIn() {

  useEffect(() => {
    const token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJhdXRoLWFwaSIsInN1YiI6Implc3NpY2FAZ21haWwuY29tIiwiZXhwIjoxNzQyNDQzODExfQ.b96GeRkOto6qxOjDJQbur3wSxNnom41wrBw3J8pK4_A";
    axios.get("http://localhost:8080/credentials", {
      headers: {
        Authorization: `Bearer ${token}` // Envia o token no cabeçalho
      }
    })
    .then(response => {
      console.log("Dados do backend:", response.data);
    })
    .catch(error => {
      console.error("Erro ao buscar os dados:", error);
    });
  }, []);

  return (
    <div>SignIn</div>
  )
}

export default SignIn