import "../../styles/credentials/Pagination.css";

interface PaginationProps {
  currentPage: number;
  onPageChange: (newPage: number) => void;
  totalPages: number;
}

function Pagination({ currentPage, onPageChange, totalPages }: PaginationProps) {
  // Determina as páginas que serão mostradas
  const pagesToShow = [];
  for (let i = 0; i < 5 && i + currentPage < totalPages; i++) {
    pagesToShow.push(i + currentPage);
  }

  return (
    <div className="pagination">
      <button
        disabled={currentPage === 0}
        onClick={() => onPageChange(currentPage - 1)}
      >
        1
      </button>
      
      {pagesToShow.map((page) => (
        <button
          key={page}
          className={currentPage === page ? "active" : ""}
          onClick={() => onPageChange(page)}
        >
          {page + 1}
        </button>
      ))}
      
      {totalPages > 5 && currentPage < totalPages - 5 && (
        <button onClick={() => onPageChange(currentPage + 5)}>{`>>`}</button>
      )}

      <button
        disabled={currentPage === totalPages - 1}
        onClick={() => onPageChange(currentPage + 1)}
      >
        2
      </button>
    </div>
  );
}

export default Pagination;
