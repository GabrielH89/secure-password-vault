import { useState } from "react"
import axios, { AxiosError } from "axios"
import { Link, useNavigate } from "react-router-dom";
import '../../styles/users/SignIn.css';

function SignIn() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const navigate = useNavigate();

  const handleLogin = async (e: {preventDefault: () => void;}) => {
    try{    
        e.preventDefault();
        if(email.trim().length > 0 || password.trim().length > 0) {
            const response = await axios.post("http://localhost:8080/auth/login", {
                email: email,
                password: password
            });
            console.log("Response: " + response.data);
            const {token, id} = response.data; // Supondo que o token esteja na resposta como 'token'
            sessionStorage.setItem('token', token);
            sessionStorage.setItem('id', id);
            navigate("/hello");
            
        }else{
            alert("Preencha o email e senha");
        }
    }catch(error) {
        if ((error as AxiosError).response && (error as AxiosError).response?.status === 404) {
            setErrorMessage("Email ou senha inválidos");
            setTimeout(() => {
                setErrorMessage(""); 
            }, 3000);
        } else {
            console.log("Error: " + error);
        }
    }
}

return (
  <div className="signInContainer">
  <form className="signInForm" onSubmit={handleLogin}>
  {errorMessage && 
      <div className="error-message" style={{ backgroundColor: '#B22222', color: 'white', 
      padding: '10px', marginBottom: '10px', borderRadius: '5px', textAlign: 'center', fontSize: '1.1rem' }}>
  {errorMessage}</div>}
      <h2>Login</h2>
      <div className="formGroup">
      <label>Email:</label>
      <input
          type="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
      />
      </div>
      <div className="formGroup">
      <label>Password:</label>
      <input
          type="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
      />
      </div>
      <div className='login-btn'>
          <button type="submit" onClick={handleLogin}>Login</button>
      </div>
      <p>Não possui uma conta? <Link to={"/"}>Cadastre-se</Link></p>
  </form>
  </div>
)

  /*useEffect(() => {
    const token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJhdXRoLWFwaSIsInN1YiI6Implc3NpY2FAZ21haWwuY29tIiwiZXhwIjoxNzQyNTA5NjEzfQ.qjpAYYQFXrDdGZS7ZGl9Z6ReBAbDPe5prMtNzx5XmLI";
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
  )*/
}

export default SignIn