package sgbf.modelo;

import javafx.scene.control.Alert;
import sgbf.util.UtilControloDaData;
import sgbf.util.UtilControloExcessao;

/**
 *
 * @author Look
 */
public class ModCategoria {

    private Integer idCategoria;
    private String designacao;
    private ModEstante estanteNova;
    private ModEstante estanteActual;
    private UtilControloDaData utilControloDaData;

    public ModCategoria() {
        this.idCategoria = 0;
        this.designacao = null;
        this.estanteActual = new ModEstante();
        this.estanteNova = new ModEstante();
        this.utilControloDaData = new UtilControloDaData();
    }

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria, String operacao) {
        this.idCategoria = idCategoria;
    }

    public String getDesignacao() {
        return designacao;
    }

    public void setDesignacao(String designacao, String operacao) {
        if (designacao == null) {
            throw new UtilControloExcessao(operacao, "Categoria não definida !", Alert.AlertType.INFORMATION);
        } else {
            if (designacao.isEmpty()) {
                throw new UtilControloExcessao(operacao, "Categoria não definida !", Alert.AlertType.INFORMATION);
            } else {
                this.designacao = designacao;
            }
        }
    }

    @Override
    public String toString() {
        return designacao;
    }

    public ModEstante getEstanteNova() {
        return estanteNova;
    }

    public void setEstanteNova(ModEstante estanteMod, String operacao) {
        if (estanteMod == null) {
            this.estanteNova = new ModEstante();
        } else {
            this.estanteNova = estanteMod;
        }
    }

    public ModEstante getEstanteActual() {
        return estanteActual;
    }

    public void setEstanteModActual(ModCategoria categoriaMod, String operacao) {
        if (categoriaMod.getEstanteActual() == null) {
            this.estanteActual = new ModEstante();
        } else {
            this.estanteActual = categoriaMod.getEstanteNova();
        }
    }

    public UtilControloDaData getUtilControloDaData() {
        return utilControloDaData;
    }

    public void equals(ModCategoria categoriaMod, String operacao) {
        if ((this.estanteNova.getIdEstante() == 0) && (categoriaMod.getEstanteNova().getIdEstante() == 0)) {
            if ((this.idCategoria != categoriaMod.idCategoria) && (this.designacao.equalsIgnoreCase(categoriaMod.designacao))) {
                throw new UtilControloExcessao(operacao, "Já existe uma categoria com esta designação !", Alert.AlertType.WARNING);
            }
        } else {
            if (this.getEstanteNova().getIdEstante() == categoriaMod.getEstanteNova().getIdEstante()) {
                if ((this.idCategoria != categoriaMod.idCategoria) && (this.designacao.equalsIgnoreCase(categoriaMod.designacao))) {
                    throw new UtilControloExcessao(operacao, "Já existe uma categoria com esta designação nesta estante !", Alert.AlertType.WARNING);
                }
            }
        }
    }

}
