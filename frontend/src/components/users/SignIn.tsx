import { useState } from "react";
import axios, { AxiosError } from "axios";
import { Link, useNavigate } from "react-router-dom";
import "../../styles/users/SignIn.css";
import Modal from "./Modal";
import SignUp from "./SignUp";
import LoadingModal from "../credentials/LoadingModal"; // import do loading

function SignIn() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [errorMessage, setErrorMessage] = useState("");
    const [isSignUpOpen, setSignUpOpen] = useState(false);
    const [isLoading, setIsLoading] = useState(false); // estado do loading
    const [isBlocked, setIsBlocked] = useState(false);
    const [countdown, setCountdown] = useState(0);

    const navigate = useNavigate();
    const API_URL = import.meta.env.VITE_API_URL;

    const startCountdown = (seconds : number) => {
        setIsBlocked(true);
        setCountdown(seconds);

        const interval = setInterval(() => {
            setCountdown((prev) => {
                if(prev <= 1) {
                    clearInterval(interval)
                    setIsBlocked(false);
                    return 0;
                }
                return prev -1;
            });
        }, 1000)
    }

    const handleLogin = async (e: React.FormEvent) => {
        e.preventDefault();

        if(isBlocked) {
            setErrorMessage(`Aguarde ${countdown} segundos antes de tentar novamente`);
        }

        setIsLoading(true); // ativa o loading
        setErrorMessage("");

        if (!email.trim() || !password.trim()) {
            setErrorMessage("Preencha todos os campos.");
            setIsLoading(false); // desativa se tiver erro de preenchimento
            return;
        }

        try {
            const response = await axios.post(`${API_URL}/auth/login`, { email, password });
            const { token, userId } = response.data;
            sessionStorage.setItem("token", token);
            sessionStorage.setItem("userId", userId);
            navigate("/home");
        } catch (error) {
            const axiosError = error as AxiosError;
            if (axiosError.response?.status === 401 || axiosError.response?.status === 404) {
                setErrorMessage("Email ou senha inválidos.");
                setIsLoading(false);
                startCountdown(3);
            } 
            setTimeout(() => setErrorMessage(""), 3000);
        } finally {
            setIsLoading(false); // desativa após a tentativa
        }
    };

    return (
        <div className="signInContainer">
            {isLoading && <LoadingModal />} {/* exibe o loading */}
            
            <form className="signInForm" onSubmit={handleLogin}>
                {errorMessage && <div className="error-message">{errorMessage}</div>}
                {isBlocked && (
                    <div className="countdown-message">
                        Tente novamente em {countdown} segundo{countdown > 1 ? "s" : ""}.
                    </div>
                )}
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
                    <label>Senha:</label>
                    <input
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </div>
                <button type="submit" disabled={isBlocked}>
                    login
                </button>
                <p>
                    Não possui uma conta?{" "}
                    <Link to="#" onClick={() => setSignUpOpen(true)}>Cadastre-se</Link>
                </p>
            </form>

            <Modal isOpen={isSignUpOpen} onClose={() => setSignUpOpen(false)}>
                <SignUp />
            </Modal>
        </div>
    );
}

export default SignIn;
