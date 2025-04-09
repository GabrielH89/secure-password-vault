import axios from "axios";
import { useState } from "react";
import Modal from "../users/Modal";
import "../../styles/credentials/DeleteAllCredentials.css";

interface Props {
    isOpen: boolean;
    onClose: () => void;
    onDeleted: () => void;
}

const DeleteAllCredentials = ({isOpen, onClose, onDeleted}: Props) => {
    const [isLoading, setIsLoading] = useState(false);
    const API_URL = import.meta.env.VITE_API_URL;

    const handleDeleteAll = async () => {
        try{
            setIsLoading(true);
            const token = sessionStorage.getItem('token');
            await axios.delete(`${API_URL}/credentials`, {
                headers: {
                    "Authorization": `Bearer ${token}`,
                }
            });
            setIsLoading(false);
            onDeleted();
            onClose();
        }catch(error) {
            setIsLoading(false);
            console.log("Error " + error);
        }
    };

    return(
        <Modal isOpen={isOpen} onClose={onClose}>
        <div className="deleteAllCredentials-modal">
        <h2>Deseja realmente deletar todas as credenciais?</h2>
        <div style={{ display: "flex", justifyContent: "space-between", marginTop: "1rem" }}>
          <button onClick={onClose} className="cancel-btn">Cancelar</button>
          <button onClick={handleDeleteAll} disabled={isLoading} className="confirm-btn">
            {isLoading ? "Deletando..." : "Confirmar"}
          </button>
        </div>
        </div>    
      </Modal>
    )
}

export default DeleteAllCredentials;
