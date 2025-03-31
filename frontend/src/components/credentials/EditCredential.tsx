import axios from "axios";
import { useEffect, useState } from "react";
import { FaEye, FaEyeSlash } from "react-icons/fa";

interface Credential {
    id_password: number;
    systemName: string;
    passwordBody: string;
    createAt: string;
    updateAt: string;
    user_id: number;
}

function EditCredential({ credential, onClose }: { credential: Credential; onClose: () => void }) {
    const [systemName, setSystemName] = useState(credential.systemName);
    const [passwordBody, setPasswordBody] = useState(credential.passwordBody);
    const [errorMessage, setErrorMessage] = useState("");
    const [showPassword, setShowPassword] = useState(false);

    useEffect(() => {
        const fetchCredential = async () => {
            try {
                const token = sessionStorage.getItem("token");
                const response = await axios.get(`http://localhost:8080/credentials/${credential.id_password}`, {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });
                setSystemName(response.data.systemName);
                setPasswordBody(response.data.passwordBody);
                console.log(response.data)
            } catch (error) {
                console.log("Erro ao buscar credencial: " + error);
            }
        };
        fetchCredential();
    }, [credential.id_password]);

    return (
        <div>
            <form className="editFormCredential">
                <div className="formGroup">
                    <label>Nome do sistema</label>
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
                    <button type="button" className="toggle-password" onClick={() => setShowPassword(!showPassword)}>
                        {showPassword ? <FaEyeSlash /> : <FaEye />}
                    </button>
                </div>
                {errorMessage && <p className="error">{errorMessage}</p>}
                <button type="submit">Editar</button>
            </form>
        </div>
    );
}

export default EditCredential;
