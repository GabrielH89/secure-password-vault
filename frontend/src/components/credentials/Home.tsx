import { useEffect, useRef, useState } from "react";
import axios from "axios";
import '../../styles/credentials/Home.css';
import AddCredential from "./AddCredential";
import Modal from "../users/Modal";
import ConfirmDeleteModal from "./ConfirmDeleteModal";
import EditCredential from "./EditCredential";
import DeleteAllCredentials from "./DeleteAllCredentials";
import { useUserData } from "../../utils/useUserData";
import { Link, useNavigate } from "react-router-dom";
import { FaUserCircle } from "react-icons/fa";
import Pagination from "./Pagination";

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
  const [isProfileMenuOpen, setIsProfileMenuOpen] = useState(false);
  const [isDeleteAllModalOpen, setIsDeleteAllModalOpen] = useState(false);
  const [draggedIndex, setDraggedIndex] = useState<number | null>(null);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  const navigate = useNavigate();

  const profileMenuRef = useRef<HTMLDivElement>(null);
  const { userName, imageUser } = useUserData();
  const API_URL = import.meta.env.VITE_API_URL;

  const handleEdit = (credential: Credential) => {
    setCredentialToEdit(credential);
    setIsEditCredentialOpen(true);
  };

  console.log(userName)
  console.log(imageUser)
  /*useEffect(() => {
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
  }, []);*/

  useEffect(() => {
    const fetchCredentials = async () => {
      try{  
        const token = sessionStorage.getItem("token");
        const response = await axios.get(`${API_URL}/credentials?page=${currentPage}`, {
          headers: {
            'Authorization': `Bearer ${token}`,
          }
        });

        setCredentials(response.data);

        const totalItems = parseInt(response.headers['x-total-count'], 10);
        setTotalPages(Math.ceil(totalItems / 12))
      }catch(error) {
         console.log("Erro ao buscar credenciais: " + error);
      }
    };
    fetchCredentials(); 
  }, [currentPage]);

  const handlePageChange = (page: number) => {
    setCurrentPage(page);
  };

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

  const toggleProfileMenu = () => {
    setIsProfileMenuOpen(prev => !prev);
  };

  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (profileMenuRef.current && !profileMenuRef.current.contains(event.target as Node)) {
        setIsProfileMenuOpen(false);
      }
    };
    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, []);

  const logout = () => {
    sessionStorage.removeItem("token");
    navigate("/");
  };

  // 🔄 Drag-and-drop handlers com envio ao backend
  const handleDragStart = (index: number, e: React.DragEvent<HTMLDivElement>) => {
    setDraggedIndex(index);
    e.currentTarget.classList.add("dragging");
  };

  const handleDragOver = (event: React.DragEvent<HTMLDivElement>) => {
    event.preventDefault(); // Permite o drop
  };

  const handleDrop = async (index: number, e: React.DragEvent<HTMLDivElement>) => {
    if (draggedIndex === null || draggedIndex === index) return;

    const updatedCredentials = [...credentials];
    const [draggedItem] = updatedCredentials.splice(draggedIndex, 1);
    updatedCredentials.splice(index, 0, draggedItem);

    setCredentials(updatedCredentials); // Atualiza localmente
    setDraggedIndex(null);
    e.currentTarget.classList.remove("dragging");

    // 🆕 Envia a nova ordem ao backend
    try {
      const token = sessionStorage.getItem("token");
      const orderedIds = updatedCredentials.map((cred) => cred.id_password);
      await axios.put(`${API_URL}/credentials/reorder`, orderedIds, {
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
      });
    } catch (error) {
      console.error("Erro ao reordenar credenciais:", error);
    }
  };

  const handleDragEnd = (e: React.DragEvent<HTMLDivElement>) => {
    setDraggedIndex(null);
    e.currentTarget.classList.remove("dragging");
  };

  return (
    <div>
      <div className="menu-profile">
        <h2>System credential</h2>
        <div className="profile-icon" onClick={toggleProfileMenu} ref={profileMenuRef}>
        {imageUser ? (
    <img
      src={`${API_URL}${imageUser}`}
      alt="Imagem de perfil"
      className="profile-picture"
      style={{ width: 48, height: 48, borderRadius: "50%", objectFit: "cover" }}
    />
  ) : (
    <FaUserCircle size={48} />
  )}
          {isProfileMenuOpen && (
            <div className="profile-dropdown">{imageUser}
              <ul>
                <li>
                  <Link to="/personal-profile" className="dropdown-button">Informações pessoais</Link>
                </li>
                <li>
                  <button onClick={() => setIsDeleteAllModalOpen(true)}>Deletar todas as credenciais</button>
                </li>
                <li>
                  <button onClick={logout}>Sair</button>
                </li>
              </ul>
            </div>
          )}
        </div>
      </div>

      <h1>Bem-vindo(a), {userName}</h1>
      <div className="addCredentialOpen">
        <button onClick={() => setIsAddCredentialOpen(true)}>Inserir credencial</button>
        <Modal isOpen={isAddCredentialOpen} onClose={() => setIsAddCredentialOpen(false)}>
          <AddCredential
            onClose={() => setIsAddCredentialOpen(false)}
            setCredentials={setCredentials}
          />
        </Modal>
      </div>

      <div className="credentials-container">
        {credentials.map((cred, index) => (
          <div
            key={cred.id_password}
            className="credential-card"
            draggable
            onDragStart={(e) => handleDragStart(index, e)}
            onDragOver={handleDragOver}
            onDrop={(e) => handleDrop(index, e)}
            onDragEnd={handleDragEnd}
          >
            <h3 className="card-content-scroll">{cred.systemName}</h3>
            <p className="card-content-scroll"><strong>Senha:</strong> {cred.passwordBody}</p>
            <p><strong>Criado em:</strong> {new Date(cred.createAt).toLocaleDateString()} às 
            {new Date(cred.createAt).toLocaleTimeString()}</p>
            <p><strong>Atualizado em:</strong> {new Date(cred.updateAt).toLocaleDateString()} às 
            {new Date(cred.updateAt).toLocaleTimeString()}</p>
            <div className="card-actions">
              <button className="edit-button" onClick={() => handleEdit(cred)}>Editar</button>
              <button onClick={() => confirmDelete(cred.id_password)} className="delete-button">Deletar</button>
            </div>
          </div>
        ))}
      </div>

      <Pagination currentPage={currentPage} onPageChange={handlePageChange}  totalPages={totalPages}/>

      {isEditCredentialOpen && credentialToEdit && (
        <Modal isOpen={isEditCredentialOpen} onClose={() => setIsEditCredentialOpen(false)}>
          <EditCredential
            credential={credentialToEdit}
            onClose={() => setIsEditCredentialOpen(false)}
            setCredentials={setCredentials}
          />
        </Modal>
      )}

      <ConfirmDeleteModal
        isOpen={isDeleteModalOpen}
        onClose={() => setIsDeleteModalOpen(false)}
        onConfirm={handleDelete}
      />

      <DeleteAllCredentials
        isOpen={isDeleteAllModalOpen}
        onClose={() => setIsDeleteAllModalOpen(false)}
        onDeleted={() => setCredentials([])}
      />
    </div>
  );
}

export default Home;
