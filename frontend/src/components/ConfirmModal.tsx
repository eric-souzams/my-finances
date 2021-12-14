type ModalProps = {
  header: string;
  message: string;
  handlerConfirm: () => void;
  handlerCancel: () => void;
}

export function ConfirmModal(props: ModalProps) {
  return (
    <div className="modal" id="confirm-modal" style={{display: 'block'}}>
      <div className="modal-dialog" role="document">
        <div className="modal-content">
          <div className="modal-header">
            <h5 className="modal-title">{props.header}</h5>
          </div>
          <div className="modal-body">
            {props.message}
          </div>
          <div className="modal-footer">
            <button 
              type="button" 
              className="btn btn-primary"
              onClick={props.handlerConfirm}
            >
              Confirmar
            </button>
            <button 
              type="button" 
              className="btn btn-secondary" 
              data-bs-dismiss="modal"
              onClick={props.handlerCancel}
            >
              Cancelar
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}