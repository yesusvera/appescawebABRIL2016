package br.org.unesco.appesca.service;

import br.org.unesco.appesca.model.FormFiltroPesquisa;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.org.unesco.appesca.model.exp.RowExportCVS;
import br.org.unesco.appesca.model.exp.TypeVariables;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author yesus CRIEI PARA EXTRAIR O TEMPLATE DO CVS.
 */
public class TemplateCVS {

    private List<RowExportCVS> listRowCVS = new ArrayList<>();

    public void execute(File file) {
        try {
            BufferedReader buffReader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = buffReader.readLine()) != null) {
                addRow(line);
            }
            buffReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addRow(String strRow) {
        RowExportCVS rowExportCVS = parseRow(strRow);

        if (rowExportCVS != null) {
            if (rowExportCVS.getCodigoExportacao() != null) {
                listRowCVS.add(rowExportCVS);
            }
        }
    }

    private RowExportCVS parseRow(String strRow) {
        if (strRow == null) {
            return null;
        }

        String[] arrStrRow = strRow.split(";");
//
//		if (arrStrRow == null || arrStrRow.length < 5) {
//			return null;
//		}

        RowExportCVS row = new RowExportCVS();

        try {
            row.setBloco(arrStrRow[0]);
            row.setQuestao(arrStrRow[1]);
            row.setTipoVariavel(arrStrRow[2]);
            row.setCodigoExportacao(arrStrRow[3]);
            row.setCod1AppescaAndroid(arrStrRow[4]);
            row.setCod2AppescaAndroid(arrStrRow[5]);
        } catch (ArrayIndexOutOfBoundsException e) {

        }

        return row;
    }

    public String montaCabecalho(String conteudoCSV, FormFiltroPesquisa filtroFormulario, int qtdeRegistros) {

        conteudoCSV += "FILTROS FORMULÁRIO" + ";\n";
        conteudoCSV += linhaInformativoFiltro("Situação:", filtroFormulario.getSituacao(), true);
        conteudoCSV += linhaInformativoFiltro("Nome Pesquisador:", filtroFormulario.getPesquisador());
        conteudoCSV += linhaInformativoFiltro("Código Registro:", filtroFormulario.getCodigoRegistro());
        conteudoCSV += linhaInformativoFiltro("Data Início:", filtroFormulario.getDataInicio());
        conteudoCSV += linhaInformativoFiltro("Data Fim:", filtroFormulario.getDataFim());
        conteudoCSV += "\n\n";

        conteudoCSV += "FILTROS ENTREVISTADO" + ";\n";
        conteudoCSV += linhaInformativoFiltro("UF:", filtroFormulario.getUf(), true);
        conteudoCSV += linhaInformativoFiltro("Município:", filtroFormulario.getMunicipio());
        conteudoCSV += linhaInformativoFiltro("Comunidade:", filtroFormulario.getComunidade());
        conteudoCSV += linhaInformativoFiltro("Idade mínima:", filtroFormulario.getIdadeInicial());
        conteudoCSV += linhaInformativoFiltro("Idade máxima:", filtroFormulario.getIdadeFim());
        conteudoCSV += linhaInformativoFiltro("Sexo:", filtroFormulario.getSexo(), true);
        conteudoCSV += linhaInformativoFiltro("Pescador:", filtroFormulario.isPescadorB2Q1Ra() ? "Sim" : "Não");
        conteudoCSV += linhaInformativoFiltro("Pescador Camarão/Caranguejo:", filtroFormulario.isPescadorCamCarB2Rb() ? "Sim" : "Não");

        conteudoCSV += "Total de registros encontrados:;" + qtdeRegistros + ";\n;;\n;;\n";

        conteudoCSV += "Codigo;Pesquisador;Recurso;Estado;Municipio;Comunidade;UC;lat;long;";
        for (RowExportCVS row : getListRowCVS()) {
            if (codigoExportacaoValido(row.getCodigoExportacao())) {
                conteudoCSV += row.getCodigoExportacao() + ";";
            }
        }
        conteudoCSV += "\n";
        return conteudoCSV;
    }

     public String linhaInformativoFiltro(String label, Object valorParam){
         return linhaInformativoFiltro(label, valorParam, false);
     } 
    
    public String linhaInformativoFiltro(String label, Object valorParam, boolean todosPadrao) {
        String valor = "-";
        if(todosPadrao){
            valor = "TODOS";
        }

        if (valorParam != null) {
            if (valorParam instanceof String) {
                if (!((String) valorParam).isEmpty()) {
                    valor = (String) valorParam;
                }
            } else if (valorParam instanceof Date) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                valor = sdf.format((Date) valorParam);
            }
        }

        String linhaFiltro = label + ";" + valor + ";\n";
        return linhaFiltro;
    }

    public static boolean codigoExportacaoValido(String codigoExportacao) {
        return codigoExportacao != null
                && !codigoExportacao.isEmpty()
                && codigoExportacao.toLowerCase().startsWith("var");
    }

    public String priorizaRespostaComDoisCodigos(String linhaMontada, String respCod2, String respCod1) {
        if (respCod2 != null && !respCod2.trim().isEmpty()) {
            linhaMontada += "\"" + respCod2 + "\"";
        } else {
            linhaMontada += "\"" + respCod1 + "\"";
        }
        return linhaMontada;
    }

    public List<RowExportCVS> getListRowCVS() {
        return listRowCVS;
    }

    public void setListRowCVS(List<RowExportCVS> listRowCVS) {
        this.listRowCVS = listRowCVS;
    }

    /**
     * Validations
     *
     * @param row
     * @return
     */
    private static boolean rowIsType(RowExportCVS row, String typeVariable) {
        if (row != null && row.getTipoVariavel() != null
                && row.getTipoVariavel().trim().equalsIgnoreCase(typeVariable)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean rowIsNumber(RowExportCVS row) {
        return rowIsType(row, TypeVariables.NUMBER) || rowIsType(row, TypeVariables.TEXT_NUMBER);
    }

    public static boolean rowIsText(RowExportCVS row) {
        return rowIsType(row, TypeVariables.TEXT);
    }

    public static boolean rowIsMultiple(RowExportCVS row) {
        return rowIsType(row, TypeVariables.MULTIPLE);
    }

    public static boolean rowIsUnique(RowExportCVS row) {
        return rowIsType(row, TypeVariables.UNIQUE);
    }

    public static boolean rowIsEmpty(RowExportCVS row) {
        return rowIsType(row, TypeVariables.EMPTY);
    }

    public static boolean rowIsDataAplicacao(RowExportCVS row) {
        return rowIsType(row, TypeVariables.DATA_APLICACAO);
    }

    public static boolean rowIsNomePesquisador(RowExportCVS row) {
        return rowIsType(row, TypeVariables.NOME_PESQUISADOR);
    }

    public static boolean temDoisCodigos(RowExportCVS row) {
        return row.getCod1AppescaAndroid() != null && !row.getCod1AppescaAndroid().trim().isEmpty()
                && row.getCod2AppescaAndroid() != null && !row.getCod2AppescaAndroid().trim().isEmpty();
    }
}
