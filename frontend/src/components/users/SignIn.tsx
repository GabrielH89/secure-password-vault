import { useState } from "react";
import axios, { AxiosError } from "axios";
import { Link, useNavigate } from "react-router-dom";
import "../../styles/users/SignIn.css";
import Modal from "./Modal";
import SignUp from "./SignUp";

function SignIn() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [errorMessage, setErrorMessage] = useState("");
    const [isSignUpOpen, setSignUpOpen] = useState(false);
    const navigate = useNavigate();

    const handleLogin = async (e: React.FormEvent) => {
        e.preventDefault();

        if (!email.trim() || !password.trim()) {
            setErrorMessage("Preencha todos os campos.");
            return;
        }

        try {
            const response = await axios.post("http://localhost:8080/auth/login", { email, password });

            const { token, userId } = response.data;
            sessionStorage.setItem("token", token);
            sessionStorage.setItem("userId", userId);
            navigate("/home");
        } catch (error) {
            const axiosError = error as AxiosError;
            if (axiosError.response?.status === 404) {
                setErrorMessage("Email ou senha inválidos.");
            } else {
                setErrorMessage("Erro ao tentar realizar login.");
            }
            setTimeout(() => setErrorMessage(""), 3000);
        }
    };

    return (
        <div className="signInContainer">
            <form className="signInForm" onSubmit={handleLogin}>
                {errorMessage && <div className="error-message">{errorMessage}</div>}
                <h2>Login</h2>
                <div className="formGroup">
                    <label>Email:</label>
                    <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} required />
                </div>
                <div className="formGroup">
                    <label>Senha:</label>
                    <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} required />
                </div>
                <button type="submit">Login</button>
                <p>Não possui uma conta? <Link to="#" onClick={() => setSignUpOpen(true)}>Cadastre-se</Link></p>
            </form>
            <Modal isOpen={isSignUpOpen} onClose={() => setSignUpOpen(false)}>
                <SignUp />
            </Modal>
        </div>
    );
}

export default SignIn;
