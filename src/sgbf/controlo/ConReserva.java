/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sgbf.controlo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Alert;
import sgbf.modelo.ModEstante;
import sgbf.modelo.ModFuncionario;
import sgbf.modelo.ModReserva;
import sgbf.modelo.ModUtente;
import sgbf.modelo.ModVisitante;
import sgbf.util.UtilControloDaData;
import sgbf.util.UtilControloExcessao;

/**
 *
 * @author Look
 */
public class ConReserva extends ConCRUD {

    @Override
    public boolean registar(Object objecto_registar, String operacao) {
        ModReserva reservaMod = (ModReserva) objecto_registar;
        try {
            if (this.temPendentes(reservaMod.getUtenteMod(), operacao)) {
                throw new UtilControloExcessao(operacao, "O Utente seleccionado tem pendentes", Alert.AlertType.WARNING);
            } else {
                super.query = "INSERT INTO tcc.reserva (dias_remanescente,data_vencimento, Utente_idUtente, Funcionario_idFuncionario) "
                        + "VALUES (?, ?, ?, ?)";
                super.preparedStatement = super.caminhoDaBaseDados.baseDeDados(operacao).prepareStatement(query);
                super.preparedStatement.setInt(1, reservaMod.getDias_remanescente());
                super.preparedStatement.setTimestamp(2, reservaMod.getDataVencimento(reservaMod.getDias_remanescente(), operacao));
                super.preparedStatement.setInt(3, reservaMod.getUtenteMod().getIdUtente());
                super.preparedStatement.setInt(4, reservaMod.getFuncionarioMod().getIdFuncionario());
                return !super.preparedStatement.execute();
            }
        } catch (SQLException erro) {
            throw new UtilControloExcessao(operacao, "Erro ao " + operacao + " Reserva !\nErro: " + erro.getMessage(), Alert.AlertType.ERROR);
        } finally {
            super.caminhoDaBaseDados.fecharTodasConexoes(preparedStatement, setResultset, operacao);
        }
    }

    private boolean temPendentes(ModUtente utenteMod, String operacao) throws SQLException {
        super.query = "select * from Reserva where Utente_idUtente = ? and estado='Activo'";
        super.preparedStatement = super.caminhoDaBaseDados.baseDeDados(operacao).prepareStatement(query);
        super.preparedStatement.setInt(1, utenteMod.getIdUtente());
        super.setResultset = super.preparedStatement.executeQuery();
        if (super.setResultset.next()) {
            return true;
        } else {
            super.query = "select * from Reserva inner join emprestimo on idReserva = Reserva_idReserva "
                    + "where Utente_idUtente=? and emprestimo.estado='Activo'";
            super.preparedStatement = super.caminhoDaBaseDados.baseDeDados(operacao).prepareStatement(query);
            super.preparedStatement.setInt(1, utenteMod.getIdUtente());
            super.setResultset = super.preparedStatement.executeQuery();
            return setResultset.next();
        }
    }

    public Integer utlimoCodigoRegistado(String operacao) {
        try {
            super.query = "select max(idReserva) from reserva";
            super.preparedStatement = super.caminhoDaBaseDados.baseDeDados(operacao).prepareStatement(query);
            super.setResultset = super.preparedStatement.executeQuery();
            if (super.setResultset.next()) {
                return super.setResultset.getInt("max(idReserva)");
            } else {
                return 1;
            }
        } catch (SQLException ex) {
            throw new UtilControloExcessao(operacao, "Erro ao Listar Código da Reserva!\nErro: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @Override
    public boolean alterar(Object objecto_alterar, String operacao) {
        ModReserva reservaMod = (ModReserva) objecto_alterar;
        throw new UtilControloExcessao(operacao, "Operação não disponível !", Alert.AlertType.ERROR);
    }

    @Override
    public boolean remover(Object objecto_remover, String operacao) {
        ModEstante estanteMod = (ModEstante) objecto_remover;
        throw new UtilControloExcessao(operacao, "Operação não disponível !", Alert.AlertType.ERROR);
    }

    @Override
    public List<Object> listarTodos(String operacao) {
        List<Object> todosRegistos = new ArrayList<>();
        throw new UtilControloExcessao(operacao, "Operação não disponível !", Alert.AlertType.ERROR);
    }

    @Override
    public List<Object> pesquisar(Object objecto_pesquisar, String operacao) {
        List<Object> todosRegistosEncontrados = new ArrayList<>();
        ModVisitante visitanteMod = (ModVisitante)objecto_pesquisar;
        try{
            super.query = "select * from reserva where Utente_idUtente=?";
            super.preparedStatement = super.caminhoDaBaseDados.baseDeDados(operacao).prepareStatement(query);
            super.preparedStatement.setInt(1, visitanteMod.getIdUtente());
            super.setResultset  = super.preparedStatement.executeQuery();
            while(super.setResultset.next()){
                todosRegistosEncontrados.add(this.pegarRegistos(super.setResultset, operacao));
            }
            return todosRegistosEncontrados;
        }catch(SQLException erro){
            throw new UtilControloExcessao(operacao,"Erro ao "+operacao+"  !\nErro: "+erro.getMessage(),Alert.AlertType.ERROR);
        }
    }

    private Object pegarRegistos(ResultSet setResultset, String operacao) throws SQLException {
        ModReserva reservaMod = new ModReserva();
        reservaMod.setFuncionarioMod(new ModFuncionario(), operacao); //Limpar o valor do Funcionário por defeito
        reservaMod.setUtenteMod(new ModVisitante(), operacao); //Faz o funcionário deixar de ser nulo
        reservaMod.setIdReserva(setResultset.getInt("idReserva"), operacao);
        reservaMod.setEstado(setResultset.getString("estado"), operacao);
        reservaMod.getUtilControloDaData().setData_registo(setResultset.getTimestamp("data_reserva"), operacao);
        reservaMod.setDias_remanescente(setResultset.getByte("dias_remanescente"), operacao);
        reservaMod.setData_vencimento(UtilControloDaData.TimestampParaDatatime(setResultset.getTimestamp("data_vencimento")), operacao);
        reservaMod.getUtenteMod().setIdUtente(setResultset.getInt("Utente_idUtente"), operacao);
        reservaMod.getFuncionarioMod().setIdFuncionario(setResultset.getInt("Funcionario_idFuncionario"), operacao);
        return reservaMod;
    }

}
