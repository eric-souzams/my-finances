import currencyFormater from 'currency-formatter';

type ReleaseProps = {
  id: number;
  descricao: string;
  valor: number;
  tipo: string;
  mes: number;
  status: string;
}

type TableProps = {
  releases: ReleaseProps[];
  editAction: (id: number) => void;
  deleteAction: (release: ReleaseProps) => void;
}

export function ReleaseTable(props: TableProps) {

  const rows = props.releases.map(release => {
    return (
      <tr key={release.id}>
        <td>{release.descricao}</td>
        <td>{currencyFormater.format(release.valor, { locale: 'pt-BR' })}</td>
        <td>{release.tipo}</td>
        <td>{release.mes}</td>
        <td>{release.status}</td>
        <td style={{ padding: '5px 0 5px 0' }}>
          <button 
            type="button" 
            className="btn btn-info"
            onClick={() => props.editAction(release.id)}
          >
            Editar
            <i className="pi pi-pencil"></i>
          </button>
          <button 
            type="button" 
            className="btn btn-warning"
            onClick={() => props.deleteAction(release)}
          >
            Deletar
            <i className="pi pi-trash"></i>
          </button>
        </td>
      </tr>
    );
  })

  return (
    <table className="table table-hover table-striped">
      <thead>
        <tr>
          <th scope="col">Descrição</th>
          <th scope="col">Valor</th>
          <th scope="col">Tipo</th>
          <th scope="col">Mês</th>
          <th scope="col">Situação</th>
          <th scope="col">Ações</th>
        </tr>
      </thead>
      <tbody>
        {rows}
      </tbody>
    </table>
  );
}