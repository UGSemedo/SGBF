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
import sgbf.modelo.ModEstante;
import sgbf.util.UtilControloExcessao;

/**
 *
 * @author Look
 */
public class DaoEstante extends DaoCRUD {

    @Override
    public boolean registar(Object objecto_registar, String operacao) {
        ModEstante estanteMod = (ModEstante) objecto_registar;
        try {
            if (this.jaExiste(estanteMod, operacao)) {
                throw new UtilControloExcessao(operacao, "Erro ao verificar dados da Estante", Alert.AlertType.ERROR);
            } else {
                super.query = "INSERT INTO tcc.Estante (designacao, descricacao, linha, coluna, Area_idArea)"
                        + " VALUES (?, ?, ?, ?, ?)";
                super.preparedStatement = super.caminhoDaBaseDados.conectarBaseDeDados().prepareStatement(query);
                super.preparedStatement.setString(1, estanteMod.getDesignacao());
                super.preparedStatement.setString(2, estanteMod.getDescricao());
                super.preparedStatement.setByte(3, estanteMod.getLinha());
                super.preparedStatement.setByte(4, estanteMod.getColuna());
                super.preparedStatement.setInt(5, estanteMod.getAreaMod().getIdArea());
                return !super.preparedStatement.execute();
            }
        } catch (SQLException erro) {
            throw new UtilControloExcessao(operacao, "Erro ao " + operacao + " Estante !\nErro: " + erro.getMessage(), Alert.AlertType.ERROR);
        } finally {
            super.caminhoDaBaseDados.fecharTodasConexoes(preparedStatement, setResultset);
        }
    }

    @Override
    public boolean alterar(Object objecto_alterar, String operacao) {
        ModEstante estanteMod = (ModEstante) objecto_alterar;
        try {
            if (this.jaExiste(estanteMod, operacao)) {
                throw new UtilControloExcessao(operacao, "Erro ao verificar dados da Estante", Alert.AlertType.ERROR);
            } else {
                super.query = "UPDATE tcc.Estante set designacao=?, descricacao=?, linha=?, coluna=?, Area_idArea=?,"
                        + "data_modificacao = default where idEstante=?";
                super.preparedStatement = super.caminhoDaBaseDados.conectarBaseDeDados().prepareStatement(query);
                super.preparedStatement.setString(1, estanteMod.getDesignacao());
                super.preparedStatement.setString(2, estanteMod.getDescricao());
                super.preparedStatement.setByte(3, estanteMod.getLinha());
                super.preparedStatement.setByte(4, estanteMod.getColuna());
                super.preparedStatement.setInt(5, estanteMod.getAreaMod().getIdArea());
                super.preparedStatement.setInt(6, estanteMod.getIdEstante());
                return !super.preparedStatement.execute();
            }
        } catch (SQLException erro) {
            throw new UtilControloExcessao(operacao, "Erro ao " + operacao + " Estante !\nErro: " + erro.getMessage(), Alert.AlertType.ERROR);
        } finally {
            super.caminhoDaBaseDados.fecharTodasConexoes(preparedStatement, setResultset);
        }
    }

    @Override
    public boolean remover(Object objecto_remover, String operacao) {
        ModEstante estanteMod = (ModEstante) objecto_remover;
        try {
            if (this.temDadosRelacionados(estanteMod, operacao)) {
                throw new UtilControloExcessao(operacao, "Esta operação não pode ser executada\n A Estante seleccionada possui registo !", Alert.AlertType.ERROR);
            } else {
                super.query = "delete from tcc.Estante where idEstante=?";
                super.preparedStatement = super.caminhoDaBaseDados.conectarBaseDeDados().prepareStatement(query);
                super.preparedStatement.setInt(1, estanteMod.getIdEstante());
                return !super.preparedStatement.execute();
            }
        } catch (SQLException erro) {
            throw new UtilControloExcessao(operacao, "Erro ao " + operacao + " Estante !\nErro: " + erro.getMessage(), Alert.AlertType.ERROR);
        } finally {
            super.caminhoDaBaseDados.fecharTodasConexoes(preparedStatement, setResultset);
        }
    }

    @Override
    public List<Object> listarTodos(String operacao) {
        List<Object> todosRegistos = new ArrayList<>();
        try {
            super.query = "select * from tcc.Estante order by designacao, data_modificacao asc";
            super.preparedStatement = super.caminhoDaBaseDados.conectarBaseDeDados().prepareStatement(query);
            super.setResultset = super.preparedStatement.executeQuery();
            while (super.setResultset.next()) {
                todosRegistos.add(this.pegarRegistos(super.setResultset, operacao));
            }
            return todosRegistos;
        } catch (SQLException erro) {
            throw new UtilControloExcessao( operacao,"Erro ao " + operacao + " Estante(s) !\nErro: " + erro.getMessage(),Alert.AlertType.ERROR);
        }
    }

    @Override
    public List<Object> pesquisar(Object objecto_pesquisar, String operacao) {
        List<Object> todosRegistosEncontrados = new ArrayList<>();
        ModEstante estanteMod = (ModEstante) objecto_pesquisar;
        try {
            super.query = "select * from tcc.Estante where idEstante=? or "
                    + "designacao like '%" + estanteMod.getDesignacao() + "%'";
            super.preparedStatement = super.caminhoDaBaseDados.conectarBaseDeDados().prepareStatement(query);
            super.preparedStatement.setInt(1, estanteMod.getIdEstante());
            super.setResultset = super.preparedStatement.executeQuery();
            while (super.setResultset.next()) {
                todosRegistosEncontrados.add(this.pegarRegistos(super.setResultset, operacao));
            }
            return todosRegistosEncontrados;
        } catch (SQLException erro) {
            throw new UtilControloExcessao( operacao,"Erro ao " + operacao + " Editora(s) !\nErro: " + erro.getMessage(),Alert.AlertType.WARNING);
        }
    }

    private Object pegarRegistos(ResultSet setResultset, String operacao) throws SQLException {
        ModEstante estanteMod = new ModEstante();
        estanteMod.setIdEstante(setResultset.getInt("idEstante"), operacao);
        estanteMod.setDesignacao(setResultset.getString("designacao"), operacao);
        estanteMod.setDescricao(setResultset.getString("descricacao"), operacao);
        estanteMod.setLinha(setResultset.getByte("linha"), operacao);
        estanteMod.setColuna(setResultset.getByte("coluna"), operacao);
        estanteMod.getAreaMod().setIdArea(setResultset.getInt("Area_idArea"), operacao);
        estanteMod.getUtilControloDaData().setData_registo(setResultset.getTimestamp("data_registo"), operacao);
        estanteMod.getUtilControloDaData().setData_modificacao(setResultset.getTimestamp("data_modificacao"), operacao);
        return estanteMod;
    }

    private boolean temDadosRelacionados(ModEstante estanteMod, String operacao) throws SQLException {
        super.query = "select * from categoriasdaestante where Estante_idEstante=?";
        super.preparedStatement = super.caminhoDaBaseDados.conectarBaseDeDados().prepareStatement(super.query);
        super.preparedStatement.setInt(1, estanteMod.getIdEstante());
        super.setResultset = super.preparedStatement.executeQuery();
        return super.setResultset.next();
    }

    private boolean jaExiste(ModEstante estanteIntroduzida, String operacao) {
        for (Object todosRegistos : this.listarTodos(operacao)) {
            ModEstante estanteRegistado = (ModEstante) todosRegistos;
            estanteRegistado.equals(estanteIntroduzida, operacao);
        }
        return false;
    }

}
