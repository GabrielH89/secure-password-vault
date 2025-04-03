import axios from "axios";
import { useState } from "react";
import { FaEye, FaEyeSlash } from "react-icons/fa";
import "../../styles/credentials/AddCredential.css";
import LoadingModal from "./LoadingModal";

interface Credential {
  id_password: number;
  systemName: string;
  passwordBody: string;
  createAt: string;
  updateAt: string;
  user_id: number;
}

interface AddCredentialProps {
  onClose: () => void;
  setCredentials?: React.Dispatch<React.SetStateAction<Credential[]>>;
}

function AddCredential({ onClose, setCredentials }: AddCredentialProps) {
  const [systemName, setSystemName] = useState("");
  const [passwordBody, setPasswordBody] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");

  const addCredential = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsLoading(true);
    setErrorMessage("");

    try {
      if (!setCredentials) {
        throw new Error("Erro: setCredentials não foi passado ao componente.");
      }

      const userData = { systemName, passwordBody };
      const token = sessionStorage.getItem("token");

      if (!token) {
        throw new Error("Usuário não autenticado.");
      }

      const response = await axios.post("http://localhost:8080/credentials", userData, {
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      });

      const newCredential = response.data;

      setCredentials((prevCredentials) => [...prevCredentials, newCredential]);

      setSystemName("");
      setPasswordBody("");
      setShowPassword(false);
      onClose();
    } catch (error) {
      setErrorMessage("Erro ao cadastrar credencial.");
      console.error("Erro no cadastro:", error);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div>
      {isLoading && <LoadingModal />}
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
          <label>Senha:</label>
          <input
            type={showPassword ? "text" : "password"}
            value={passwordBody}
            onChange={(e) => setPasswordBody(e.target.value)}
            maxLength={500}
            required
          />
          <div className="div-toggle-btn">
          <button type="button" className="toggle-password" onClick={() => setShowPassword(!showPassword)}>
            {showPassword ? <FaEyeSlash /> : <FaEye />}
          </button>
          </div>
        </div>
        {errorMessage && <p className="error">{errorMessage}</p>}
        <div className="div-add-btn">
          <button className="add-btn" type="submit">
            Cadastrar
          </button>
        </div>
      </form>
    </div>
  );
}

export default AddCredential;
