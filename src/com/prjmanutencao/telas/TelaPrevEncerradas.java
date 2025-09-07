/*
 * The MIT License
 *
 * Copyright 2025 fabio.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.prjmanutencao.telas;

import com.prjmanutencao.dal.ModuloConexao;
import java.sql.*;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;
import static com.prjmanutencao.telas.TelaLogin.id;
import static com.prjmanutencao.telas.TelaPrevEnc3.id12;
import static com.prjmanutencao.telas.TelaPrevEnc3.id11;
import static com.prjmanutencao.telas.TelaPrevEnc3.id10;
import static com.prjmanutencao.telas.TelaPrevEnc2.id9;
import static com.prjmanutencao.telas.TelaPrevEnc2.id8;
import static com.prjmanutencao.telas.TelaPrevEnc2.id7;
import static com.prjmanutencao.telas.TelaPrevEnc1.id6;
import static com.prjmanutencao.telas.TelaPrevEnc1.id5;
import static com.prjmanutencao.telas.TelaPrevEnc1.id4;
import static com.prjmanutencao.telas.TelaPrevEnc.id3;
import static com.prjmanutencao.telas.TelaPrevEnc.id2;
import static com.prjmanutencao.telas.TelaPrevEnc.id1;
import java.awt.Color;
import java.awt.HeadlessException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

/**
 * Mostra as listas de preventivas encerradas e irregulares, dando a opção de
 * verificar as mesmas.
 *
 * @author Fábio S. Oliveira
 * @version 1.1
 */
