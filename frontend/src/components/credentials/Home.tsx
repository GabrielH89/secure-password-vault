import { useEffect, useState } from "react";
import axios from "axios";
import { Link } from "react-router-dom";
import '../../styles/credentials/Home.css';

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
  const [modalOpen, setModalOpen] = useState(false);

  useEffect(() => {
    const fetchCredentials = async () => {
      try {
        const token = sessionStorage.getItem("token");
        const response = await axios.get("http://localhost:8080/credentials", {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        setCredentials(response.data); // Atualiza o estado com os dados da API
        console.log(response.data)
      } catch (error) {
        console.log("Error " + error);
      }
    };
    fetchCredentials();
  }, []);

  const handleDelete = async (id: number) => {
    try {
      const token = sessionStorage.getItem("token");
      await axios.delete(`http://localhost:8080/credentials/${id}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      console.log("Id " + id);
      setCredentials(credentials.filter(cred => cred.id_password !== id)); // Remove do estado
    } catch (error) {
      console.error("Erro ao deletar credencial:", error);
    }
  };

  const handleUpdate = async (userId: number) => {
    // Lógica para abrir um modal ou tela de edição
    console.log(`Atualizar credencial com ID: ${userId}`);
  };

  return (
    <div>
      <h1>Bem-vindo(a), User</h1>

      <div>
        <button>Add credential</button>
      </div>

      <div className="credentials-container">
        {credentials.map((cred) => (
          <div key={cred.id_password} className="credential-card">
            <h3>{cred.systemName}</h3>
            <p><strong>Senha:</strong> {cred.passwordBody}</p>
            <p><strong>Criado em:</strong> {new Date(cred.createAt).toLocaleDateString()}</p>
            <p><strong>Atualizado em:</strong> {new Date(cred.updateAt).toLocaleDateString()}</p>
            <div className="card-actions">
              <button onClick={() => handleUpdate(cred.id_password)} className="edit-button">
              </button>
              <button onClick={() => handleDelete(cred.id_password)} className="delete-button">Deletar
              </button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

export default Home;
