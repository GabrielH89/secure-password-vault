import axios, { AxiosError } from "axios";
import { useState } from "react";
import { useNavigate } from "react-router-dom";

function AddCredential() {
    const [systemName, setSystemName] = useState("");
    const [passwordBody, setPasswordBody] = useState("");
    const [errorMessage, setErrorMessage] = useState("");
    const navigate = useNavigate();

    const addCredential = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            const userData = { systemName, passwordBody };

            await axios.post("http://localhost:8080/credentials", userData, {
                headers: {
                    "Content-Type": "application/json",
                },
            });

            setSystemName("");
            setPasswordBody("");
            alert("Credential cadastrada com sucesso");
            navigate("/home");
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
            <form className="addCredentialForm" onSubmit={addCredential}>
                <div className="formGroup">
                    <label>Nome do sistema:</label>
                    <input 
                        type="text" 
                        value={systemName} 
                        onChange={(e) => setSystemName(e.target.value)}
                        maxLength={255} 
                        required 
                    />
                </div>
                <div className="formGroup">
                    <label>Senha</label>
                    <input 
                        type="password" 
                        value={passwordBody} 
                        onChange={(e) => setPasswordBody(e.target.value)} 
                        maxLength={600} 
                        required 
                    />
                </div>
                {errorMessage && <p className="error">{errorMessage}</p>}
                <button type="submit">Cadastrar</button>
            </form>
        </div>
    );
}

export default AddCredential;