public class TelaPrevEncerradas extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public static String nome = null;
    String si = null;
    String set = null;
    String data = null;
    String setor = null;
    String tempo = null;
    String tempo1 = null;
    String tempo2 = null;
    String t = null;
    String prev = null;

    /**
     * Mostra as listas de preventivas encerradas e irregulares.
     *
     * @author Fábio S. Oliveira
     * @version 1.1
     */
    public TelaPrevEncerradas() {
        initComponents();
        conexao = ModuloConexao.conector();
        t = "Encerrada";
        tempo = "60";
        tempo1 = "60";
        tempo2 = "60";

    }

    /**
     * Método `tabelaFormat`. Este método configura a aparência da tabela
     * `tblPreEle`, ajustando a largura das colunas e centralizando o conteúdo e
     * os cabeçalhos. Ele melhora a legibilidade e a organização visual da
     * tabela na interface gráfica.
     *
     * Funcionalidades: - **Definição de Largura das Colunas**: - Define
     * larguras específicas para as cinco primeiras colunas da tabela: - Coluna
     * 0: 90 pixels - Coluna 1: 210 pixels - Coluna 2: 200 pixels - Coluna 3:
     * 220 pixels - Coluna 4: 90 pixels
     *
     * - **Centralização do Cabeçalho**: - Obtém o cabeçalho da tabela
     * (`JTableHeader`) e centraliza seu conteúdo horizontalmente.
     *
     * - **Desabilita Redimensionamento Automático**: - Define o modo de
     * redimensionamento da tabela como `AUTO_RESIZE_OFF`, permitindo controle
     * manual das larguras.
     *
     * - **Centralização do Conteúdo das Células**: - Cria um
     * `DefaultTableCellRenderer` com alinhamento central. - Aplica esse
     * renderizador a todas as colunas da tabela.
     *
     * - **Tratamento de Erros**: - Envolve a configuração em um bloco
     * `try-catch` para capturar e exibir possíveis exceções.
     *
     * Fluxo do Método: 1. Define larguras específicas para as colunas. 2.
     * Centraliza o cabeçalho da tabela. 3. Desativa o redimensionamento
     * automático. 4. Centraliza o conteúdo de todas as colunas. 5. Trata erros
     * e exibe mensagem em caso de falha.
     */
    private void tabelaFormat() {

        int width = 90, width1 = 210, width2 = 200, width3 = 220, width4 = 90;
        JTableHeader header;

        try {

            header = tblPreEle.getTableHeader();
            DefaultTableCellRenderer centralizado = (DefaultTableCellRenderer) header.getDefaultRenderer();
            centralizado.setHorizontalAlignment(SwingConstants.CENTER);
            tblPreEle.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
            tblPreEle.getColumnModel().getColumn(0).setPreferredWidth(width);
            tblPreEle.getColumnModel().getColumn(1).setPreferredWidth(width1);
            tblPreEle.getColumnModel().getColumn(2).setPreferredWidth(width2);
            tblPreEle.getColumnModel().getColumn(3).setPreferredWidth(width3);
            tblPreEle.getColumnModel().getColumn(4).setPreferredWidth(width4);

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
//      onde está zero pode ser alterado para a coluna desejada
            for (int i = 0; i < tblPreEle.getColumnCount(); i++) {
                tblPreEle.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao alinhar as tabelas" + "/n" + e);
        }
    }

    /**
     * Método `tabelaFormat1`. Este método configura a aparência da tabela
     * `tblPreHid`, ajustando a largura das colunas e centralizando tanto os
     * cabeçalhos quanto o conteúdo das células. Ele melhora a apresentação
     * visual da tabela de preventivas hidráulicas.
     *
     * Funcionalidades: - **Definição de Largura das Colunas**: - Define
     * larguras específicas para as seis primeiras colunas: - Coluna 0: 90 px -
     * Coluna 1: 210 px - Coluna 2: 200 px - Coluna 3: 220 px - Coluna 4: 90 px
     * - Coluna 5: 150 px
     *
     * - **Centralização do Cabeçalho**: - Obtém o cabeçalho da tabela
     * (`JTableHeader`) e centraliza o texto horizontalmente.
     *
     * - **Desabilita Redimensionamento Automático**: - Define `AUTO_RESIZE_OFF`
     * para permitir controle manual das larguras.
     *
     * - **Centralização do Conteúdo das Células**: - Cria um
     * `DefaultTableCellRenderer` com alinhamento central. - Aplica esse
     * renderizador a todas as colunas da tabela.
     *
     * - **Tratamento de Erros**: - Envolve a configuração em um bloco
     * `try-catch` para capturar e exibir falhas de formatação.
     *
     * Fluxo do Método: 1. Define larguras específicas para cada coluna. 2.
     * Centraliza os cabeçalhos. 3. Desativa o redimensionamento automático. 4.
     * Centraliza o conteúdo de todas as colunas. 5. Trata possíveis exceções.
     */
    private void tabelaFormat1() {

        int width = 90, width1 = 210, width2 = 200, width3 = 220, width4 = 90, width5 = 150;
        JTableHeader header;

        try {

            header = tblPreHid.getTableHeader();
            DefaultTableCellRenderer centralizado1 = (DefaultTableCellRenderer) header.getDefaultRenderer();
            centralizado1.setHorizontalAlignment(SwingConstants.CENTER);
            tblPreHid.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
            tblPreHid.getColumnModel().getColumn(0).setPreferredWidth(width);
            tblPreHid.getColumnModel().getColumn(1).setPreferredWidth(width1);
            tblPreHid.getColumnModel().getColumn(2).setPreferredWidth(width2);
            tblPreHid.getColumnModel().getColumn(3).setPreferredWidth(width3);
            tblPreHid.getColumnModel().getColumn(4).setPreferredWidth(width4);
            tblPreHid.getColumnModel().getColumn(5).setPreferredWidth(width5);

            DefaultTableCellRenderer centerRenderer1 = new DefaultTableCellRenderer();
            centerRenderer1.setHorizontalAlignment(JLabel.CENTER);
//      onde está zero pode ser alterado para a coluna desejada
            for (int i = 0; i < tblPreHid.getColumnCount(); i++) {
                tblPreHid.getColumnModel().getColumn(i).setCellRenderer(centerRenderer1);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao alinhar as tabelas" + "/n" + e);
        }
    }

    /**
     * Método `tabelaFormat2`. Este método configura a aparência da tabela
     * `tblPreCivil`, ajustando a largura das colunas e centralizando tanto os
     * cabeçalhos quanto o conteúdo das células. Ele melhora a apresentação
     * visual da tabela de preventivas civis.
     *
     * Funcionalidades: - **Definição de Largura das Colunas**: - Define
     * larguras específicas para as seis primeiras colunas: - Coluna 0: 90 px -
     * Coluna 1: 210 px - Coluna 2: 200 px - Coluna 3: 220 px - Coluna 4: 90 px
     * - Coluna 5: 150 px
     *
     * - **Centralização do Cabeçalho**: - Obtém o cabeçalho da tabela
     * (`JTableHeader`) e centraliza o texto horizontalmente.
     *
     * - **Desabilita Redimensionamento Automático**: - Define `AUTO_RESIZE_OFF`
     * para permitir controle manual das larguras.
     *
     * - **Centralização do Conteúdo das Células**: - Cria um
     * `DefaultTableCellRenderer` com alinhamento central. - Aplica esse
     * renderizador a todas as colunas da tabela.
     *
     * - **Tratamento de Erros**: - Envolve a configuração em um bloco
     * `try-catch` para capturar e exibir falhas de formatação.
     *
     * Fluxo do Método: 1. Define larguras específicas para cada coluna. 2.
     * Centraliza os cabeçalhos. 3. Desativa o redimensionamento automático. 4.
     * Centraliza o conteúdo de todas as colunas. 5. Trata possíveis exceções.
     */
    private void tabelaFormat2() {

        int width = 90, width1 = 210, width2 = 200, width3 = 220, width4 = 90, width5 = 150;
        JTableHeader header;

        try {
            header = tblPreCivil.getTableHeader();
            DefaultTableCellRenderer centralizado2 = (DefaultTableCellRenderer) header.getDefaultRenderer();
            centralizado2.setHorizontalAlignment(SwingConstants.CENTER);
            tblPreCivil.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
            tblPreCivil.getColumnModel().getColumn(0).setPreferredWidth(width);
            tblPreCivil.getColumnModel().getColumn(1).setPreferredWidth(width1);
            tblPreCivil.getColumnModel().getColumn(2).setPreferredWidth(width2);
            tblPreCivil.getColumnModel().getColumn(3).setPreferredWidth(width3);
            tblPreCivil.getColumnModel().getColumn(4).setPreferredWidth(width4);
            tblPreCivil.getColumnModel().getColumn(5).setPreferredWidth(width5);

            DefaultTableCellRenderer centerRenderer2 = new DefaultTableCellRenderer();
            centerRenderer2.setHorizontalAlignment(JLabel.CENTER);
//      onde está zero pode ser alterado para a coluna desejada
            for (int i = 0; i < tblPreCivil.getColumnCount(); i++) {
                tblPreCivil.getColumnModel().getColumn(i).setCellRenderer(centerRenderer2);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao alinhar as tabelas" + "/n" + e);
        }

    }

    /**
     * Método `tabelaFormat3`. Este método configura a aparência da tabela
     * `tblPreRef`, ajustando a largura das colunas e centralizando tanto os
     * cabeçalhos quanto o conteúdo das células. Ele melhora a apresentação
     * visual da tabela de preventivas de refrigeração.
     *
     * Funcionalidades: - **Definição de Largura das Colunas**: - Define
     * larguras específicas para as seis primeiras colunas: - Coluna 0: 90 px -
     * Coluna 1: 210 px - Coluna 2: 200 px - Coluna 3: 220 px - Coluna 4: 90 px
     * - Coluna 5: 150 px
     *
     * - **Centralização do Cabeçalho**: - Obtém o cabeçalho da tabela
     * (`JTableHeader`) e centraliza o texto horizontalmente.
     *
     * - **Desabilita Redimensionamento Automático**: - Define `AUTO_RESIZE_OFF`
     * para permitir controle manual das larguras.
     *
     * - **Centralização do Conteúdo das Células**: - Cria um
     * `DefaultTableCellRenderer` com alinhamento central. - Aplica esse
     * renderizador a todas as colunas da tabela.
     *
     * - **Tratamento de Erros**: - Envolve a configuração em um bloco
     * `try-catch` para capturar e exibir falhas de formatação.
     *
     * Fluxo do Método: 1. Define larguras específicas para cada coluna. 2.
     * Centraliza os cabeçalhos. 3. Desativa o redimensionamento automático. 4.
     * Centraliza o conteúdo de todas as colunas. 5. Trata possíveis exceções.
     */
    private void tabelaFormat3() {

        int width = 90, width1 = 210, width2 = 200, width3 = 220, width4 = 90, width5 = 150;
        JTableHeader header;

        try {
            header = tblPreRef.getTableHeader();
            DefaultTableCellRenderer centralizado3 = (DefaultTableCellRenderer) header.getDefaultRenderer();
            centralizado3.setHorizontalAlignment(SwingConstants.CENTER);
            tblPreRef.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
            tblPreRef.getColumnModel().getColumn(0).setPreferredWidth(width);
            tblPreRef.getColumnModel().getColumn(1).setPreferredWidth(width1);
            tblPreRef.getColumnModel().getColumn(2).setPreferredWidth(width2);
            tblPreRef.getColumnModel().getColumn(3).setPreferredWidth(width3);
            tblPreRef.getColumnModel().getColumn(4).setPreferredWidth(width4);
            tblPreRef.getColumnModel().getColumn(5).setPreferredWidth(width5);

            DefaultTableCellRenderer centerRenderer3 = new DefaultTableCellRenderer();
            centerRenderer3.setHorizontalAlignment(JLabel.CENTER);
//      onde está zero pode ser alterado para a coluna desejada
            for (int i = 0; i < tblPreRef.getColumnCount(); i++) {
                tblPreRef.getColumnModel().getColumn(i).setCellRenderer(centerRenderer3);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao alinhar as tabelas" + "/n" + e);
        }
    }

    /**
     * Método `inicializacao`. Responsável por configurar a interface gráfica
     * com base no estado atual da variável `t`, que pode ser `"Encerrada"` ou
     * `"Irregular"`. Define o tipo de preventiva (`prev`), altera o ícone de
     * fundo, seleciona opções padrão e ajusta a cor das tabelas.
     *
     * Funcionalidades: - Define o valor de `prev` conforme o estado. - Altera o
     * ícone do rótulo `lblPreEnc` com base no tipo de preventiva. - Chama o
     * método `selec_encerrad()` para carregar os dados correspondentes. -
     * Seleciona o botão de opção `rbnPreMen` como padrão. - Define a cor do
     * texto das tabelas conforme o estado: - Azul para preventivas encerradas.
     * - Vermelho para preventivas irregulares. - Envolve toda a lógica em um
     * bloco `try-catch` para capturar exceções.
     */
    private void inicializacao() {

        try {

            if (t.equals("Encerrada")) {
                prev = "Encerrada";
                ImageIcon ico = new ImageIcon(getClass().getResource("/com/prjmanutencao/telas/imagens_fundo/encerradas_1.png"));
                lblPreEnc.setIcon(ico);
                selec_encerrad();
//                pesq_Bot();
                rbnPreMen.setSelected(true);
                rbnPreMen.setSelected(true);
                tblPreEle.setForeground(Color.blue);
                tblPreHid.setForeground(Color.blue);
                tblPreCivil.setForeground(Color.blue);
                tblPreRef.setForeground(Color.blue);
            }
            if (t.equals("Irregular")) {
                prev = "Irregular";
                ImageIcon ico = new ImageIcon(getClass().getResource("/com/prjmanutencao/telas/imagens_fundo/irregular_1.png"));
                lblPreEnc.setIcon(ico);
                selec_encerrad();
//                pesq_Bot();
                rbnPreMen.setSelected(true);
                tblPreEle.setForeground(Color.red);
                tblPreHid.setForeground(Color.red);
                tblPreCivil.setForeground(Color.red);
                tblPreRef.setForeground(Color.red);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método `selec_encerrad`. Responsável por carregar os dados das
     * preventivas encerradas de acordo com o tipo selecionado (`si`), que pode
     * ser `"Mensal"`, `"Trimestral"` ou `"Semestral"`. Também aplica a
     * formatação visual às tabelas.
     *
     * Funcionalidades: - Chama o método `limpar()` para resetar os campos da
     * interface. - Para cada tipo de preventiva: - Executa os métodos de
     * carregamento de dados (`ele_*_ini`, `hid_*_ini`, etc.). - Aplica a
     * formatação visual nas tabelas correspondentes (`tabelaFormat*()`). -
     * Envolve a lógica em um bloco `try-catch` para capturar e exibir erros.
     */
    private void selec_encerrad() {
//        limpar();
        try {

            if (si.equals("Mensal")) {
                ele_men_ini();
                tabelaFormat();
                hid_men_ini();
                tabelaFormat1();
                civ_men_ini();
                tabelaFormat2();
                ref_men_ini();
                tabelaFormat3();
            }

            if (si.equals("Trimestral")) {
                ele_tri_ini();
                tabelaFormat();
                hid_tri_ini();
                tabelaFormat1();
                civ_tri_ini();
                tabelaFormat2();
                ref_tri_ini();
                tabelaFormat3();

            }
            if (si.equals("Semestral")) {
                ele_sem_ini();
                tabelaFormat();
                hid_sem_ini();
                tabelaFormat1();
                civ_sem_ini();
                tabelaFormat2();
                ref_sem_ini();
                tabelaFormat3();

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método `inicializacao`. Responsável por configurar a interface gráfica
     * com base no estado atual da variável `t`, que pode ser `"Encerrada"` ou
     * `"Irregular"`. Define o tipo de preventiva (`prev`), altera o ícone de
     * fundo, seleciona opções padrão e ajusta a cor das tabelas.
     *
     * Funcionalidades: - Define o valor de `prev` conforme o estado. - Altera o
     * ícone do rótulo `lblPreEnc` com base no tipo de preventiva. - Chama o
     * método `selec_encerrad()` para carregar os dados correspondentes. -
     * Seleciona o botão de opção `rbnPreMen` como padrão. - Define a cor do
     * texto das tabelas conforme o estado: - Azul para preventivas encerradas.
     * - Vermelho para preventivas irregulares. - Envolve toda a lógica em um
     * bloco `try-catch` para capturar exceções.
     */
    private void ele_men_ini() {

        String s = prev;

        String sql = "select id_form_ele_mensal as 'N°', nome_prev as Técnico, setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', tempo_dis as Distribuição from  form_eletrica_mensal where iduser = ? and situacao_prev = ? and data_prev  > (NOW() - INTERVAL ? DAY) ";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, s);
            pst.setString(3, tempo);
            rs = pst.executeQuery();
            tblPreEle.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `selec_encerrad`. Responsável por carregar os dados das
     * preventivas encerradas de acordo com o tipo selecionado (`si`), que pode
     * ser `"Mensal"`, `"Trimestral"` ou `"Semestral"`. Também aplica a
     * formatação visual às tabelas.
     *
     * Funcionalidades: - Chama o método `limpar()` para resetar os campos da
     * interface. - Para cada tipo de preventiva: - Executa os métodos de
     * carregamento de dados (`ele_*_ini`, `hid_*_ini`, etc.). - Aplica a
     * formatação visual nas tabelas correspondentes (`tabelaFormat*()`). -
     * Envolve a lógica em um bloco `try-catch` para capturar e exibir erros.
     */
    private void ele_men() {

        String s = prev;

        String sql = "select id_form_ele_mensal as 'N°', nome_prev as Técnico, setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', tempo_dis as Distribuição from  form_eletrica_mensal where (iduser = ? and situacao_prev = ?  and id_form_ele_mensal like ?) or (iduser = ? and situacao_prev = ?  and setor_prev like ?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, s);
            pst.setString(3, txtPesqEle.getText() + "%");
            pst.setString(4, id);
            pst.setString(5, s);
            pst.setString(6, txtPesqEle.getText() + "%");
            rs = pst.executeQuery();
            tblPreEle.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `ele_tri_ini`. Carrega preventivas elétricas **trimestrais em
     * andamento**, filtrando por usuário (`iduser`), situação (`situacao_prev`)
     * e intervalo de dias (`tempo1`). Os dados são exibidos na tabela
     * `tblPreEle`.
     *
     * Funcionalidades: - Executa uma consulta SQL na tabela
     * `form_eletrica_trimestral` com os seguintes filtros: - `iduser` (usuário
     * logado) - `situacao_prev` (estado da preventiva, como "Encerrada" ou
     * "Irregular") - `data_prev` dentro de um intervalo de dias definido por
     * `tempo1` - Converte o `ResultSet` em modelo de tabela com `DbUtils`. -
     * Aplica formatação visual com `tabelaFormat()`. - Trata exceções SQL com
     * `JOptionPane`.
     */
    private void ele_tri_ini() {

        String s = prev;

        String sql = "select id_form_ele_trimestral as 'N°', nome_prev as Técnico, setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', tempo_dis as Distribuição from  form_eletrica_trimestral where iduser = ? and situacao_prev = ? and data_prev  > (NOW() - INTERVAL ? DAY) ";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, s);
            pst.setString(3, tempo1);
            rs = pst.executeQuery();
            tblPreEle.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `ele_tri`. Realiza a **pesquisa de preventivas elétricas
     * trimestrais** com base no número do formulário digitado no campo
     * `txtPesqEle`. Filtra os resultados por usuário (`iduser`), situação
     * (`situacao_prev`) e prefixo do ID (`id_form_ele_trimestral`).
     *
     * Funcionalidades: - Executa uma consulta SQL na tabela
     * `form_eletrica_trimestral` com os filtros: - `iduser` - `situacao_prev` -
     * `id_form_ele_trimestral` iniciando com o texto digitado - Atualiza a
     * tabela `tblPreEle` com os resultados. - Aplica formatação com
     * `tabelaFormat()`. - Trata exceções SQL com `JOptionPane`.
     */
    private void ele_tri() {
        String s = prev;

        String sql = "select id_form_ele_trimestral as 'N°', nome_prev as Técnico, setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', tempo_dis as Distribuição from  form_eletrica_trimestral where (iduser = ? and situacao_prev = ?  and id_form_ele_trimestral like ?) or (iduser = ? and situacao_prev = ?  and setor_prev like ?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, s);
            pst.setString(3, txtPesqEle.getText() + "%");
            pst.setString(4, id);
            pst.setString(5, s);
            pst.setString(6, txtPesqEle.getText() + "%");
            rs = pst.executeQuery();
            tblPreEle.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `ele_sem`. Realiza a **pesquisa de preventivas elétricas
     * semestrais** com base no número do formulário digitado no campo
     * `txtPesqEle`. Filtra os resultados por usuário (`iduser`), situação
     * (`situacao_prev`) e prefixo do ID (`id_form_ele_semestral`).
     *
     * Funcionalidades: - Define a variável `s` com o valor da situação
     * (`prev`). - Executa uma consulta SQL na tabela `form_eletrica_semestral`
     * com os filtros: - `iduser`: identifica o usuário logado. -
     * `situacao_prev`: estado da preventiva (ex: "Encerrada", "Irregular"). -
     * `id_form_ele_semestral`: inicia com o texto digitado no campo de
     * pesquisa. - Atualiza a tabela `tblPreEle` com os resultados usando
     * `DbUtils`. - Aplica formatação visual com `tabelaFormat()`. - Trata
     * exceções SQL com `JOptionPane`.
     */
    private void ele_sem() {
        String s = prev;

        String sql = "select id_form_ele_semestral as 'N°', nome_prev as Técnico, setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', tempo_dis as Distribuição from  form_eletrica_semestral where (iduser = ? and situacao_prev = ?  and id_form_ele_semestral like ?) or (iduser = ? and situacao_prev = ?  and setor_prev like ?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, s);
            pst.setString(3, txtPesqEle.getText() + "%");
            pst.setString(4, id);
            pst.setString(5, s);
            pst.setString(6, txtPesqEle.getText() + "%");
            rs = pst.executeQuery();
            tblPreEle.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `ele_sem_ini`. Carrega preventivas elétricas **semestrais
     * recentes**, com base em um intervalo de dias definido por `tempo2`.
     * Filtra os dados por usuário (`iduser`) e situação (`situacao_prev`),
     * exibindo os resultados na tabela `tblPreEle`.
     *
     * Funcionalidades: - Define a variável `s` com o valor da situação
     * (`prev`). - Executa uma consulta SQL na tabela `form_eletrica_semestral`
     * com os filtros: - `iduser`: identifica o usuário logado. -
     * `situacao_prev`: estado da preventiva. - `data_prev`: maior que a data
     * atual menos o intervalo de dias (`tempo2`). - Atualiza a tabela
     * `tblPreEle` com os resultados usando `DbUtils`. - Aplica formatação
     * visual com `tabelaFormat()`. - Trata exceções SQL com `JOptionPane`.
     */
    private void ele_sem_ini() {

        String s = prev;

        String sql = "select id_form_ele_semestral as 'N°', nome_prev as Técnico, setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', tempo_dis as Distribuição from  form_eletrica_semestral where iduser = ? and situacao_prev = ? and data_prev  > (NOW() - INTERVAL ? DAY) ";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, s);
            pst.setString(3, tempo2);
            rs = pst.executeQuery();
            tblPreEle.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `ele_sem`. Realiza a **pesquisa de preventivas elétricas
     * semestrais** com base no número do formulário digitado no campo
     * `txtPesqEle`. Filtra os resultados por usuário (`iduser`), situação
     * (`situacao_prev`) e prefixo do ID (`id_form_ele_semestral`).
     *
     * Funcionalidades: - Define a variável `s` com o valor da situação
     * (`prev`). - Executa uma consulta SQL na tabela `form_eletrica_semestral`
     * com os filtros: - `iduser`: identifica o usuário logado. -
     * `situacao_prev`: estado da preventiva (ex: "Encerrada", "Irregular"). -
     * `id_form_ele_semestral`: inicia com o texto digitado no campo de
     * pesquisa. - Atualiza a tabela `tblPreEle` com os resultados usando
     * `DbUtils`. - Aplica formatação visual com `tabelaFormat()`. - Trata
     * exceções SQL com `JOptionPane`.
     */
    private void setar_ele() {
        set = "Eletrica";
        int setar = tblPreEle.getSelectedRow();
        txtIdPrevIni.setText(tblPreEle.getModel().getValueAt(setar, 0).toString());
        txtTipPreIni.setText(set + " - " + si);
        txtPesqEle.setText(null);
        txtPesqHid.setText(null);
        txtPesqCiv.setText(null);
        txtPesqRef.setText(null);
        btnIniPrev.setEnabled(true);
        selec_encerrad();
    }

    /**
     * Método `hid_men_ini`. Responsável por carregar preventivas hidráulicas
     * mensais **recentes**, com base em um intervalo de dias definido pela
     * variável `tempo`. Os dados são filtrados por usuário (`iduser`) e
     * situação (`situacao_prev`), e exibidos na tabela `tblPreHid`.
     *
     * Funcionalidades: - **Consulta SQL**: - Seleciona colunas relevantes da
     * tabela `form_hidraulica_mensal`, como número do formulário, técnico,
     * setor, equipamento, código e distribuição. - Aplica filtros: - `iduser`:
     * identifica o usuário logado. - `situacao_prev`: estado da preventiva (ex:
     * "Encerrada", "Irregular"). - `data_prev`: registros com data posterior ao
     * intervalo definido por `tempo`.
     *
     * - **Execução e Exibição**: - Prepara e executa a consulta com
     * `PreparedStatement`. - Converte o `ResultSet` em modelo de tabela com
     * `DbUtils`. - Aplica formatação visual à tabela com `tabelaFormat1()`.
     *
     * - **Tratamento de Erros**: - Captura exceções SQL e exibe mensagem ao
     * usuário via `JOptionPane`.
     *
     * Fluxo do Método: 1. Define a situação da preventiva. 2. Prepara e executa
     * a consulta SQL com filtros. 3. Atualiza a tabela `tblPreHid` com os
     * resultados. 4. Aplica formatação visual. 5. Trata possíveis exceções.
     */
    private void hid_men_ini() {

        String s = prev;

        String sql = "select id_hidraulica_mensal as 'N°', nome_prev as Técnico, setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', tempo_dis as Distribuição from  form_hidraulica_mensal where iduser = ? and situacao_prev = ? and data_prev  > (NOW() - INTERVAL ? DAY) ";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, s);
            pst.setString(3, tempo);
            rs = pst.executeQuery();
            tblPreHid.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat1();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `hid_men`. Realiza a **pesquisa de preventivas hidráulicas
     * mensais**, filtrando os dados com base no número do formulário ou no
     * setor, conforme o texto digitado no campo `txtPesqEle`. Os resultados são
     * exibidos na tabela `tblPreHid`.
     *
     * Funcionalidades: - **Consulta SQL**: - A consulta busca registros da
     * tabela `form_hidraulica_mensal` com os seguintes filtros: - `iduser`:
     * identifica o usuário logado. - `situacao_prev`: estado da preventiva (ex:
     * "Encerrada", "Irregular"). - `id_form_ele_semestral LIKE ?` **(possível
     * erro)**: o nome da coluna parece incorreto para uma tabela mensal. -
     * `setor_prev LIKE ?`: alternativa de filtro por setor. - A cláusula `OR` é
     * usada para permitir que a pesquisa funcione com base em dois critérios
     * diferentes.
     *
     * - **Execução e Exibição**: - Os parâmetros são preenchidos com os valores
     * do usuário e do campo de pesquisa. - O `ResultSet` é convertido em modelo
     * de tabela com `DbUtils`. - A formatação visual é aplicada com
     * `tabelaFormat1()`.
     *
     * - **Tratamento de Erros**: - Captura exceções SQL e exibe mensagem ao
     * usuário via `JOptionPane`.
     *
     *
     */
    private void hid_men() {
        String s = prev;

        String sql = "select id_hidraulica_mensal as 'N°', nome_prev as 'Técnico', setor_prev as 'Setor', nome_equi_set as 'Equipamento', cod_equi_set as 'Código', tempo_dis as 'Distribuição' from  form_hidraulica_mensal where (iduser = ? and situacao_prev = ?  and id_hidraulica_mensal like ?) or (iduser = ? and situacao_prev = ?  and setor_prev like ?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, s);
            pst.setString(3, txtPesqEle.getText() + "%");
            pst.setString(4, id);
            pst.setString(5, s);
            pst.setString(6, txtPesqEle.getText() + "%");
            rs = pst.executeQuery();
            tblPreHid.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat1();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `hid_tri`. Realiza a **pesquisa de preventivas hidráulicas
     * trimestrais**, permitindo filtrar os registros com base no número do
     * formulário ou no setor, conforme o texto digitado no campo `txtPesqEle`.
     * Os resultados são exibidos na tabela `tblPreHid`.
     *
     * Funcionalidades: - **Consulta SQL**: - A consulta busca registros da
     * tabela `form_hidraulica_trimestral` com os seguintes filtros: - `iduser`:
     * identifica o usuário logado. - `situacao_prev`: estado da preventiva (ex:
     * "Encerrada", "Irregular"). - `id_form_ele_semestral LIKE ?` **(possível
     * erro)**: o nome da coluna parece incorreto para essa tabela. -
     * `setor_prev LIKE ?`: alternativa de filtro por setor. - A cláusula `OR`
     * permite que a pesquisa funcione com base em dois critérios diferentes.
     *
     * - **Execução e Exibição**: - Os parâmetros são preenchidos com os valores
     * do usuário e do campo de pesquisa. - O `ResultSet` é convertido em modelo
     * de tabela com `DbUtils`. - A formatação visual é aplicada com
     * `tabelaFormat1()`.
     *
     * - **Tratamento de Erros**: - Captura exceções SQL e exibe mensagem ao
     * usuário via `JOptionPane`.
     *
     */
    private void hid_tri() {
        String s = prev;

        String sql = "select id_hidraulica_trimestral as 'N°', nome_prev as Técnico, setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', tempo_dis as Distribuição from  form_hidraulica_trimestral where (iduser = ? and situacao_prev = ?  and id_hidraulica_trimestral like ?) or (iduser = ? and situacao_prev = ?  and setor_prev like ?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, s);
            pst.setString(3, txtPesqEle.getText() + "%");
            pst.setString(4, id);
            pst.setString(5, s);
            pst.setString(6, txtPesqEle.getText() + "%");
            rs = pst.executeQuery();
            tblPreHid.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat1();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `hid_tri_ini`. Responsável por carregar preventivas hidráulicas
     * **trimestrais recentes**, com base em um intervalo de dias definido pela
     * variável `tempo1`. Os dados são filtrados por usuário (`iduser`) e
     * situação (`situacao_prev`), e exibidos na tabela `tblPreHid`.
     *
     * Funcionalidades: - **Consulta SQL**: - Seleciona colunas relevantes da
     * tabela `form_hidraulica_trimestral`, como número do formulário, técnico,
     * setor, equipamento, código e distribuição. - Aplica filtros: - `iduser`:
     * identifica o usuário logado. - `situacao_prev`: estado da preventiva (ex:
     * "Encerrada", "Irregular"). - `data_prev`: registros com data posterior ao
     * intervalo definido por `tempo1`.
     *
     * - **Execução e Exibição**: - Prepara e executa a consulta com
     * `PreparedStatement`. - Converte o `ResultSet` em modelo de tabela com
     * `DbUtils`. - Aplica formatação visual à tabela com `tabelaFormat1()`.
     *
     * - **Tratamento de Erros**: - Captura exceções SQL e exibe mensagem ao
     * usuário via `JOptionPane`.
     *
     * Fluxo do Método: 1. Define a situação da preventiva. 2. Prepara e executa
     * a consulta SQL com filtros. 3. Atualiza a tabela `tblPreHid` com os
     * resultados. 4. Aplica formatação visual. 5. Trata possíveis exceções.
     */
    private void hid_tri_ini() {

        String s = prev;

        String sql = "select id_hidraulica_trimestral as 'N°', nome_prev as Técnico, setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', tempo_dis as Distribuição from  form_hidraulica_trimestral where iduser = ? and situacao_prev = ? and data_prev  > (NOW() - INTERVAL ? DAY) ";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, s);
            pst.setString(3, tempo1);
            rs = pst.executeQuery();
            tblPreHid.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat1();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `hid_sem`. Realiza a **pesquisa de preventivas hidráulicas
     * semestrais**, filtrando os registros com base no número do formulário
     * (`id_hidraulica_semestral`) ou no setor (`setor_prev`), conforme o texto
     * digitado no campo `txtPesqEle`. Os resultados são exibidos na tabela
     * `tblPreHid`.
     *
     * Funcionalidades: - **Consulta SQL**: - A consulta busca registros da
     * tabela `form_hidraulica_semestral` com os seguintes filtros: - `iduser`:
     * identifica o usuário logado. - `situacao_prev`: estado da preventiva (ex:
     * "Encerrada", "Irregular"). - `id_hidraulica_semestral LIKE ?` ou
     * `setor_prev LIKE ?`.
     *
     *
     * - **Execução e Exibição**: - Prepara e executa a consulta com
     * `PreparedStatement`. - Converte o `ResultSet` em modelo de tabela com
     * `DbUtils`. - Aplica formatação visual à tabela com `tabelaFormat1()`.
     *
     * - **Tratamento de Erros**: - Captura exceções SQL e exibe mensagem ao
     * usuário via `JOptionPane`.
     *
     */
    private void hid_sem() {
        String s = prev;

        String sql = "select id_hidraulica_semestral as 'N°', nome_prev as Técnico, setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', tempo_dis as Distribuição from  form_hidraulica_semestral where (iduser = ? and situacao_prev = ?  and id_hidraulica_semestral like ?) or (iduser = ? and situacao_prev = ?  and setor_prev like ?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, s);
            pst.setString(3, txtPesqEle.getText() + "%");
            pst.setString(4, id);
            pst.setString(5, s);
            pst.setString(6, txtPesqEle.getText() + "%");
            rs = pst.executeQuery();
            tblPreHid.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat1();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `hid_sem_ini`. Responsável por carregar preventivas hidráulicas
     * **semestrais recentes**, com base em um intervalo de dias definido pela
     * variável `tempo2`. Os dados são filtrados por usuário (`iduser`) e
     * situação (`situacao_prev`), e exibidos na tabela `tblPreHid`.
     *
     * Funcionalidades: - **Consulta SQL**: - Seleciona colunas relevantes da
     * tabela `form_hidraulica_semestral`, como número do formulário, técnico,
     * setor, equipamento, código e distribuição. - Aplica filtros: - `iduser`:
     * identifica o usuário logado. - `situacao_prev`: estado da preventiva (ex:
     * "Encerrada", "Irregular"). - `data_prev`: registros com data posterior ao
     * intervalo definido por `tempo2`.
     *
     * - **Execução e Exibição**: - Prepara e executa a consulta com
     * `PreparedStatement`. - Converte o `ResultSet` em modelo de tabela com
     * `DbUtils`. - Aplica formatação visual à tabela com `tabelaFormat1()`.
     *
     * - **Tratamento de Erros**: - Captura exceções SQL e exibe mensagem ao
     * usuário via `JOptionPane`.
     *
     * Fluxo do Método: 1. Define a situação da preventiva. 2. Prepara e executa
     * a consulta SQL com filtros. 3. Atualiza a tabela `tblPreHid` com os
     * resultados. 4. Aplica formatação visual. 5. Trata possíveis exceções.
     */
    private void hid_sem_ini() {

        String s = prev;

        String sql = "select id_hidraulica_semestral as 'N°', nome_prev as Técnico, setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', tempo_dis as Distribuição from  form_hidraulica_semestral where iduser = ? and situacao_prev = ? and data_prev  > (NOW() - INTERVAL ? DAY) ";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, s);
            pst.setString(3, tempo2);
            rs = pst.executeQuery();
            tblPreHid.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat1();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `setar_hid`. Este método é acionado quando o usuário seleciona uma
     * linha da tabela `tblPreHid`, que exibe preventivas hidráulicas. Ele
     * extrai os dados da linha selecionada e os insere nos campos da interface
     * gráfica, além de limpar os campos de pesquisa e habilitar o botão de
     * início da preventiva. Também atualiza a visualização chamando
     * `selec_encerrad()`.
     *
     * Funcionalidades: - **Definição do Tipo de Preventiva**: - Define a
     * variável `set` como `"Hidraulica"`, indicando o tipo de preventiva
     * selecionada.
     *
     * - **Captura da Linha Selecionada**: - Obtém o índice da linha selecionada
     * na tabela `tblPreHid`. - Define o campo `txtIdPrevIni` com o valor da
     * primeira coluna da linha selecionada. - Define o campo `txtTipPreIni` com
     * o tipo de preventiva concatenado com o valor da variável `si` (ex:
     * "Hidraulica - Trimestral").
     *
     * - **Limpeza de Campos de Pesquisa**: - Limpa os campos de texto
     * `txtPesqEle`, `txtPesqHid`, `txtPesqCiv` e `txtPesqRef`.
     *
     * - **Habilitação de Botão**: - Ativa o botão `btnIniPrev`, permitindo que
     * o usuário inicie a preventiva selecionada.
     *
     * - **Atualização da Visualização**: - Chama o método `selec_encerrad()`
     * para recarregar os dados com base na seleção atual.
     *
     * Fluxo do Método: 1. Define o tipo de preventiva como "Hidraulica". 2.
     * Obtém a linha selecionada na tabela. 3. Preenche os campos com os dados
     * da linha. 4. Limpa os campos de pesquisa. 5. Habilita o botão de início
     * da preventiva. 6. Atualiza a visualização com `selec_encerrad()`.
     */
    private void setar_hid() {
        set = "Hidraulica";
        int setar = tblPreHid.getSelectedRow();
        txtIdPrevIni.setText(tblPreHid.getModel().getValueAt(setar, 0).toString());
        txtTipPreIni.setText(set + " - " + si);
        txtPesqEle.setText(null);
        txtPesqHid.setText(null);
        txtPesqCiv.setText(null);
        txtPesqRef.setText(null);
        btnIniPrev.setEnabled(true);
        selec_encerrad();
    }

    private void civ_men() {
        String s = prev;

        String sql = "select id_civil_mensal as 'N°', nome_prev as Técnico, setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', tempo_dis as Distribuição from  form_civil_mensal where (iduser = ? and situacao_prev = ?  and id_civil_mensal like ?) or (iduser = ? and situacao_prev = ?  and setor_prev like ?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, s);
            pst.setString(3, txtPesqEle.getText() + "%");
            pst.setString(4, id);
            pst.setString(5, s);
            pst.setString(6, txtPesqEle.getText() + "%");
            rs = pst.executeQuery();
            tblPreCivil.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat2();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `civ_men_ini`. Responsável por carregar preventivas civis mensais
     * **recentes**, com base em um intervalo de dias definido pela variável
     * `tempo`. Os dados são filtrados por usuário (`iduser`) e situação
     * (`situacao_prev`), e exibidos na tabela `tblPreCivil`.
     *
     * Funcionalidades: - **Consulta SQL**: - Seleciona colunas relevantes da
     * tabela `form_civil_mensal`, como: - Número do formulário
     * (`id_civil_mensal`) - Nome do técnico (`nome_prev`) - Setor
     * (`setor_prev`) - Equipamento (`nome_equi_set`) - Código do equipamento
     * (`cod_equi_set`) - Distribuição de tempo (`tempo_dis`) - Aplica filtros:
     * - `iduser`: identifica o usuário logado. - `situacao_prev`: estado da
     * preventiva (ex: "Encerrada", "Irregular"). - `data_prev`: registros com
     * data posterior ao intervalo definido por `tempo`.
     *
     * - **Execução e Exibição**: - Prepara e executa a consulta com
     * `PreparedStatement`. - Converte o `ResultSet` em modelo de tabela com
     * `DbUtils`. - Aplica formatação visual à tabela com `tabelaFormat2()`.
     *
     * - **Tratamento de Erros**: - Captura exceções SQL e exibe mensagem ao
     * usuário via `JOptionPane`.
     *
     * Fluxo do Método: 1. Define a situação da preventiva. 2. Prepara e executa
     * a consulta SQL com filtros. 3. Atualiza a tabela `tblPreCivil` com os
     * resultados. 4. Aplica formatação visual. 5. Trata possíveis exceções.
     */
    private void civ_men_ini() {

        String s = prev;

        String sql = "select id_civil_mensal as 'N°', nome_prev as Técnico, setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', tempo_dis as Distribuição from  form_civil_mensal where iduser = ? and situacao_prev = ? and data_prev  > (NOW() - INTERVAL ? DAY) ";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, s);
            pst.setString(3, tempo);
            rs = pst.executeQuery();
            tblPreCivil.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat2();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `civ_tri`. Realiza a **pesquisa de preventivas civis
     * trimestrais**, permitindo filtrar os registros com base no número do
     * formulário (`id_civil_trimestral`) ou no setor (`setor_prev`), conforme o
     * texto digitado no campo `txtPesqEle`. Os resultados são exibidos na
     * tabela `tblPreCivil`.
     *
     * Funcionalidades: - **Consulta SQL**: - A consulta busca registros da
     * tabela `form_civil_trimestral` com os seguintes filtros: - `iduser`:
     * identifica o usuário logado. - `situacao_prev`: estado da preventiva (ex:
     * "Encerrada", "Irregular"). - `id_civil_trimestral LIKE ?` ou `setor_prev
     * LIKE ?`. - A cláusula `OR` está corretamente agrupada com parênteses para
     * garantir a lógica adequada da consulta.
     *
     * - **Execução e Exibição**: - Os parâmetros são preenchidos com os valores
     * do usuário e do campo de pesquisa. - O `ResultSet` é convertido em modelo
     * de tabela com `DbUtils`. - A formatação visual é aplicada com
     * `tabelaFormat2()`.
     *
     * - **Tratamento de Erros**: - Captura exceções SQL e exibe mensagem ao
     * usuário via `JOptionPane`.
     *
     * Fluxo do Método: 1. Define a situação da preventiva. 2. Prepara e executa
     * a consulta SQL com filtros múltiplos. 3. Atualiza a tabela `tblPreCivil`
     * com os resultados. 4. Aplica formatação visual. 5. Trata possíveis
     * exceções.
     */
    private void civ_tri() {
        String s = prev;

        String sql = "select id_civil_trimestral as 'N°', nome_prev as Técnico, setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', tempo_dis as Distribuição from  form_civil_trimestral where (iduser = ? and situacao_prev = ?  and id_civil_trimestral like ?) or (iduser = ? and situacao_prev = ?  and setor_prev like ?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, s);
            pst.setString(3, txtPesqEle.getText() + "%");
            pst.setString(4, id);
            pst.setString(5, s);
            pst.setString(6, txtPesqEle.getText() + "%");
            rs = pst.executeQuery();
            tblPreCivil.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat2();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `civ_tri_ini`. Carrega preventivas civis **trimestrais recentes**,
     * com base em um intervalo de dias definido pela variável `tempo1`. Os
     * dados são filtrados por usuário (`iduser`) e situação (`situacao_prev`),
     * e exibidos na tabela `tblPreCivil`.
     *
     * Funcionalidades: - Executa uma consulta SQL na tabela
     * `form_civil_trimestral` com os filtros: - `iduser`: identifica o usuário
     * logado. - `situacao_prev`: estado da preventiva (ex: "Encerrada",
     * "Irregular"). - `data_prev`: registros com data posterior ao intervalo
     * definido por `tempo1`. - Atualiza a tabela `tblPreCivil` com os
     * resultados. - Aplica formatação visual com `tabelaFormat2()`. - Trata
     * exceções SQL com `JOptionPane`.
     */
    private void civ_tri_ini() {

        String s = prev;

        String sql = "select id_civil_trimestral as 'N°', nome_prev as Técnico, setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', tempo_dis as Distribuição from  form_civil_trimestral where iduser = ? and situacao_prev = ? and data_prev  > (NOW() - INTERVAL ? DAY) ";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, s);
            pst.setString(3, tempo1);
            rs = pst.executeQuery();
            tblPreCivil.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat2();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `civ_sem`. Realiza a **pesquisa de preventivas civis semestrais**,
     * permitindo filtrar os registros com base no número do formulário
     * (`id_civil_semestral`) ou no setor (`setor_prev`), conforme o texto
     * digitado no campo `txtPesqEle`. Os resultados são exibidos na tabela
     * `tblPreCivil`.
     *
     * Funcionalidades: - Executa uma consulta SQL na tabela
     * `form_civil_semestral` com os seguintes filtros: - `iduser`: identifica o
     * usuário logado. - `situacao_prev`: estado da preventiva. -
     * `id_civil_semestral LIKE ?` ou `setor_prev LIKE ?`. - A cláusula `OR`
     * está corretamente agrupada com parênteses para garantir a lógica da
     * consulta. - Atualiza a tabela `tblPreCivil` com os resultados. - Aplica
     * formatação visual com `tabelaFormat2()`. - Trata exceções SQL com
     * `JOptionPane`.
     */
    private void civ_sem() {
        String s = prev;

        String sql = "select id_civil_semestral as 'N°', nome_prev as Técnico, setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', tempo_dis as Distribuição from  form_civil_semestral where (iduser = ? and situacao_prev = ?  and id_civil_semestral like ?) or (iduser = ? and situacao_prev = ?  and setor_prev like ?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, s);
            pst.setString(3, txtPesqEle.getText() + "%");
            pst.setString(4, id);
            pst.setString(5, s);
            pst.setString(6, txtPesqEle.getText() + "%");
            rs = pst.executeQuery();
            tblPreCivil.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat2();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void civ_sem_ini() {

        String s = prev;

        String sql = "select id_civil_semestral as 'N°', nome_prev as Técnico, setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', tempo_dis as Distribuição from  form_civil_semestral where iduser = ? and situacao_prev = ? and data_prev  > (NOW() - INTERVAL ? DAY) ";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, s);
            pst.setString(3, tempo2);
            rs = pst.executeQuery();
            tblPreCivil.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat2();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `civ_tri_ini`. Carrega preventivas civis **trimestrais recentes**,
     * com base em um intervalo de dias definido pela variável `tempo1`. Os
     * dados são filtrados por usuário (`iduser`) e situação (`situacao_prev`),
     * e exibidos na tabela `tblPreCivil`.
     *
     * Funcionalidades: - Executa uma consulta SQL na tabela
     * `form_civil_trimestral` com os filtros: - `iduser`: identifica o usuário
     * logado. - `situacao_prev`: estado da preventiva (ex: "Encerrada",
     * "Irregular"). - `data_prev`: registros com data posterior ao intervalo
     * definido por `tempo1`. - Atualiza a tabela `tblPreCivil` com os
     * resultados. - Aplica formatação visual com `tabelaFormat2()`. - Trata
     * exceções SQL com `JOptionPane`.
     */
    private void setar_civ() {
        set = "Civil";
        int setar = tblPreCivil.getSelectedRow();
        txtIdPrevIni.setText(tblPreCivil.getModel().getValueAt(setar, 0).toString());
        txtTipPreIni.setText(set + " - " + si);
        txtPesqEle.setText(null);
        txtPesqHid.setText(null);
        txtPesqCiv.setText(null);
        txtPesqRef.setText(null);
        btnIniPrev.setEnabled(true);
        selec_encerrad();
    }

    /**
     * Método `civ_sem`. Realiza a **pesquisa de preventivas civis semestrais**,
     * permitindo filtrar os registros com base no número do formulário
     * (`id_civil_semestral`) ou no setor (`setor_prev`), conforme o texto
     * digitado no campo `txtPesqEle`. Os resultados são exibidos na tabela
     * `tblPreCivil`.
     *
     * Funcionalidades: - Executa uma consulta SQL na tabela
     * `form_civil_semestral` com os seguintes filtros: - `iduser`: identifica o
     * usuário logado. - `situacao_prev`: estado da preventiva. -
     * `id_civil_semestral LIKE ?` ou `setor_prev LIKE ?`. - A cláusula `OR`
     * está corretamente agrupada com parênteses para garantir a lógica da
     * consulta. - Atualiza a tabela `tblPreCivil` com os resultados. - Aplica
     * formatação visual com `tabelaFormat2()`. - Trata exceções SQL com
     * `JOptionPane`.
     */
    private void ref_men() {
        String s = prev;

        String sql = "select id_refrigeracao_mensal as 'N°', nome_prev as Técnico, setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', tempo_dis as Distribuição from  form_refrigeracao_mensal where (iduser = ? and situacao_prev = ?  and id_refrigeracao_mensal like ?) or (iduser = ? and situacao_prev = ?  and setor_prev like ?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, s);
            pst.setString(3, txtPesqEle.getText() + "%");
            pst.setString(4, id);
            pst.setString(5, s);
            pst.setString(6, txtPesqEle.getText() + "%");
            rs = pst.executeQuery();
            tblPreRef.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat3();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `ref_men_ini`. Carrega preventivas de refrigeração **mensais
     * recentes**, com base em um intervalo de dias definido pela variável
     * `tempo`. Os dados são filtrados por usuário (`iduser`) e situação
     * (`situacao_prev`), e exibidos na tabela `tblPreRef`.
     *
     * Funcionalidades: - Executa uma consulta SQL na tabela
     * `form_refrigeracao_mensal` com os filtros: - `iduser`: identifica o
     * usuário logado. - `situacao_prev`: estado da preventiva (ex: "Encerrada",
     * "Irregular"). - `data_prev`: registros com data posterior ao intervalo
     * definido por `tempo`. - Atualiza a tabela `tblPreRef` com os resultados.
     * - Aplica formatação visual com `tabelaFormat3()`. - Trata exceções SQL
     * com `JOptionPane`.
     */
    private void ref_men_ini() {

        String s = prev;

        String sql = "select id_refrigeracao_mensal as 'N°', nome_prev as Técnico, setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', tempo_dis as Distribuição from  form_refrigeracao_mensal where iduser = ? and situacao_prev = ? and data_prev  > (NOW() - INTERVAL ? DAY) ";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, s);
            pst.setString(3, tempo);
            rs = pst.executeQuery();
            tblPreRef.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat3();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `ref_tri`. Realiza a **pesquisa de preventivas de refrigeração
     * trimestrais**, permitindo filtrar os registros com base no número do
     * formulário (`id_refrigeracao_trimestral`) ou no setor (`setor_prev`),
     * conforme o texto digitado no campo `txtPesqEle`. Os resultados são
     * exibidos na tabela `tblPreRef`.
     *
     * Funcionalidades: - **Consulta SQL**: - A consulta busca registros da
     * tabela `form_refrigeracao_trimestral` com os seguintes filtros: -
     * `iduser`: identifica o usuário logado. - `situacao_prev`: estado da
     * preventiva (ex: "Encerrada", "Irregular"). - `id_refrigeracao_trimestral
     * LIKE ?` ou `setor_prev LIKE ?`. - A cláusula `OR` está corretamente
     * agrupada com parênteses para garantir a lógica da consulta.
     *
     * - **Execução e Exibição**: - Os parâmetros são preenchidos com os valores
     * do usuário e do campo de pesquisa. - O `ResultSet` é convertido em modelo
     * de tabela com `DbUtils`. - A formatação visual é aplicada com
     * `tabelaFormat3()`.
     *
     * - **Tratamento de Erros**: - Captura exceções SQL e exibe mensagem ao
     * usuário via `JOptionPane`.
     *
     * Fluxo do Método: 1. Define a situação da preventiva. 2. Prepara e executa
     * a consulta SQL com filtros múltiplos. 3. Atualiza a tabela `tblPreRef`
     * com os resultados. 4. Aplica formatação visual. 5. Trata possíveis
     * exceções.
     */
    private void ref_tri() {
        String s = prev;

        String sql = "select id_refrigeracao_trimestral as 'N°', nome_prev as Técnico, setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', tempo_dis as Distribuição from  form_refrigeracao_trimestral where (iduser = ? and situacao_prev = ?  and id_refrigeracao_trimestral like ?) or (iduser = ? and situacao_prev = ?  and setor_prev like ?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, s);
            pst.setString(3, txtPesqEle.getText() + "%");
            pst.setString(4, id);
            pst.setString(5, s);
            pst.setString(6, txtPesqEle.getText() + "%");
            rs = pst.executeQuery();
            tblPreRef.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat3();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `ref_tri_ini`. Carrega preventivas de refrigeração **trimestrais
     * recentes**, com base em um intervalo de dias definido pela variável
     * `tempo1`. Os dados são filtrados por usuário (`iduser`) e situação
     * (`situacao_prev`), e exibidos na tabela `tblPreRef`.
     *
     * Funcionalidades: - Executa uma consulta SQL na tabela
     * `form_refrigeracao_trimestral` com os filtros: - `iduser`: identifica o
     * usuário logado. - `situacao_prev`: estado da preventiva (ex: "Encerrada",
     * "Irregular"). - `data_prev`: registros com data posterior ao intervalo
     * definido por `tempo1`. - Atualiza a tabela `tblPreRef` com os resultados.
     * - Aplica formatação visual com `tabelaFormat3()`. - Trata exceções SQL
     * com `JOptionPane`.
     */
    private void ref_tri_ini() {

        String s = prev;

        String sql = "select id_refrigeracao_trimestral as 'N°', nome_prev as Técnico, setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', tempo_dis as Distribuição from  form_refrigeracao_trimestral where iduser = ? and situacao_prev = ? and data_prev  > (NOW() - INTERVAL ? DAY) ";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, s);
            pst.setString(3, tempo1);
            rs = pst.executeQuery();
            tblPreRef.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat3();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `ref_sem`. Realiza a **pesquisa de preventivas de refrigeração
     * semestrais**, permitindo filtrar os registros com base no número do
     * formulário (`id_refrigeracao_semestral`) ou no setor (`setor_prev`),
     * conforme o texto digitado no campo `txtPesqEle`. Os resultados são
     * exibidos na tabela `tblPreRef`.
     *
     * Funcionalidades: - Executa uma consulta SQL na tabela
     * `form_refrigeracao_semestral` com os seguintes filtros: - `iduser` e
     * `situacao_prev` combinados com `id_refrigeracao_semestral LIKE ?` - ou
     * `iduser` e `situacao_prev` combinados com `setor_prev LIKE ?` - A
     * cláusula `OR` está corretamente agrupada com parênteses para garantir a
     * lógica da consulta. - Preenche os parâmetros da consulta com os valores
     * do usuário e do campo de pesquisa. - Atualiza a tabela `tblPreRef` com os
     * resultados. - Aplica formatação visual com `tabelaFormat3()`. - Trata
     * exceções SQL com `JOptionPane`.
     */
    private void ref_sem() {
        String s = prev;

        String sql = "select id_refrigeracao_semestral as 'N°', nome_prev as Técnico, setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', tempo_dis as Distribuição from  form_refrigeracao_semestral where iduser = ? and situacao_prev = ?  and id_refrigeracao_semestral like ? or iduser = ? and situacao_prev = ?  and setor_prev like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, s);
            pst.setString(3, txtPesqEle.getText() + "%");
            pst.setString(4, id);
            pst.setString(5, s);
            pst.setString(6, txtPesqEle.getText() + "%");
            rs = pst.executeQuery();
            tblPreRef.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat3();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `ref_sem_ini`. Carrega preventivas de refrigeração **semestrais
     * recentes**, com base em um intervalo de dias definido pela variável
     * `tempo2`. Os dados são filtrados por usuário (`iduser`) e situação
     * (`situacao_prev`), e exibidos na tabela `tblPreRef`.
     *
     * Funcionalidades: - **Consulta SQL**: - Seleciona colunas relevantes da
     * tabela `form_refrigeracao_semestral`, como: - Número do formulário
     * (`id_refrigeracao_semestral`) - Nome do técnico (`nome_prev`) - Setor
     * (`setor_prev`) - Equipamento (`nome_equi_set`) - Código do equipamento
     * (`cod_equi_set`) - Distribuição de tempo (`tempo_dis`) - Aplica filtros:
     * - `iduser`: identifica o usuário logado. - `situacao_prev`: estado da
     * preventiva (ex: "Encerrada", "Irregular"). - `data_prev`: registros com
     * data posterior ao intervalo definido por `tempo2`.
     *
     * - **Execução e Exibição**: - Prepara e executa a consulta com
     * `PreparedStatement`. - Converte o `ResultSet` em modelo de tabela com
     * `DbUtils`. - Aplica formatação visual à tabela com `tabelaFormat3()`.
     *
     * - **Tratamento de Erros**: - Captura exceções SQL e exibe mensagem ao
     * usuário via `JOptionPane`.
     *
     * Fluxo do Método: 1. Define a situação da preventiva. 2. Prepara e executa
     * a consulta SQL com filtros. 3. Atualiza a tabela `tblPreRef` com os
     * resultados. 4. Aplica formatação visual. 5. Trata possíveis exceções.
     */
    private void ref_sem_ini() {

        String s = prev;

        String sql = "select id_refrigeracao_semestral as 'N°', nome_prev as Técnico, setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', tempo_dis as Distribuição from  form_refrigeracao_semestral where iduser = ? and situacao_prev = ? and data_prev  > (NOW() - INTERVAL ? DAY) ";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, s);
            pst.setString(3, tempo2);
            rs = pst.executeQuery();
            tblPreRef.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat3();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `setar_ref`. Acionado quando o usuário seleciona uma linha da
     * tabela `tblPreRef`, este método extrai os dados da linha selecionada e os
     * insere nos campos da interface gráfica. Também limpa os campos de
     * pesquisa e atualiza a visualização.
     *
     * Funcionalidades: - **Definição do Tipo de Preventiva**: - Define a
     * variável `set` como `"Refrigeração"`, indicando o tipo de preventiva
     * selecionada.
     *
     * - **Captura da Linha Selecionada**: - Obtém o índice da linha selecionada
     * na tabela `tblPreRef`. - Define o campo `txtIdPrevIni` com o valor da
     * primeira coluna da linha selecionada. - Define o campo `txtTipPreIni` com
     * o tipo de preventiva concatenado com o valor da variável `si` (ex:
     * "Refrigeração - Trimestral").
     *
     * - **Limpeza de Campos de Pesquisa**: - Limpa os campos de texto
     * `txtPesqEle`, `txtPesqHid`, `txtPesqCiv` e `txtPesqRef`.
     *
     * - **Habilitação de Botão**: - Ativa o botão `btnIniPrev`, permitindo que
     * o usuário inicie a preventiva selecionada.
     *
     * - **Atualização da Visualização**: - Chama o método `selec_encerrad()`
     * para recarregar os dados com base na seleção atual.
     *
     * Fluxo do Método: 1. Define o tipo de preventiva como "Refrigeração". 2.
     * Obtém a linha selecionada na tabela. 3. Preenche os campos com os dados
     * da linha. 4. Limpa os campos de pesquisa. 5. Habilita o botão de início
     * da preventiva. 6. Atualiza a visualização com `selec_encerrad()`.
     */
    private void setar_ref() {
        set = "Refrigeração";
        int setar = tblPreRef.getSelectedRow();
        txtIdPrevIni.setText(tblPreRef.getModel().getValueAt(setar, 0).toString());
        txtTipPreIni.setText(set + " - " + si);
        txtPesqEle.setText(null);
        txtPesqHid.setText(null);
        txtPesqCiv.setText(null);
        txtPesqRef.setText(null);
        btnIniPrev.setEnabled(true);
        selec_encerrad();
    }

    /**
     * Método `pev_pesq`. Realiza a **pesquisa de preventivas elétricas** com
     * base na data selecionada no componente `cadDataPre`. A pesquisa é feita
     * de acordo com o tipo de setor selecionado (`Eletrica - Mensal`,
     * `Trimestral` ou `Semestral`), e os resultados são exibidos na tabela
     * `tblPreEle`.
     *
     * Funcionalidades: - **Captura da Data**: - Obtém a data selecionada no
     * componente `cadDataPre`. - Formata a data no padrão `dd/MM/yyyy` e
     * armazena em `this.data`.
     *
     * - **Consulta Condicional**: - Verifica o valor da variável `setor` para
     * determinar qual tabela consultar: - `"Eletrica - Mensal"` →
     * `form_eletrica_mensal` - `"Eletrica - Trimestral"` →
     * `form_eletrica_trimestral` - `"Eletrica - Semestral"` →
     * `form_eletrica_semestral` - Executa uma consulta SQL filtrando por: -
     * `iduser`: usuário logado. - `situacao_prev`: situação da preventiva (ex:
     * "Encerrada", "Irregular"). - `tempo_dis LIKE ?`: campo de distribuição de
     * tempo iniciando com a data formatada.
     *
     * - **Exibição dos Resultados**: - Converte o `ResultSet` em modelo de
     * tabela com `DbUtils`. - Aplica formatação visual com `tabelaFormat()`.
     *
     * - **Tratamento de Erros**: - Cada bloco de consulta está envolvido em
     * `try-catch` para capturar e exibir exceções SQL.
     *
     * Observações: - O método pode ser refatorado para evitar repetição,
     * extraindo a lógica comum em um método auxiliar que receba o nome da
     * tabela e o campo de ID como parâmetros.
     */
    private void pev_pesq() {
        try {
            if (setor.equals("Eletrica - Mensal")) {
                String s = prev;
                java.util.Date pega = cadDataPre.getDate();
                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                this.data = formato.format(pega);

                String sql = "select id_form_ele_mensal as 'N°', nome_prev as Técnico, setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', tempo_dis as Distribuição from  form_eletrica_mensal where iduser = ? and situacao_prev = ? and tempo_dis like ?";
                try {
                    pst = conexao.prepareStatement(sql);
                    pst.setString(1, id);
                    pst.setString(2, s);
                    pst.setString(3, data + "%");
                    rs = pst.executeQuery();
                    tblPreEle.setModel(DbUtils.resultSetToTableModel(rs));
                    tabelaFormat();

                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            }
            if (setor.equals("Eletrica - Trimestral")) {
                String s = prev;
                java.util.Date pega = cadDataPre.getDate();
                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                this.data = formato.format(pega);

                String sql = "select id_form_ele_trimestral as 'N°', nome_prev as Técnico, setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', tempo_dis as Distribuição from  form_eletrica_trimestral where iduser = ? and situacao_prev = ? and tempo_dis like ?";
                try {
                    pst = conexao.prepareStatement(sql);
                    pst.setString(1, id);
                    pst.setString(2, s);
                    pst.setString(3, data + "%");
                    rs = pst.executeQuery();
                    tblPreEle.setModel(DbUtils.resultSetToTableModel(rs));
                    tabelaFormat();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            }
            if (setor.equals("Eletrica - Semestral")) {
                String s = prev;
                java.util.Date pega = cadDataPre.getDate();
                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                this.data = formato.format(pega);

                String sql = "select id_form_ele_semestral as 'N°', nome_prev as Técnico, setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', tempo_dis as Distribuição from  form_eletrica_semestral where iduser = ? and situacao_prev = ? and tempo_dis like ?";
                try {
                    pst = conexao.prepareStatement(sql);
                    pst.setString(1, id);
                    pst.setString(2, s);
                    pst.setString(3, data + "%");
                    rs = pst.executeQuery();
                    tblPreEle.setModel(DbUtils.resultSetToTableModel(rs));
                    tabelaFormat();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            }
            if (setor.equals("Hidráulica - Mensal")) {
                String s = prev;
                java.util.Date pega = cadDataPre.getDate();
                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                this.data = formato.format(pega);

                String sql = "select id_hidraulica_mensal as 'N°', nome_prev as Técnico, setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', tempo_dis as Distribuição from  form_hidraulica_mensal where iduser = ? and situacao_prev = ? and tempo_dis like ?";
                try {
                    pst = conexao.prepareStatement(sql);
                    pst.setString(1, id);
                    pst.setString(2, s);
                    pst.setString(3, data + "%");
                    rs = pst.executeQuery();
                    tblPreHid.setModel(DbUtils.resultSetToTableModel(rs));
                    tabelaFormat1();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            }
            if (setor.equals("Hidráulica - Trimestral")) {
                String s = prev;
                java.util.Date pega = cadDataPre.getDate();
                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                this.data = formato.format(pega);

                String sql = "select id_hidraulica_trimestral as 'N°', nome_prev as Técnico, setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', tempo_dis as Distribuição from  form_hidraulica_trimestral where iduser = ? and situacao_prev = ? and tempo_dis like ?";
                try {
                    pst = conexao.prepareStatement(sql);
                    pst.setString(1, id);
                    pst.setString(2, s);
                    pst.setString(3, data + "%");
                    rs = pst.executeQuery();
                    tblPreHid.setModel(DbUtils.resultSetToTableModel(rs));
                    tabelaFormat1();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            }
            if (setor.equals("Hidráulica - Semestral")) {
                String s = prev;
                java.util.Date pega = cadDataPre.getDate();
                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                this.data = formato.format(pega);

                String sql = "select id_hidraulica_semestral as 'N°', nome_prev as Técnico, setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', tempo_dis as Distribuição from  form_hidraulica_semestral where iduser = ? and situacao_prev = ? and tempo_dis like ?";
                try {
                    pst = conexao.prepareStatement(sql);
                    pst.setString(1, id);
                    pst.setString(2, s);
                    pst.setString(3, data + "%");
                    rs = pst.executeQuery();
                    tblPreHid.setModel(DbUtils.resultSetToTableModel(rs));
                    tabelaFormat1();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            }
            if (setor.equals("Civil - Mensal")) {
                String s = prev;
                java.util.Date pega = cadDataPre.getDate();
                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                this.data = formato.format(pega);

                String sql = "select id_civil_mensal as 'N°', nome_prev as Técnico, setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', tempo_dis as Distribuição from  form_civil_mensal where iduser = ? and situacao_prev = ? and tempo_dis like ?";
                try {
                    pst = conexao.prepareStatement(sql);
                    pst.setString(1, id);
                    pst.setString(2, s);
                    pst.setString(3, data + "%");
                    rs = pst.executeQuery();
                    tblPreCivil.setModel(DbUtils.resultSetToTableModel(rs));
                    tabelaFormat2();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            }
            if (setor.equals("Civil - Trimestral")) {
                String s = prev;
                java.util.Date pega = cadDataPre.getDate();
                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                this.data = formato.format(pega);

                String sql = "select id_civil_trimestral as 'N°', nome_prev as Técnico, setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', tempo_dis as Distribuição from  form_civil_trimestral where iduser = ? and situacao_prev = ? and tempo_dis like ?";
                try {
                    pst = conexao.prepareStatement(sql);
                    pst.setString(1, id);
                    pst.setString(2, s);
                    pst.setString(3, data + "%");
                    rs = pst.executeQuery();
                    tblPreCivil.setModel(DbUtils.resultSetToTableModel(rs));
                    tabelaFormat2();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            }
            if (setor.equals("Civil - Semestral")) {
                String s = prev;
                java.util.Date pega = cadDataPre.getDate();
                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                this.data = formato.format(pega);

                String sql = "select id_civil_semestral as 'N°', nome_prev as Técnico, setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', tempo_dis as Distribuição from  form_civil_semestral where iduser = ? and situacao_prev = ? and tempo_dis like ?";
                try {
                    pst = conexao.prepareStatement(sql);
                    pst.setString(1, id);
                    pst.setString(2, s);
                    pst.setString(3, data + "%");
                    rs = pst.executeQuery();
                    tblPreCivil.setModel(DbUtils.resultSetToTableModel(rs));
                    tabelaFormat2();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            }
            if (setor.equals("Refrigeração - Mensal")) {
                String s = prev;
                java.util.Date pega = cadDataPre.getDate();
                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                this.data = formato.format(pega);

                String sql = "select id_refrigeracao_mensal as 'N°', nome_prev as Técnico, setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', tempo_dis as Distribuição from  form_refrigeracao_mensal where iduser = ? and situacao_prev = ? and tempo_dis like ?";
                try {
                    pst = conexao.prepareStatement(sql);
                    pst.setString(1, id);
                    pst.setString(2, s);
                    pst.setString(3, data + "%");
                    rs = pst.executeQuery();
                    tblPreRef.setModel(DbUtils.resultSetToTableModel(rs));
                    tabelaFormat3();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            }
            if (setor.equals("Refrigeração - Trimestral")) {
                String s = prev;
                java.util.Date pega = cadDataPre.getDate();
                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                this.data = formato.format(pega);

                String sql = "select id_refrigeracao_trimestral as 'N°', nome_prev as Técnico, setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', tempo_dis as Distribuição from  form_refrigeracao_trimestral where iduser = ? and situacao_prev = ? and tempo_dis like ?";
                try {
                    pst = conexao.prepareStatement(sql);
                    pst.setString(1, id);
                    pst.setString(2, s);
                    pst.setString(3, data + "%");
                    rs = pst.executeQuery();
                    tblPreRef.setModel(DbUtils.resultSetToTableModel(rs));
                    tabelaFormat3();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            }
            if (setor.equals("Refrigeração - Semestral")) {
                String s = prev;
                java.util.Date pega = cadDataPre.getDate();
                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                this.data = formato.format(pega);

                String sql = "select id_refrigeracao_semestral as 'N°', nome_prev as Técnico, setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', tempo_dis as Distribuição from  form_refrigeracao_semestral where iduser = ? and situacao_prev = ? and tempo_dis like ?";
                try {
                    pst = conexao.prepareStatement(sql);
                    pst.setString(1, id);
                    pst.setString(2, s);
                    pst.setString(3, data + "%");
                    rs = pst.executeQuery();
                    tblPreRef.setModel(DbUtils.resultSetToTableModel(rs));
                    tabelaFormat3();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            }

        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `prev_encee`. Responsável por abrir a tela de visualização de uma
     * preventiva encerrada, com base no tipo e periodicidade selecionados (ex:
     * "Eletrica - Mensal", "Civil - Trimestral", etc.). O método identifica a
     * aba correspondente e ativa a visualização apropriada na interface
     * gráfica.
     *
     * Funcionalidades: - Obtém o valor do campo `txtTipPreIni`, que indica o
     * tipo e periodicidade da preventiva. - Compara esse valor com combinações
     * conhecidas (ex: "Eletrica - Mensal"). - Para cada caso: - Captura o ID da
     * preventiva (`txtIdPrevIni`) e armazena em uma variável específica (`id1`,
     * `id2`, etc.). - Instancia a tela correspondente (`TelaPrevEnc`,
     * `TelaPrevEnc1`, etc.). - Torna a tela visível e a adiciona ao container
     * pai. - Seleciona e habilita a aba apropriada no `JTabbedPane`. - Chama o
     * método `prev_encerradaX()` correspondente para carregar os dados. - Fecha
     * a tela atual com `dispose()`.
     *
     */
    private void prev_encee() {
        String atu;
        atu = txtTipPreIni.getText();
        try {

            if (atu.equals("Eletrica - Mensal")) {
                id1 = txtIdPrevIni.getText();
                TelaPrevEnc enc = new TelaPrevEnc();
                enc.setVisible(true);
                getParent().add(enc);
                TelaPrevEnc.jtpAbas.setSelectedIndex(0);
                TelaPrevEnc.jtpAbas.setEnabledAt(0, true);
                enc.prev_encerrada2();
                try {
                    enc.setMaximum(true);
                } catch (PropertyVetoException e) {
                    JOptionPane.showMessageDialog(null, e);
                }
                dispose();
            }
            if (atu.equals("Eletrica - Trimestral")) {
                id2 = txtIdPrevIni.getText();
                TelaPrevEnc enc = new TelaPrevEnc();
                enc.setVisible(true);
                getParent().add(enc);
                TelaPrevEnc.jtpAbas.setSelectedIndex(1);
                TelaPrevEnc.jtpAbas.setEnabledAt(1, true);
                enc.prev_encerrada1();
                try {
                    enc.setMaximum(true);
                } catch (PropertyVetoException e) {
                    JOptionPane.showMessageDialog(null, e);
                }
                dispose();
            }
            if (atu.equals("Eletrica - Semestral")) {
                id3 = txtIdPrevIni.getText();
                TelaPrevEnc enc = new TelaPrevEnc();
                enc.setVisible(true);
                getParent().add(enc);
                TelaPrevEnc.jtpAbas.setSelectedIndex(2);
                TelaPrevEnc.jtpAbas.setEnabledAt(2, true);
                enc.prev_encerrada();
                try {
                    enc.setMaximum(true);
                } catch (PropertyVetoException e) {
                    JOptionPane.showMessageDialog(null, e);
                }
                dispose();
            }
            if (atu.equals("Hidraulica - Mensal")) {
                id4 = txtIdPrevIni.getText();
                TelaPrevEnc1 enc1 = new TelaPrevEnc1();
                enc1.setVisible(true);
                getParent().add(enc1);
                TelaPrevEnc1.jtpAbas1.setSelectedIndex(0);
                TelaPrevEnc1.jtpAbas1.setEnabledAt(0, true);
                enc1.prev_encerrada3();
                try {
                    enc1.setMaximum(true);
                } catch (PropertyVetoException e) {
                    JOptionPane.showMessageDialog(null, e);
                }
                dispose();
            }
            if (atu.equals("Hidraulica - Trimestral")) {
                id5 = txtIdPrevIni.getText();
                TelaPrevEnc1 enc1 = new TelaPrevEnc1();
                enc1.setVisible(true);
                getParent().add(enc1);
                TelaPrevEnc1.jtpAbas1.setSelectedIndex(1);
                TelaPrevEnc1.jtpAbas1.setEnabledAt(1, true);
                enc1.prev_encerrada4();
                try {
                    enc1.setMaximum(true);
                } catch (PropertyVetoException e) {
                    JOptionPane.showMessageDialog(null, e);
                }
                dispose();
            }
            if (atu.equals("Hidraulica - Semestral")) {
                id6 = txtIdPrevIni.getText();
                TelaPrevEnc1 enc1 = new TelaPrevEnc1();
                enc1.setVisible(true);
                getParent().add(enc1);
                TelaPrevEnc1.jtpAbas1.setSelectedIndex(2);
                TelaPrevEnc1.jtpAbas1.setEnabledAt(2, true);
                enc1.prev_encerrada5();
                try {
                    enc1.setMaximum(true);
                } catch (PropertyVetoException e) {
                    JOptionPane.showMessageDialog(null, e);
                }
                dispose();
            }
            if (atu.equals("Civil - Mensal")) {
                id9 = txtIdPrevIni.getText();
                TelaPrevEnc2 enc2 = new TelaPrevEnc2();
                enc2.setVisible(true);
                getParent().add(enc2);
                TelaPrevEnc2.jtpAbas3.setSelectedIndex(0);
                TelaPrevEnc2.jtpAbas3.setEnabledAt(0, true);
                enc2.prev_encerrada8();
                try {
                    enc2.setMaximum(true);
                } catch (PropertyVetoException e) {
                    JOptionPane.showMessageDialog(null, e);
                }
                dispose();
            }
            if (atu.equals("Civil - Trimestral")) {
                id8 = txtIdPrevIni.getText();
                TelaPrevEnc2 enc2 = new TelaPrevEnc2();
                enc2.setVisible(true);
                getParent().add(enc2);
                TelaPrevEnc2.jtpAbas3.setSelectedIndex(1);
                TelaPrevEnc2.jtpAbas3.setEnabledAt(1, true);
                enc2.prev_encerrada7();
                try {
                    enc2.setMaximum(true);
                } catch (PropertyVetoException e) {
                    JOptionPane.showMessageDialog(null, e);
                }
                dispose();
            }
            if (atu.equals("Civil - Semestral")) {
                id7 = txtIdPrevIni.getText();
                TelaPrevEnc2 enc2 = new TelaPrevEnc2();
                enc2.setVisible(true);
                getParent().add(enc2);
                TelaPrevEnc2.jtpAbas3.setSelectedIndex(2);
                TelaPrevEnc2.jtpAbas3.setEnabledAt(2, true);
                enc2.prev_encerrada6();
                try {
                    enc2.setMaximum(true);
                } catch (PropertyVetoException e) {
                    JOptionPane.showMessageDialog(null, e);
                }
                dispose();
            }
            if (atu.equals("Refrigeração - Mensal")) {
                id10 = txtIdPrevIni.getText();
                TelaPrevEnc3 enc3 = new TelaPrevEnc3();
                enc3.setVisible(true);
                getParent().add(enc3);
                TelaPrevEnc3.jtpAbas3.setSelectedIndex(0);
                TelaPrevEnc3.jtpAbas3.setEnabledAt(0, true);
                enc3.prev_encerrada12();
                try {
                    enc3.setMaximum(true);
                } catch (PropertyVetoException e) {
                    JOptionPane.showMessageDialog(null, e);
                }
                dispose();
            }
            if (atu.equals("Refrigeração - Trimestral")) {
                id11 = txtIdPrevIni.getText();
                TelaPrevEnc3 enc3 = new TelaPrevEnc3();
                enc3.setVisible(true);
                getParent().add(enc3);
                TelaPrevEnc3.jtpAbas3.setSelectedIndex(1);
                TelaPrevEnc3.jtpAbas3.setEnabledAt(1, true);
                enc3.prev_encerrada11();
                try {
                    enc3.setMaximum(true);
                } catch (PropertyVetoException e) {
                    JOptionPane.showMessageDialog(null, e);
                }
                dispose();
            }
            if (atu.equals("Refrigeração - Semestral")) {
                id12 = txtIdPrevIni.getText();
                TelaPrevEnc3 enc3 = new TelaPrevEnc3();
                enc3.setVisible(true);
                getParent().add(enc3);
                TelaPrevEnc3.jtpAbas3.setSelectedIndex(2);
                TelaPrevEnc3.jtpAbas3.setEnabledAt(2, true);
                enc3.prev_encerrada10();
                try {
                    enc3.setMaximum(true);
                } catch (PropertyVetoException e) {
                    JOptionPane.showMessageDialog(null, e);
                }
                dispose();
            }
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método `pesq_Bot`. Habilita ou desabilita os botões de pesquisa de
     * preventivas (elétrica, hidráulica, civil e refrigeração) com base na
     * existência de registros para o usuário logado, considerando a
     * periodicidade selecionada.
     *
     * Funcionalidades: - Cria um mapa associando cada área técnica ao seu
     * respectivo botão de pesquisa. - Habilita todos os botões inicialmente. -
     * Para cada área: - Monta dinamicamente o nome da tabela com base na área e
     * na periodicidade (`si`). - Executa uma consulta `SELECT COUNT(*)` para
     * verificar se há registros com `iduser` e `situacao_prev`. - Se o total
     * for zero, desabilita o botão correspondente.
     *
     * Benefícios: - Reduz duplicação de código. - Facilita manutenção e
     * expansão (basta adicionar novas áreas ao mapa). - Melhora a legibilidade
     * e organização da lógica de verificação.
     */
    private void pesq_Bot() {

        Map<String, JRadioButton> areaBotoes = new LinkedHashMap<>();
        areaBotoes.put("eletrica", rbnPesEle);
        areaBotoes.put("hidraulica", rbnPesHid);
        areaBotoes.put("civil", rbnPesCiv);
        areaBotoes.put("refrigeracao", rbnPesRef);

        // Habilita todos inicialmente
        areaBotoes.values().forEach(botao -> botao.setEnabled(true));

        String periodicidade = si.toLowerCase(); // exemplo: mensal, trimestral, semestral

        for (Map.Entry<String, JRadioButton> entrada : areaBotoes.entrySet()) {
            String area = entrada.getKey(); // exemplo: "eletrica"
            JRadioButton botao = entrada.getValue();

            String tabela = String.format("form_%s_%s", area, periodicidade);
            String sql = String.format("SELECT COUNT(*) FROM %s WHERE iduser = ? AND situacao_prev = ?", tabela);

            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, id);
                pst.setString(2, prev);
                rs = pst.executeQuery();

                if (rs.next()) {
                    String total = rs.getString(1);
                    if ("0".equals(total)) {
                        botao.setEnabled(false);
                    }
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Erro ao verificar tabela " + tabela + ": " + e.getMessage());
            }
        }
    }

    private void limpar() {
        txtPesqEle.setText(null);
        txtPesqHid.setText(null);
        txtPesqCiv.setText(null);
        txtPesqRef.setText(null);
        txtIdPrevIni.setText(null);
        txtTipPreIni.setText(null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        cadDataPre = new com.toedter.calendar.JDateChooser();
        txtPesqEle = new javax.swing.JTextField();
        txtPesqHid = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtPesqRef = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtPesqCiv = new javax.swing.JTextField();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblPreRef = new javax.swing.JTable();
        jScrollPane15 = new javax.swing.JScrollPane();
        tblPreEle = new javax.swing.JTable();
        jScrollPane16 = new javax.swing.JScrollPane();
        tblPreCivil = new javax.swing.JTable();
        jScrollPane17 = new javax.swing.JScrollPane();
        tblPreHid = new javax.swing.JTable();
        btnIniPrev = new javax.swing.JButton();
        txtIdPrevIni = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtTipPreIni = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        rbnPreMen = new javax.swing.JRadioButton();
        rbnPreTri = new javax.swing.JRadioButton();
        rbnPreSem = new javax.swing.JRadioButton();
        jLabel10 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        rbnPesEle = new javax.swing.JRadioButton();
        rbnPesRef = new javax.swing.JRadioButton();
        rbnPesHid = new javax.swing.JRadioButton();
        rbnPesCiv = new javax.swing.JRadioButton();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        rbnPreEnc = new javax.swing.JRadioButton();
        rbnPreIrr = new javax.swing.JRadioButton();
        lblPreEnc = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Encerradas/Irregulares");
        setMaximumSize(new java.awt.Dimension(1008, 672));
        setPreferredSize(new java.awt.Dimension(1007, 672));
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameOpened(evt);
            }
        });
        getContentPane().setLayout(null);

        cadDataPre.setAutoscrolls(true);
        cadDataPre.setEnabled(false);
        cadDataPre.getDateEditor().addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if ("date".equals(e.getPropertyName())) {
                    pev_pesq();
                }
            }
        });
        getContentPane().add(cadDataPre);
        cadDataPre.setBounds(56, 124, 126, 28);

        txtPesqEle.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtPesqEle.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPesqEleKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPesqEleKeyTyped(evt);
            }
        });
        getContentPane().add(txtPesqEle);
        txtPesqEle.setBounds(60, 218, 80, 24);

        txtPesqHid.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtPesqHid.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPesqHidKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPesqHidKeyTyped(evt);
            }
        });
        getContentPane().add(txtPesqHid);
        txtPesqHid.setBounds(60, 301, 80, 24);

        jLabel3.setBackground(new java.awt.Color(0, 0, 51));
        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/9104277_search_file_folder_document_data_icon.png"))); // NOI18N
        getContentPane().add(jLabel3);
        jLabel3.setBounds(139, 298, 30, 30);

        jLabel4.setBackground(new java.awt.Color(0, 0, 51));
        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/9104277_search_file_folder_document_data_icon.png"))); // NOI18N
        jLabel4.setToolTipText("");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(139, 381, 30, 30);

        txtPesqRef.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtPesqRef.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPesqRefKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPesqRefKeyTyped(evt);
            }
        });
        getContentPane().add(txtPesqRef);
        txtPesqRef.setBounds(60, 385, 80, 24);

        jLabel5.setBackground(new java.awt.Color(0, 0, 51));
        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/9104277_search_file_folder_document_data_icon.png"))); // NOI18N
        getContentPane().add(jLabel5);
        jLabel5.setBounds(139, 463, 30, 30);

        txtPesqCiv.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtPesqCiv.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPesqCivKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPesqCivKeyTyped(evt);
            }
        });
        getContentPane().add(txtPesqCiv);
        txtPesqCiv.setBounds(60, 468, 80, 24);

        tblPreRef = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIdex, int colIndex){
                return false;
            }
        };
        tblPreRef.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tblPreRef.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "N° Preventiva", "Tècnico ", "Setor", "Equipamento", "Cod. Equipamento", "Distribuição"
            }
        ));
        tblPreRef.getTableHeader().setReorderingAllowed(false);
        tblPreRef.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPreRefMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tblPreRef);

        getContentPane().add(jScrollPane5);
        jScrollPane5.setBounds(178, 332, 766, 76);

        tblPreEle = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIdex, int colIndex){
                return false;
            }
        };
        tblPreEle.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tblPreEle.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "N° Preventiva", "Tècnico ", "Setor", "Equipamento", "Cod. Equipamento", "Distribuição"
            }
        ));
        tblPreEle.getTableHeader().setReorderingAllowed(false);
        tblPreEle.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPreEleMouseClicked(evt);
            }
        });
        jScrollPane15.setViewportView(tblPreEle);

        getContentPane().add(jScrollPane15);
        jScrollPane15.setBounds(178, 164, 766, 76);

        tblPreCivil = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIdex, int colIndex){
                return false;
            }
        };
        tblPreCivil.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tblPreCivil.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "N° Preventiva", "Tècnico ", "Setor", "Equipamento", "Cod. Equipamento", "Distribuição"
            }
        ));
        tblPreCivil.getTableHeader().setReorderingAllowed(false);
        tblPreCivil.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPreCivilMouseClicked(evt);
            }
        });
        jScrollPane16.setViewportView(tblPreCivil);

        getContentPane().add(jScrollPane16);
        jScrollPane16.setBounds(178, 416, 766, 76);

        tblPreHid = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIdex, int colIndex){
                return false;
            }
        };
        tblPreHid.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tblPreHid.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "N° Preventiva", "Tècnico ", "Setor", "Equipamento", "Cod. Equipamento", "Distribuição"
            }
        ));
        tblPreHid.getTableHeader().setReorderingAllowed(false);
        tblPreHid.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPreHidMouseClicked(evt);
            }
        });
        jScrollPane17.setViewportView(tblPreHid);

        getContentPane().add(jScrollPane17);
        jScrollPane17.setBounds(178, 248, 766, 76);

        btnIniPrev.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/48754_document_document.png"))); // NOI18N
        btnIniPrev.setToolTipText("Visualizar Preventiva");
        btnIniPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIniPrevActionPerformed(evt);
            }
        });
        getContentPane().add(btnIniPrev);
        btnIniPrev.setBounds(740, 530, 64, 64);

        txtIdPrevIni.setEditable(false);
        txtIdPrevIni.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(txtIdPrevIni);
        txtIdPrevIni.setBounds(830, 530, 70, 28);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 51));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("N°");
        getContentPane().add(jLabel6);
        jLabel6.setBounds(46, 468, 16, 24);

        txtTipPreIni.setEditable(false);
        txtTipPreIni.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        txtTipPreIni.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(txtTipPreIni);
        txtTipPreIni.setBounds(810, 574, 110, 28);

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 51));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Pesquisa por Data:");
        getContentPane().add(jLabel8);
        jLabel8.setBounds(570, 520, 120, 14);

        buttonGroup1.add(rbnPreMen);
        rbnPreMen.setOpaque(false);
        rbnPreMen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbnPreMenActionPerformed(evt);
            }
        });
        getContentPane().add(rbnPreMen);
        rbnPreMen.setBounds(194, 121, 21, 21);

        buttonGroup1.add(rbnPreTri);
        rbnPreTri.setOpaque(false);
        rbnPreTri.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbnPreTriActionPerformed(evt);
            }
        });
        getContentPane().add(rbnPreTri);
        rbnPreTri.setBounds(452, 121, 21, 21);

        buttonGroup1.add(rbnPreSem);
        rbnPreSem.setOpaque(false);
        rbnPreSem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbnPreSemActionPerformed(evt);
            }
        });
        getContentPane().add(rbnPreSem);
        rbnPreSem.setBounds(712, 121, 21, 21);

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/9104277_search_file_folder_document_data_icon.png"))); // NOI18N
        getContentPane().add(jLabel10);
        jLabel10.setBounds(138, 214, 32, 30);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 51));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Tipo Preventiva");
        getContentPane().add(jLabel9);
        jLabel9.setBounds(820, 560, 90, 14);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 51));
        jLabel7.setText("N° Preventiva");
        getContentPane().add(jLabel7);
        jLabel7.setBounds(826, 514, 80, 20);

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 0, 51));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Pesquisa por Data:");
        getContentPane().add(jLabel11);
        jLabel11.setBounds(62, 106, 116, 16);

        buttonGroup2.add(rbnPesEle);
        rbnPesEle.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        rbnPesEle.setForeground(new java.awt.Color(0, 0, 51));
        rbnPesEle.setText("Eletrica");
        rbnPesEle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbnPesEleActionPerformed(evt);
            }
        });
        getContentPane().add(rbnPesEle);
        rbnPesEle.setBounds(526, 540, 100, 23);

        buttonGroup2.add(rbnPesRef);
        rbnPesRef.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        rbnPesRef.setForeground(new java.awt.Color(0, 0, 51));
        rbnPesRef.setText("Refrigeração");
        rbnPesRef.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbnPesRefActionPerformed(evt);
            }
        });
        getContentPane().add(rbnPesRef);
        rbnPesRef.setBounds(526, 570, 100, 23);

        buttonGroup2.add(rbnPesHid);
        rbnPesHid.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        rbnPesHid.setForeground(new java.awt.Color(0, 0, 51));
        rbnPesHid.setText("Hidráulica");
        rbnPesHid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbnPesHidActionPerformed(evt);
            }
        });
        getContentPane().add(rbnPesHid);
        rbnPesHid.setBounds(630, 540, 100, 23);

        buttonGroup2.add(rbnPesCiv);
        rbnPesCiv.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        rbnPesCiv.setForeground(new java.awt.Color(0, 0, 51));
        rbnPesCiv.setText("Civil");
        rbnPesCiv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbnPesCivActionPerformed(evt);
            }
        });
        getContentPane().add(rbnPesCiv);
        rbnPesCiv.setBounds(630, 570, 100, 23);

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 0, 51));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("N°");
        getContentPane().add(jLabel12);
        jLabel12.setBounds(46, 218, 16, 24);

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 0, 51));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("N°");
        getContentPane().add(jLabel13);
        jLabel13.setBounds(46, 301, 16, 24);

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 0, 51));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("N°");
        getContentPane().add(jLabel14);
        jLabel14.setBounds(46, 385, 16, 24);

        buttonGroup3.add(rbnPreEnc);
        rbnPreEnc.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        rbnPreEnc.setForeground(new java.awt.Color(0, 0, 51));
        rbnPreEnc.setText("Encerrada");
        rbnPreEnc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbnPreEncActionPerformed(evt);
            }
        });
        getContentPane().add(rbnPreEnc);
        rbnPreEnc.setBounds(60, 520, 100, 23);

        buttonGroup3.add(rbnPreIrr);
        rbnPreIrr.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        rbnPreIrr.setForeground(new java.awt.Color(0, 0, 51));
        rbnPreIrr.setText("Irregular");
        rbnPreIrr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbnPreIrrActionPerformed(evt);
            }
        });
        getContentPane().add(rbnPreIrr);
        rbnPreIrr.setBounds(210, 520, 100, 23);

        lblPreEnc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/telas/imagens_fundo/encerradas_1.png"))); // NOI18N
        getContentPane().add(lblPreEnc);
        lblPreEnc.setBounds(-10, -8, 1008, 672);

        setBounds(0, 0, 1007, 672);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Método `btnIniPrevActionPerformed`. Este método é acionado quando o botão
     * `btnIniPrev` é clicado na interface gráfica. Ele chama o método
     * `prev_encee()`, que é responsável por abrir a tela de visualização da
     * preventiva encerrada, com base no tipo e periodicidade selecionados.
     *
     * Funcionalidades: - Atua como listener do botão de início da preventiva. -
     * Encaminha a execução para o método `prev_encee()`, que trata a lógica de
     * navegação e carregamento da tela correspondente à preventiva selecionada.
     *
     * Observações: - O método `prev_encee()` contém a lógica de roteamento para
     * diferentes tipos de preventiva (elétrica, hidráulica, civil,
     * refrigeração) e suas periodicidades (mensal, trimestral, semestral). -
     * Este método mantém a separação entre a interface gráfica e a lógica de
     * navegação.
     */
    private void btnIniPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIniPrevActionPerformed

        prev_encee();
    }//GEN-LAST:event_btnIniPrevActionPerformed

    /**
     * Método `tblPreRefMouseClicked`. Acionado quando o usuário clica em uma
     * linha da tabela `tblPreRef`, que exibe preventivas de refrigeração. Chama
     * o método `setar_ref()` para capturar os dados da linha selecionada e
     * preencher os campos da interface.
     */
    private void tblPreRefMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPreRefMouseClicked

        setar_ref();
    }//GEN-LAST:event_tblPreRefMouseClicked

    /**
     * Método `tblPreEleMouseClicked`. Acionado quando o usuário clica em uma
     * linha da tabela `tblPreEle`, que exibe preventivas elétricas. Chama o
     * método `setar_ele()` para capturar os dados da linha selecionada e
     * preencher os campos da interface.
     */
    private void tblPreEleMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPreEleMouseClicked

        setar_ele();
    }//GEN-LAST:event_tblPreEleMouseClicked

    /**
     * Método `tblPreCivilMouseClicked`. Acionado quando o usuário clica em uma
     * linha da tabela `tblPreCivil`, que exibe preventivas civis. Chama o
     * método `setar_civ()` para capturar os dados da linha selecionada e
     * preencher os campos da interface.
     */
    private void tblPreCivilMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPreCivilMouseClicked

        setar_civ();
    }//GEN-LAST:event_tblPreCivilMouseClicked
    /**
     * Método `tblPreRefMouseClicked`. Acionado quando o usuário clica em uma
     * linha da tabela `tblPreRef`, que exibe preventivas de refrigeração. Chama
     * o método `setar_ref()` para capturar os dados da linha selecionada e
     * preencher os campos da interface.
     */
    private void tblPreHidMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPreHidMouseClicked

        setar_hid();
    }//GEN-LAST:event_tblPreHidMouseClicked

    /**
     * Método `tblPreEleMouseClicked`. Acionado quando o usuário clica em uma
     * linha da tabela `tblPreEle`, que exibe preventivas elétricas. Chama o
     * método `setar_ele()` para capturar os dados da linha selecionada e
     * preencher os campos da interface.
     */
    private void rbnPreMenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbnPreMenActionPerformed

        data = null;
        si = "Mensal";
        t = "Encerrada";
        btnIniPrev.setEnabled(false);
        selec_encerrad();
        buttonGroup2.clearSelection();
        cadDataPre.setEnabled(false);
        limpar();

    }//GEN-LAST:event_rbnPreMenActionPerformed

    /**
     * Método `tblPreCivilMouseClicked`. Acionado quando o usuário clica em uma
     * linha da tabela `tblPreCivil`, que exibe preventivas civis. Chama o
     * método `setar_civ()` para capturar os dados da linha selecionada e
     * preencher os campos da interface.
     */
    private void rbnPreTriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbnPreTriActionPerformed

        si = "Trimestral";
        btnIniPrev.setEnabled(false);
        selec_encerrad();
        buttonGroup2.clearSelection();
        cadDataPre.setEnabled(false);
        limpar();
    }//GEN-LAST:event_rbnPreTriActionPerformed

    /**
     * Método `rbnPreSemActionPerformed`. Acionado quando o botão de opção
     * "Semestral" é selecionado. Define a periodicidade como "Semestral",
     * desabilita o botão de início (`btnIniPrev`) e atualiza a visualização
     * chamando `selec_encerrad()`. Também limpa os campos da interface,
     * desativa o seletor de data (`cadDataPre`) e atualiza os botões de
     * pesquisa com `pesq_Bot()`.
     *
     * Funcionalidades: - Define `si = "Semestral"` para indicar a
     * periodicidade. - Desativa o botão de início da preventiva. - Limpa a
     * seleção de outro grupo de botões (`buttonGroup2`). - Desativa o seletor
     * de data. - Limpa os campos da interface com `limpar()`. - Atualiza os
     * botões de pesquisa com base na existência de registros.
     */

    private void rbnPreSemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbnPreSemActionPerformed

        si = "Semestral";
        btnIniPrev.setEnabled(false);
        selec_encerrad();
        buttonGroup2.clearSelection();
        cadDataPre.setEnabled(false);
        limpar();
        pesq_Bot();
    }//GEN-LAST:event_rbnPreSemActionPerformed

    /**
     * Método `formInternalFrameOpened`. Executado automaticamente quando o
     * `JInternalFrame` é aberto. Inicializa o estado da interface com valores
     * padrão e configurações visuais.
     *
     * Funcionalidades: - Define `prev = "Encerrada"` e `si = "Mensal"` como
     * valores iniciais. - Seleciona os botões de opção "Mensal" e "Encerrada".
     * - Atualiza a visualização com `selec_encerrad()` e desativa o botão de
     * início. - Atualiza os botões de pesquisa com `pesq_Bot()`. - Define o
     * ícone do seletor de data (`cadDataPre`) com uma imagem personalizada. -
     * Define a cor do texto das tabelas (`tblPreEle`, `tblPreHid`,
     * `tblPreCivil`, `tblPreRef`) como azul.
     *
     * Observações: - Este método é ideal para configurar o estado inicial da
     * tela ao ser exibida. - A imagem usada no seletor de data deve estar
     * corretamente localizada no caminho especificado.
     */
    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened

        prev = "Encerrada";
        rbnPreMen.setSelected(true);
        rbnPreEnc.setSelected(true);
        si = "Mensal";
        selec_encerrad();
        btnIniPrev.setEnabled(false);
        pesq_Bot();

        ImageIcon icon = new ImageIcon(getClass().getResource("/com/prjmanutencao/icones/data.png"));
        cadDataPre.setIcon(icon);

        tblPreEle.setForeground(Color.blue);
        tblPreHid.setForeground(Color.blue);
        tblPreCivil.setForeground(Color.blue);
        tblPreRef.setForeground(Color.blue);

    }//GEN-LAST:event_formInternalFrameOpened

    /**
     * Método `txtPesqEleKeyReleased`. Acionado sempre que uma tecla é liberada
     * no campo de texto `txtPesqEle`, utilizado para pesquisar preventivas
     * elétricas. A pesquisa é executada dinamicamente com base na periodicidade
     * selecionada (`si`) e no conteúdo digitado.
     *
     * Funcionalidades: - Captura o texto atual do campo `txtPesqEle`. -
     * Verifica a periodicidade selecionada (`Mensal`, `Trimestral` ou
     * `Semestral`). - Se o campo estiver vazio: - Chama o método correspondente
     * que carrega os registros recentes (`*_men_ini`, `*_tri_ini`,
     * `*_sem_ini`). - Se houver texto: - Chama o método de pesquisa por filtro
     * (`*_men`, `*_tri`, `*_sem`).
     *
     * - **Tratamento de Erros**: - Envolve toda a lógica em um bloco
     * `try-catch` para capturar e exibir exceções inesperadas.
     *
     * Observações: - Este método permite uma experiência de busca responsiva,
     * atualizando os resultados conforme o usuário digita. - Pode ser
     * facilmente adaptado para outras áreas (hidráulica, civil, refrigeração)
     * com a mesma lógica.
     */
    private void txtPesqEleKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesqEleKeyReleased

        String v;
        v = txtPesqEle.getText();
        try {
            if (si.equals("Mensal")) {
                if (v.equals("")) {
                    ele_men_ini();
                } else {
                    ele_men();
                }
            }
            if (si.equals("Trimestral")) {
                if (v.equals("")) {
                    ele_tri_ini();
                } else {
                    ele_tri();
                }
            }
            if (si.equals("Semestral")) {
                if (v.equals("")) {
                    ele_sem_ini();
                } else {
                    ele_sem();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_txtPesqEleKeyReleased

    /**
     * Método `txtPesqEleKeyReleased`. Acionado sempre que uma tecla é liberada
     * no campo de texto `txtPesqEle`, utilizado para pesquisar preventivas
     * elétricas. A pesquisa é executada dinamicamente com base na periodicidade
     * selecionada (`si`) e no conteúdo digitado.
     *
     * Funcionalidades: - Captura o texto atual do campo `txtPesqEle`. -
     * Verifica a periodicidade selecionada (`Mensal`, `Trimestral` ou
     * `Semestral`). - Se o campo estiver vazio: - Chama o método correspondente
     * que carrega os registros recentes (`*_men_ini`, `*_tri_ini`,
     * `*_sem_ini`). - Se houver texto: - Chama o método de pesquisa por filtro
     * (`*_men`, `*_tri`, `*_sem`).
     *
     * - **Tratamento de Erros**: - Envolve toda a lógica em um bloco
     * `try-catch` para capturar e exibir exceções inesperadas.
     *
     * Observações: - Este método permite uma experiência de busca responsiva,
     * atualizando os resultados conforme o usuário digita. - Pode ser
     * facilmente adaptado para outras áreas (hidráulica, civil, refrigeração)
     * com a mesma lógica.
     */
    private void txtPesqHidKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesqHidKeyReleased

        String v;
        v = txtPesqHid.getText();
        try {
            if (si.equals("Mensal")) {
                if (v.equals("")) {
                    hid_men_ini();
                } else {
                    hid_men();
                }
            }
            if (si.equals("Trimestral")) {
                if (v.equals("")) {
                    hid_tri_ini();
                } else {
                    hid_tri();
                }
            }
            if (si.equals("Semestral")) {
                if (v.equals("")) {
                    hid_sem_ini();
                } else {
                    hid_sem();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_txtPesqHidKeyReleased

    /**
     * Método `txtPesqEleKeyReleased`. Acionado sempre que uma tecla é liberada
     * no campo de texto `txtPesqEle`, utilizado para pesquisar preventivas
     * elétricas. A pesquisa é executada dinamicamente com base na periodicidade
     * selecionada (`si`) e no conteúdo digitado.
     *
     * Funcionalidades: - Captura o texto atual do campo `txtPesqEle`. -
     * Verifica a periodicidade selecionada (`Mensal`, `Trimestral` ou
     * `Semestral`). - Se o campo estiver vazio: - Chama o método correspondente
     * que carrega os registros recentes (`*_men_ini`, `*_tri_ini`,
     * `*_sem_ini`). - Se houver texto: - Chama o método de pesquisa por filtro
     * (`*_men`, `*_tri`, `*_sem`).
     *
     * - **Tratamento de Erros**: - Envolve toda a lógica em um bloco
     * `try-catch` para capturar e exibir exceções inesperadas.
     *
     * Observações: - Este método permite uma experiência de busca responsiva,
     * atualizando os resultados conforme o usuário digita. - Pode ser
     * facilmente adaptado para outras áreas (hidráulica, civil, refrigeração)
     * com a mesma lógica.
     */
    private void txtPesqRefKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesqRefKeyReleased

        String v;
        v = txtPesqRef.getText();
        try {
            if (si.equals("Mensal")) {
                if (v.equals("")) {
                    ref_men_ini();
                } else {
                    ref_men();
                }
            }
            if (si.equals("Trimestral")) {
                if (v.equals("")) {
                    ref_tri_ini();
                } else {
                    ref_tri();
                }
            }
            if (si.equals("Semestral")) {
                if (v.equals("")) {
                    ref_sem_ini();
                } else {
                    ref_sem();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_txtPesqRefKeyReleased

    /**
     * Método `txtPesqEleKeyReleased`. Acionado sempre que uma tecla é liberada
     * no campo de texto `txtPesqEle`, utilizado para pesquisar preventivas
     * elétricas. A pesquisa é executada dinamicamente com base na periodicidade
     * selecionada (`si`) e no conteúdo digitado.
     *
     * Funcionalidades: - Captura o texto atual do campo `txtPesqEle`. -
     * Verifica a periodicidade selecionada (`Mensal`, `Trimestral` ou
     * `Semestral`). - Se o campo estiver vazio: - Chama o método correspondente
     * que carrega os registros recentes (`*_men_ini`, `*_tri_ini`,
     * `*_sem_ini`). - Se houver texto: - Chama o método de pesquisa por filtro
     * (`*_men`, `*_tri`, `*_sem`).
     *
     * - **Tratamento de Erros**: - Envolve toda a lógica em um bloco
     * `try-catch` para capturar e exibir exceções inesperadas.
     *
     * Observações: - Este método permite uma experiência de busca responsiva,
     * atualizando os resultados conforme o usuário digita. - Pode ser
     * facilmente adaptado para outras áreas (hidráulica, civil, refrigeração)
     * com a mesma lógica.
     */
    private void txtPesqCivKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesqCivKeyReleased

        String v;
        v = txtPesqCiv.getText();
        try {
            if (si.equals("Mensal")) {
                if (v.equals("")) {
                    civ_men_ini();
                } else {
                    civ_men();
                }
            }
            if (si.equals("Trimestral")) {
                if (v.equals("")) {
                    civ_tri_ini();
                } else {
                    civ_tri();
                }
            }
            if (si.equals("Semestral")) {
                if (v.equals("")) {
                    civ_sem_ini();
                } else {
                    civ_sem();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_txtPesqCivKeyReleased

    /**
     * Método `rbnPesEleActionPerformed`. Acionado quando o botão de opção de
     * pesquisa para preventivas **elétricas** é selecionado.
     *
     * Funcionalidades: - Limpa a variável `data` e redefine `setor`. - Define
     * `setor` como `"Eletrica - " + si`, onde `si` representa a periodicidade
     * selecionada (Mensal, Trimestral, etc.). - Ativa o seletor de data
     * (`cadDataPre`) para permitir filtragem por data. - Atualiza a
     * visualização chamando `selec_encerrad()`.
     */
    private void rbnPesEleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbnPesEleActionPerformed

        data = null;
        setor = null;
        setor = "Eletrica - " + si;
        cadDataPre.setEnabled(true);
        selec_encerrad();

    }//GEN-LAST:event_rbnPesEleActionPerformed

    /**
     * Método `rbnPesHidActionPerformed`. Acionado quando o botão de opção de
     * pesquisa para preventivas **hidráulicas** é selecionado.
     *
     * Funcionalidades: - Limpa a variável `data` e redefine `setor`. - Define
     * `setor` como `"Hidráulica - " + si`. - Ativa o seletor de data
     * (`cadDataPre`). - Atualiza a visualização com `selec_encerrad()`.
     */
    private void rbnPesHidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbnPesHidActionPerformed

        data = null;
        setor = null;
        setor = "Hidráulica - " + si;
        cadDataPre.setEnabled(true);
        selec_encerrad();

    }//GEN-LAST:event_rbnPesHidActionPerformed

    /**
     * Método `rbnPesRefActionPerformed`. Acionado quando o botão de opção de
     * pesquisa para preventivas **de refrigeração** é selecionado.
     *
     * Funcionalidades: - Limpa a variável `data` e redefine `setor`. - Define
     * `setor` como `"Refrigeração - " + si`. - Ativa o seletor de data
     * (`cadDataPre`). - Atualiza a visualização com `selec_encerrad()`.
     */
    private void rbnPesRefActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbnPesRefActionPerformed

        data = null;
        setor = null;
        setor = "Refrigeração - " + si;
        cadDataPre.setEnabled(true);
        selec_encerrad();

    }//GEN-LAST:event_rbnPesRefActionPerformed

    /**
     * Método `rbnPesCivActionPerformed`. Acionado quando o botão de opção de
     * pesquisa para preventivas **civis** é selecionado.
     *
     * Funcionalidades: - Limpa a variável `data` e redefine `setor`. - Define
     * `setor` como `"Civil - " + si`, onde `si` representa a periodicidade
     * atual (Mensal, Trimestral, etc.). - Ativa o seletor de data
     * (`cadDataPre`) para permitir filtragem por data. - Atualiza a
     * visualização com `selec_encerrad()`.
     */
    private void rbnPesCivActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbnPesCivActionPerformed

        data = null;
        setor = null;
        setor = "Civil - " + si;
        cadDataPre.setEnabled(true);
        selec_encerrad();
    }//GEN-LAST:event_rbnPesCivActionPerformed

    /**
     * Método `rbnPreEncActionPerformed`. Acionado quando o botão de opção
     * "Encerrada" é selecionado.
     *
     * Funcionalidades: - Define a periodicidade como `"Mensal"` e o tipo de
     * preventiva como `"Encerrada"`. - Desativa o botão de início da preventiva
     * (`btnIniPrev`). - Chama o método `inicializacao()` para carregar os dados
     * iniciais. - Limpa a seleção de outro grupo de botões (`buttonGroup2`). -
     * Desativa o seletor de data (`cadDataPre`).
     */
    private void rbnPreEncActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbnPreEncActionPerformed

        si = "Mensal";
        t = "Encerrada";
        btnIniPrev.setEnabled(false);
        inicializacao();
        buttonGroup2.clearSelection();
        cadDataPre.setEnabled(false);
    }//GEN-LAST:event_rbnPreEncActionPerformed

    /**
     * Método `rbnPreIrrActionPerformed`. Acionado quando o botão de opção
     * "Irregular" é selecionado.
     *
     * Funcionalidades: - Define a periodicidade como `"Mensal"` e o tipo de
     * preventiva como `"Irregular"`. - Desativa o botão de início da preventiva
     * (`btnIniPrev`). - Chama o método `inicializacao()` para carregar os dados
     * iniciais. - Limpa a seleção de outro grupo de botões (`buttonGroup2`). -
     * Desativa o seletor de data (`cadDataPre`).
     */
    private void rbnPreIrrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbnPreIrrActionPerformed

        si = "Mensal";
        t = "Irregular";
        btnIniPrev.setEnabled(false);
        inicializacao();
        buttonGroup2.clearSelection();
        cadDataPre.setEnabled(false);
    }//GEN-LAST:event_rbnPreIrrActionPerformed

    /**
     * Método `txtPesqEleKeyTyped`. Acionado sempre que o usuário digita um
     * caractere no campo de texto `txtPesqEle`. Este método atua como um filtro
     * de entrada, permitindo apenas letras (maiúsculas e minúsculas), números,
     * o caractere especial "°" e espaço.
     *
     * Funcionalidades: - Define uma string `caracter` contendo todos os
     * caracteres permitidos. - Verifica se o caractere digitado
     * (`evt.getKeyChar()`) está contido nessa string. - Caso não esteja, o
     * evento é consumido com `evt.consume()`, impedindo que o caractere seja
     * inserido no campo.
     *
     * Observações: - O filtro é útil para evitar a entrada de símbolos
     * indesejados ou inválidos durante a digitação. - A linha comentada com
     * `Toolkit.getDefaultToolkit().beep();` pode ser ativada para emitir um som
     * ao bloquear a entrada. - Pode ser facilmente adaptado para aceitar outros
     * conjuntos de caracteres conforme a necessidade.
     */
    private void txtPesqEleKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesqEleKeyTyped

        // filtro que só permite a inclusão de letras minusculas e numeros e
        //caracteres especiais na caixa de texto:
        String caracter = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789° ";
        if (!caracter.contains(evt.getKeyChar() + "")) {
            evt.consume();
//            Toolkit.getDefaultToolkit().beep();
        }
    }//GEN-LAST:event_txtPesqEleKeyTyped

    /**
     * Método `txtPesqHidKeyTyped`. Acionado sempre que o usuário digita um
     * caractere no campo de texto `txtPesqEle`. Este método atua como um filtro
     * de entrada, permitindo apenas letras (maiúsculas e minúsculas), números,
     * o caractere especial "°" e espaço.
     *
     * Funcionalidades: - Define uma string `caracter` contendo todos os
     * caracteres permitidos. - Verifica se o caractere digitado
     * (`evt.getKeyChar()`) está contido nessa string. - Caso não esteja, o
     * evento é consumido com `evt.consume()`, impedindo que o caractere seja
     * inserido no campo.
     *
     * Observações: - O filtro é útil para evitar a entrada de símbolos
     * indesejados ou inválidos durante a digitação. - A linha comentada com
     * `Toolkit.getDefaultToolkit().beep();` pode ser ativada para emitir um som
     * ao bloquear a entrada. - Pode ser facilmente adaptado para aceitar outros
     * conjuntos de caracteres conforme a necessidade.
     */
    private void txtPesqHidKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesqHidKeyTyped

        // filtro que só permite a inclusão de letras minusculas e numeros e
        //caracteres especiais na caixa de texto:
        String caracter = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789° ";
        if (!caracter.contains(evt.getKeyChar() + "")) {
            evt.consume();
//            Toolkit.getDefaultToolkit().beep();
        }
    }//GEN-LAST:event_txtPesqHidKeyTyped

    /**
     * Método `txtPesqRefKeyTyped`. Acionado sempre que o usuário digita um
     * caractere no campo de texto `txtPesqEle`. Este método atua como um filtro
     * de entrada, permitindo apenas letras (maiúsculas e minúsculas), números,
     * o caractere especial "°" e espaço.
     *
     * Funcionalidades: - Define uma string `caracter` contendo todos os
     * caracteres permitidos. - Verifica se o caractere digitado
     * (`evt.getKeyChar()`) está contido nessa string. - Caso não esteja, o
     * evento é consumido com `evt.consume()`, impedindo que o caractere seja
     * inserido no campo.
     *
     * Observações: - O filtro é útil para evitar a entrada de símbolos
     * indesejados ou inválidos durante a digitação. - A linha comentada com
     * `Toolkit.getDefaultToolkit().beep();` pode ser ativada para emitir um som
     * ao bloquear a entrada. - Pode ser facilmente adaptado para aceitar outros
     * conjuntos de caracteres conforme a necessidade.
     */
    private void txtPesqRefKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesqRefKeyTyped

        // filtro que só permite a inclusão de letras minusculas e numeros e
        //caracteres especiais na caixa de texto:
        String caracter = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789° ";
        if (!caracter.contains(evt.getKeyChar() + "")) {
            evt.consume();
//            Toolkit.getDefaultToolkit().beep();
        }
    }//GEN-LAST:event_txtPesqRefKeyTyped

    /**
     * Método `txtPesqCivKeyTyped`. Acionado sempre que o usuário digita um
     * caractere no campo de texto `txtPesqEle`. Este método atua como um filtro
     * de entrada, permitindo apenas letras (maiúsculas e minúsculas), números,
     * o caractere especial "°" e espaço.
     *
     * Funcionalidades: - Define uma string `caracter` contendo todos os
     * caracteres permitidos. - Verifica se o caractere digitado
     * (`evt.getKeyChar()`) está contido nessa string. - Caso não esteja, o
     * evento é consumido com `evt.consume()`, impedindo que o caractere seja
     * inserido no campo.
     *
     * Observações: - O filtro é útil para evitar a entrada de símbolos
     * indesejados ou inválidos durante a digitação. - A linha comentada com
     * `Toolkit.getDefaultToolkit().beep();` pode ser ativada para emitir um som
     * ao bloquear a entrada. - Pode ser facilmente adaptado para aceitar outros
     * conjuntos de caracteres conforme a necessidade.
     */
    private void txtPesqCivKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesqCivKeyTyped

        // filtro que só permite a inclusão de letras minusculas e numeros e
        //caracteres especiais na caixa de texto:
        String caracter = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789° ";
        if (!caracter.contains(evt.getKeyChar() + "")) {
            evt.consume();
//            Toolkit.getDefaultToolkit().beep();
        }
    }//GEN-LAST:event_txtPesqCivKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnIniPrev;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private com.toedter.calendar.JDateChooser cadDataPre;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JLabel lblPreEnc;
    private javax.swing.JRadioButton rbnPesCiv;
    private javax.swing.JRadioButton rbnPesEle;
    private javax.swing.JRadioButton rbnPesHid;
    private javax.swing.JRadioButton rbnPesRef;
    private javax.swing.JRadioButton rbnPreEnc;
    private javax.swing.JRadioButton rbnPreIrr;
    private javax.swing.JRadioButton rbnPreMen;
    private javax.swing.JRadioButton rbnPreSem;
    private javax.swing.JRadioButton rbnPreTri;
    public static javax.swing.JTable tblPreCivil;
    public static javax.swing.JTable tblPreEle;
    public static javax.swing.JTable tblPreHid;
    public static javax.swing.JTable tblPreRef;
    private javax.swing.JTextField txtIdPrevIni;
    private javax.swing.JTextField txtPesqCiv;
    private javax.swing.JTextField txtPesqEle;
    private javax.swing.JTextField txtPesqHid;
    private javax.swing.JTextField txtPesqRef;
    private javax.swing.JTextField txtTipPreIni;
    // End of variables declaration//GEN-END:variables
}
