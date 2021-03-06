/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sgbf.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Alert;
import sgbf.modelo.ModDevolucao;
import sgbf.modelo.ModEstante;
import sgbf.modelo.ModItemSolicitado;
import sgbf.util.UtilControloExcessao;

/**
 *
 * @author Look
 */
public class DaoDevolucao extends DaoCRUD {

    @Override
    public boolean registar(Object objecto_registar, String operacao) {
        ModDevolucao devolucaoMod = (ModDevolucao) objecto_registar;
        try {
            for (ModItemSolicitado itemSolicitado : devolucaoMod.getEmprestimoMod().getReservaMod().getItensRegistados()) {
                super.query = "INSERT INTO tcc.devolucao (tipo_devolucao, quantidade_devolvida, Funcionario_idFuncionario, "
                        + "ItensSolicitados_Acervos_idAcervos, ItensSolicitados_Reserva_idReserva)"
                        + " VALUES (?, ?, ?, ?, ?)";
                super.preparedStatement = super.caminhoDaBaseDados.conectarBaseDeDados().prepareStatement(query);
                super.preparedStatement.setString(1, devolucaoMod.getTipo_devolucao());
                super.preparedStatement.setInt(2, itemSolicitado.getQuantidade_revervada());
                super.preparedStatement.setInt(3, devolucaoMod.getEmprestimoMod().getFuncionarioMod().getIdFuncionario());
                super.preparedStatement.setInt(4, itemSolicitado.getAcervoMod().getIdAcervo());
                super.preparedStatement.setInt(5, devolucaoMod.getEmprestimoMod().getReservaMod().getIdReserva());
                super.preparedStatement.execute();
            }
            return true;
        } catch (SQLException erro) {
            throw new UtilControloExcessao(operacao, "Erro ao " + operacao + " Devolução !\nErro: " + erro.getMessage(), Alert.AlertType.ERROR);
        } finally {
            super.caminhoDaBaseDados.fecharTodasConexoes(preparedStatement, setResultset);
        }
    }

    @Override
    public boolean alterar(Object objecto_alterar, String operacao) {
        ModDevolucao devolucaoMod = (ModDevolucao) objecto_alterar;
        try {
            super.query = "Update tcc.devolucao tipo_devolucao=?, quantidade_devolvida=?, Funcionario_idFuncionario=?, "
                    + "where ItensSolicitados_Acervos_idAcervos=? and ItensSolicitados_Reserva_idReserva=? and idDevolucao=?";
            super.preparedStatement = super.caminhoDaBaseDados.conectarBaseDeDados().prepareStatement(query);
            super.preparedStatement.setString(1, devolucaoMod.getTipo_devolucao());
            super.preparedStatement.setInt(2, devolucaoMod.getQuantidade_devolvida());
            super.preparedStatement.setInt(3, devolucaoMod.getEmprestimoMod().getFuncionarioMod().getIdFuncionario());
            //super.preparedStatement.setInt(4, devolucaoMod.getSolicitadoItemMod().getFisicoAcervoMod().getIdAcervo());
            //super.preparedStatement.setInt(5, devolucaoMod.getSolicitadoItemMod().getReservaMod().getIdReserva());
            super.preparedStatement.setInt(5, devolucaoMod.getIdDevolucao());
            return !super.preparedStatement.execute();
        } catch (SQLException erro) {
            throw new UtilControloExcessao(operacao, "Erro ao " + operacao + " Devolução !\nErro: " + erro.getMessage(), Alert.AlertType.ERROR);
        } finally {
            super.caminhoDaBaseDados.fecharTodasConexoes(preparedStatement, setResultset);
        }
    }

    @Override
    public boolean remover(Object objecto_remover, String operacao) {
        ModDevolucao devolucaoMod = (ModDevolucao) objecto_remover;
        try {
            super.query = "delete from tcc.devolucao where ItensSolicitados_Acervos_idAcervos=? and ItensSolicitados_Reserva_idReserva=?";
            super.preparedStatement = super.caminhoDaBaseDados.conectarBaseDeDados().prepareStatement(query);
            //super.preparedStatement.setInt(1, devolucaoMod.getSolicitadoItemMod().getFisicoAcervoMod().getIdAcervo());
            //super.preparedStatement.setInt(2, devolucaoMod.getSolicitadoItemMod().getReservaMod().getIdReserva());
            return !super.preparedStatement.execute();
        } catch (SQLException erro) {
            throw new UtilControloExcessao(operacao, "Erro ao " + operacao + " Devolução !\nErro: " + erro.getMessage(), Alert.AlertType.ERROR);
        } finally {
            super.caminhoDaBaseDados.fecharTodasConexoes(preparedStatement, setResultset);
        }
    }

    @Override
    public List<Object> listarTodos(String operacao) {
        List<Object> todosRegistos = new ArrayList<>();
        try {
            super.query = "select * from tcc.devolucao order by data_modificacao asc";
            super.preparedStatement = super.caminhoDaBaseDados.conectarBaseDeDados().prepareStatement(query);
            super.setResultset = super.preparedStatement.executeQuery();
            while (super.setResultset.next()) {
                todosRegistos.add(this.pegarRegistos(super.setResultset, operacao));
            }
            return todosRegistos;
        } catch (SQLException erro) {
            throw new UtilControloExcessao(operacao, "Erro ao " + operacao + " Devolução !\nErro: " + erro.getMessage(), Alert.AlertType.ERROR);
        } finally {
            super.caminhoDaBaseDados.fecharTodasConexoes(preparedStatement, setResultset);
        }
    }

    @Override
    public List<Object> pesquisar(Object objecto_pesquisar, String operacao) {
        List<Object> todosRegistosEncontrados = new ArrayList<>();
        ModEstante estanteMod = (ModEstante) objecto_pesquisar;
        try {
            super.query = "select * from tcc.devolucao where ItensSolicitados_Acervos_idAcervos=? or "
                    + "designacao like '%" + estanteMod.getDesignacao() + "%'";
            super.preparedStatement = super.caminhoDaBaseDados.conectarBaseDeDados().prepareStatement(query);
            /*super.preparedStatement.setInt(1, estanteMod.getIdEstante());
            super.setResultset  = super.preparedStatement.executeQuery();
            while(super.setResultset.next()){
                todosRegistosEncontrados.add(this.pegarRegistos(super.setResultset, operacao));
            }*/
            return todosRegistosEncontrados;
        } catch (SQLException erro) {
            throw new UtilControloExcessao(operacao, "Erro ao " + operacao + " Devolução !\nErro: " + erro.getMessage(), Alert.AlertType.ERROR);
        } finally {
            super.caminhoDaBaseDados.fecharTodasConexoes(preparedStatement, setResultset);
        }
    }

    private Object pegarRegistos(ResultSet setResultset, String operacao) throws SQLException {
        ModEstante estanteMod = new ModEstante();
        /*estanteMod.setIdEstante(setResultset.getInt("idEstante"), operacao);
        estanteMod.setDesignacao(setResultset.getString("designacao"), operacao);
        estanteMod.setDescricao(setResultset.getString("descricacao"), operacao);
        estanteMod.setLinha(setResultset.getByte("linha"), operacao);
        estanteMod.setColuna(setResultset.getByte("coluna"), operacao);
        estanteMod.getAreaMod().setIdArea(setResultset.getInt("Area_idArea"), operacao);
        estanteMod.getUtilControloDaData().setData_registo(setResultset.getTimestamp("data_registo"), operacao);
        estanteMod.getUtilControloDaData().setData_modificacao(setResultset.getTimestamp("data_modificacao"), operacao);*/
        return estanteMod;
    }

}
