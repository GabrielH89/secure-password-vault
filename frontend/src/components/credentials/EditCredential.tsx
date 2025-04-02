import { useState } from "react";
import LoadingModal from "./LoadingModal"; // Modal de carregamento
import axios from "axios";
import { FaEye, FaEyeSlash } from "react-icons/fa";
import '../../styles/credentials/EditCredential.css';

interface Credential {
  id_password: number;
  systemName: string;
  passwordBody: string;
  createAt: string;
  updateAt: string;
  user_id: number;
}

function EditCredential({ credential, onClose, setCredentials }: { credential: Credential; onClose: () => void; setCredentials: React.Dispatch<React.SetStateAction<Credential[]>> }) {
  const [systemName, setSystemName] = useState(credential.systemName);
  const [passwordBody, setPasswordBody] = useState(credential.passwordBody);
  const [showPassword, setShowPassword] = useState(false);
  const [isLoading, setIsLoading] = useState(false); // Estado de carregamento

  const editCredential = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsLoading(true); // Inicia o carregamento
    try {
      const userData = { systemName, passwordBody };
      const token = sessionStorage.getItem("token");
      await axios.put(`http://localhost:8080/credentials/${credential.id_password}`, userData, {
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Bearer ${token}`,
        },
      });

      // Atualiza a credencial diretamente na lista de credenciais de 'Home'
      setCredentials((prevCredentials) =>
        prevCredentials.map((cred) =>
          cred.id_password === credential.id_password
            ? { ...cred, systemName, passwordBody, updateAt: new Date().toISOString() } // Atualiza a credencial editada
            : cred
        )
      );

      setSystemName("");
      setPasswordBody("");
      setShowPassword(false);
      onClose();
    } catch (error) {
      console.log("Error " + error);
    } finally {
      setIsLoading(false); // Finaliza o carregamento
    }
  };

  return (
    <div>
      {isLoading && <LoadingModal />} {/* Exibe o carregamento */}
      <form className="editFormCredential" onSubmit={editCredential}>
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
        <div className="div-edit-btn">
        <button className="edit-btn" type="submit">Editar</button>
        </div>
        
      </form>
    </div>
  );
}

export default EditCredential;
