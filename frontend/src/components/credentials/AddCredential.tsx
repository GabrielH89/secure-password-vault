import axios, { AxiosError } from "axios";
import { useState } from "react";
import { FaEye, FaEyeSlash } from "react-icons/fa";
import "../../styles/credentials/AddCredential.css";

function AddCredential({ onClose }: { onClose: () => void }) {
    const [systemName, setSystemName] = useState("");
    const [passwordBody, setPasswordBody] = useState("");
    const [errorMessage, setErrorMessage] = useState("");
    const [showPassword, setShowPassword] = useState(false);

    const addCredential = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            const userData = { systemName, passwordBody };
            const token = sessionStorage.getItem('token');
            await axios.post("http://localhost:8080/credentials", userData, {
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`,
                },
            });

            setSystemName("");
            setPasswordBody("");
            alert("Credential cadastrada com sucesso");
            setShowPassword(false);
            onClose();
        } catch (error) {
            const axiosError = error as AxiosError;
            if (axiosError.response?.status === 400) {
                setErrorMessage("Usuário já existente, tente se cadastrar com outro.");
            } else {
                setErrorMessage("Erro ao processar o cadastro. Tente novamente mais tarde.");
            }
            console.error("Erro no cadastro:", axiosError);
        }
    };

    return (
        <div>
             <button type="button" onClick={onClose} className="close-button">
                X
            </button>
            <form className="addCredentialForm" onSubmit={addCredential}>
                <div className="formGroup">
                    <label>Nome do sistema:</label>
                    <input 
                        type="text" 
                        value={systemName} 
                        onChange={(e) => setSystemName(e.target.value)}
                        maxLength={200} 
                        required 
                    />
                </div>
                <div className="formGroup">
                    <label>Senha</label>
                    <input 
                        type={showPassword ? "text" : "password"} 
                        value={passwordBody} 
                        onChange={(e) => setPasswordBody(e.target.value)} 
                        maxLength={500} 
                        required 
                    />
                </div>
                <button
                    type="button"
                    className="toggle-password"
                    onClick={() => setShowPassword(!showPassword)}
                >    
                {showPassword ? <FaEyeSlash/> : <FaEye/>} 
                </button>
                {errorMessage && <p className="error">{errorMessage}</p>}
                <button type="submit">Cadastrar</button>
            </form>
        </div>
    );
}

export default AddCredential;
