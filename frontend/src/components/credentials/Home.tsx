import { useEffect, useState } from "react";
import axios from "axios";
import '../../styles/credentials/Home.css';
import AddCredential from "./AddCredential";
import Modal from "../users/Modal";
import ConfirmDeleteModal from "./ConfirmDeleteModal";
import EditCredential from "./EditCredential";
import { useUserData } from "../../utils/useUserData";
import { Link } from "react-router-dom";
import { FaUserCircle } from "react-icons/fa";

interface Credential {
  id_password: number;
  systemName: string;
  passwordBody: string;
  createAt: string;
  updateAt: string;
  user_id: number;
}

function Home() {
  const [credentials, setCredentials] = useState<Credential[]>([]);
  const [isAddCredentialOpen, setIsAddCredentialOpen] = useState(false);
  const [isEditCredentialOpen, setIsEditCredentialOpen] = useState(false);
  const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);
  const [credentialToDelete, setCredentialToDelete] = useState<number | null>(null);
  const [credentialToEdit, setCredentialToEdit] = useState<Credential | null>(null);
  const {userName} = useUserData();
  const API_URL = import.meta.env.VITE_API_URL;

  const handleEdit = (credential: Credential) => {
    setCredentialToEdit(credential);
    setIsEditCredentialOpen(true);
  };

  useEffect(() => {
    const fetchCredentials = async () => {
      try {
        const token = sessionStorage.getItem("token");
        const response = await axios.get(`${API_URL}/credentials`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        setCredentials(response.data);
      } catch (error) {
        console.log("Erro ao buscar credenciais: " + error);
      }
    };
    fetchCredentials();
  }, []);

  const confirmDelete = (id: number) => {
    setCredentialToDelete(id);
    setIsDeleteModalOpen(true);
  };

  const handleDelete = async () => {
    if (!credentialToDelete) return;
    try {
      const token = sessionStorage.getItem("token");
      await axios.delete(`${API_URL}/credentials/${credentialToDelete}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setCredentials(credentials.filter(cred => cred.id_password !== credentialToDelete));
      setIsDeleteModalOpen(false);
    } catch (error) {
      console.error("Erro ao deletar credencial:", error);
    }
  };

  return (
    <div>
      <div className="menu-profile">
        <h2>System credential</h2>
        <Link to="/personal-profile">
          <span>
           
              <FaUserCircle></FaUserCircle>
            
          </span>
        </Link>
      </div>

      <h1>Bem-vindo(a), {userName}</h1>
      <div className="addCredentialOpen">
        <button onClick={() => setIsAddCredentialOpen(true)}>Inserir senha</button>
        <Modal isOpen={isAddCredentialOpen} onClose={() => setIsAddCredentialOpen(false)}>
        {}
        <AddCredential 
          onClose={() => setIsAddCredentialOpen(false)} 
          setCredentials={setCredentials} 
        />
      </Modal>

      </div>

      <div className="credentials-container">
        {credentials.map((cred) => (
          <div key={cred.id_password} className="credential-card">
            <h3>{cred.systemName}</h3>
            <p><strong>Senha:</strong> {cred.passwordBody}</p>
            <p><strong>Criado em:</strong> {new Date(cred.createAt).toLocaleDateString()} às {new Date(cred.createAt).toLocaleTimeString()}</p>
            <p><strong>Atualizado em:</strong> {new Date(cred.updateAt).toLocaleDateString()} às {new Date(cred.updateAt).toLocaleTimeString()}</p>
            <div className="card-actions">
              <button className="edit-button" onClick={() => handleEdit(cred)}>Editar</button>
              <button onClick={() => confirmDelete(cred.id_password)} className="delete-button">Deletar</button>
            </div>
          </div>
        ))}
      </div>

      {isEditCredentialOpen && credentialToEdit && (
        <Modal isOpen={isEditCredentialOpen} onClose={() => setIsEditCredentialOpen(false)}>
          <EditCredential credential={credentialToEdit} onClose={() => setIsEditCredentialOpen(false)} setCredentials={setCredentials} />
        </Modal>
      )}

      <ConfirmDeleteModal
        isOpen={isDeleteModalOpen}
        onClose={() => setIsDeleteModalOpen(false)}
        onConfirm={handleDelete}
      />
    </div>
  );
}

export default Home;
