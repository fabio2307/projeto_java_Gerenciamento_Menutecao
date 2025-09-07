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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.sql.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import net.proteanit.sql.DbUtils;

/**
 * Realiza tanto o cadastro a correção no cadastro de preventivas no nome do
 * técnico escolhido.
 *
 * @author Fábio S. Oliveira
 * @version 1.1
 */
public class TelaDisPreventiva extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    public static String tipo = null;
    public static String idprev = null;
    String va, va1, va2 = null;
    int v = 0;
    String preve;

    StringBuffer buffer, buffer1;

    /**
     * Construtor `TelaDisPreventiva`. Este método inicializa a interface da
     * tela de distribuição preventiva (`TelaDisPreventiva`), garantindo que
     * seus componentes gráficos sejam configurados corretamente e estabelecendo
     * a conexão com o banco de dados. Ele é chamado automaticamente ao criar
     * uma nova instância da classe `TelaDisPreventiva`.
     *
     * Funcionalidades: - **Inicialização de Componentes Gráficos**: -
     * `initComponents()`: Método gerado pelo ambiente de desenvolvimento que
     * configura os elementos da interface gráfica, como botões, tabelas, caixas
     * de texto e labels.
     *
     * - **Estabelecimento da Conexão com o Banco de Dados**: - `conexao =
     * ModuloConexao.conector()`: Usa um método específico para conectar ao
     * banco de dados, garantindo que a tela possa manipular e consultar dados
     * conforme necessário.
     *
     * Fluxo do Método: 1. Inicializa os componentes visuais da tela. 2.
     * Estabelece a conexão com o banco de dados.
     */
    public TelaDisPreventiva() {
        initComponents();
        conexao = ModuloConexao.conector();

    }

    /**
     * Método `formTabela`. Este método ajusta dinamicamente a exibição da
     * tabela `tblEquiSet` com base na variável `preve`, que pode representar
     * diferentes categorias como "setor", "equipamento" ou "tecnico". Ele
     * configura as larguras das colunas, alinha os dados e personaliza a
     * exibição do cabeçalho da tabela. Além disso, ajusta a posição e tamanho
     * do rótulo `lblPesqGeral` conforme a categoria selecionada.
     *
     * Funcionalidades: - **Configuração do Rótulo `lblPesqGeral`**: - Define a
     * posição, tamanho e texto do rótulo com base na categoria selecionada.
     *
     * - **Ajuste de Colunas**: - Define diferentes larguras para as colunas
     * dependendo da categoria (`setor`, `equipamento`, `tecnico`).
     *
     * - **Alinhamento do Cabeçalho**: - Obtém o cabeçalho da tabela e ajusta
     * seu alinhamento para centralizado. - Desativa o redimensionamento
     * automático das colunas (`AUTO_RESIZE_OFF`).
     *
     * - **Alinhamento do Conteúdo das Células**: - Cria um renderizador
     * (`DefaultTableCellRenderer`) para centralizar o texto dentro das células.
     * - Aplica o renderizador a todas as colunas da tabela.
     *
     * - **Tratamento de Exceções**: - Captura possíveis erros e exibe uma
     * mensagem amigável ao usuário em caso de falha ao alinhar a tabela.
     *
     * Fluxo do Método: 1. Verifica o valor de `preve` e ajusta a configuração
     * da tabela conforme a categoria. 2. Modifica a posição, tamanho e texto do
     * rótulo `lblPesqGeral`. 3. Obtém e ajusta visualmente o cabeçalho da
     * tabela. 4. Define larguras específicas para cada coluna. 5. Aplica o
     * alinhamento centralizado às células da tabela. 6. Captura possíveis erros
     * e exibe uma mensagem ao usuário.
     */
    private void formTabela() {

        JTableHeader header;

        try {
            if (preve.equals("setor")) {
                int width = 90, width1 = 179, width2 = 310;
                lblPesqGeral.setLocation(279, 158);
                lblPesqGeral.setSize(new Dimension(76, 28));
                lblPesqGeral.setText("Setor");

                header = tblEquiSet.getTableHeader();
                DefaultTableCellRenderer centralizado = (DefaultTableCellRenderer) header.getDefaultRenderer();
                centralizado.setHorizontalAlignment(SwingConstants.CENTER);
                tblEquiSet.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
                tblEquiSet.getColumnModel().getColumn(0).setPreferredWidth(width);
                tblEquiSet.getColumnModel().getColumn(1).setPreferredWidth(width1);
                tblEquiSet.getColumnModel().getColumn(2).setPreferredWidth(width2);

                DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
                centerRenderer.setHorizontalAlignment(JLabel.CENTER);
//      onde está zero pode ser alterado para a coluna desejada
                for (int i = 0; i < tblEquiSet.getColumnCount(); i++) {
                    tblEquiSet.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);

                }

            }
            if (preve.equals("equipamento")) {
                int width = 90, width1 = 220, width2 = 90, width3 = 179;
                lblPesqGeral.setLocation(244, 158);
                lblPesqGeral.setSize(new Dimension(110, 28));
                lblPesqGeral.setText("Equipamento");

                header = tblEquiSet.getTableHeader();
                DefaultTableCellRenderer centralizado = (DefaultTableCellRenderer) header.getDefaultRenderer();
                centralizado.setHorizontalAlignment(SwingConstants.CENTER);
                tblEquiSet.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
                tblEquiSet.getColumnModel().getColumn(0).setPreferredWidth(width);
                tblEquiSet.getColumnModel().getColumn(1).setPreferredWidth(width1);
                tblEquiSet.getColumnModel().getColumn(2).setPreferredWidth(width2);
                tblEquiSet.getColumnModel().getColumn(3).setPreferredWidth(width3);

                DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
                centerRenderer.setHorizontalAlignment(JLabel.CENTER);
//      onde está zero pode ser alterado para a coluna desejada
                for (int i = 0; i < tblEquiSet.getColumnCount(); i++) {
                    tblEquiSet.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);

                }
            }
            if (preve.equals("tecnico")) {
                int width = 90, width1 = 229, width2 = 260;
                lblPesqGeral.setLocation(274, 158);
                lblPesqGeral.setSize(new Dimension(80, 28));
                lblPesqGeral.setText("Técnico");

                header = tblEquiSet.getTableHeader();
                DefaultTableCellRenderer centralizado = (DefaultTableCellRenderer) header.getDefaultRenderer();
                centralizado.setHorizontalAlignment(SwingConstants.CENTER);
                tblEquiSet.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
                tblEquiSet.getColumnModel().getColumn(0).setPreferredWidth(width);
                tblEquiSet.getColumnModel().getColumn(1).setPreferredWidth(width1);
                tblEquiSet.getColumnModel().getColumn(2).setPreferredWidth(width2);

                DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
                centerRenderer.setHorizontalAlignment(JLabel.CENTER);
//      onde está zero pode ser alterado para a coluna desejada
                for (int i = 0; i < tblEquiSet.getColumnCount(); i++) {
                    tblEquiSet.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao alinhar as tabelas" + "/n" + e);
        }

    }

    /**
     * Método `pesquisarSetorIni`. Este método executa uma consulta SQL para
     * recuperar e exibir os dados dos setores cadastrados no banco de dados.
     * Ele preenche a tabela `tblEquiSet` com as informações do setor, incluindo
     * código, andar e nome do setor.
     *
     * Funcionalidades: - **Montagem da Query SQL**: - Recupera três atributos
     * dos setores: - `id_setor` (código do setor). - `andar` (localização do
     * setor). - `setor` (nome do setor).
     *
     * - **Execução da Consulta**: - Utiliza `PreparedStatement` para evitar
     * vulnerabilidades e melhorar o desempenho. - Executa a consulta com
     * `executeQuery()`, retornando os resultados como um conjunto de registros
     * (`ResultSet`).
     *
     * - **Atualização da Tabela**: - Usa `DbUtils.resultSetToTableModel(rs)`
     * para converter os resultados da consulta em um modelo de tabela, exibindo
     * os dados na interface.
     *
     * - **Tratamento de Exceções**: - Captura falhas na execução da consulta
     * SQL (`SQLException`) e exibe mensagens amigáveis ao usuário.
     *
     * Fluxo do Método: 1. Define a consulta SQL para recuperar os dados dos
     * setores. 2. Prepara e executa a consulta, obtendo os resultados. 3.
     * Atualiza a tabela `tblEquiSet` com os registros retornados. 4. Captura e
     * trata possíveis erros.
     */
    private void pesquisarSetorIni() {
        String sql = "select id_setor as 'Cod. Setor', andar as 'Andar', setor as 'Setor' from setor";
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            tblEquiSet.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `pesquisarTecnicoIni`. Este método realiza uma consulta ao banco
     * de dados para obter informações sobre técnicos da área de manutenção. Ele
     * exibe os resultados na tabela `tblEquiSet`, apresentando o registro de
     * empregado (`R.E`), nome do técnico e sua especialidade.
     *
     * Funcionalidades: - **Definição da Área**: - Filtra os técnicos que
     * pertencem à área de "Manutenção".
     *
     * - **Montagem e Execução da Query SQL**: - Recupera os seguintes atributos
     * dos técnicos: - `iduser` (Registro de Empregado - R.E.). - `nome_usuario`
     * (Nome do técnico). - `tipo_usuario` (Especialidade do técnico). - Filtra
     * os resultados com `WHERE area_usuario = ?` para garantir que apenas
     * técnicos de manutenção sejam incluídos.
     *
     * - **Exibição dos Resultados**: - Usa `DbUtils.resultSetToTableModel(rs)`
     * para converter os resultados em um modelo de tabela e exibir na
     * interface.
     *
     * - **Tratamento de Exceções**: - Captura falhas na execução da consulta
     * SQL (`SQLException`) e exibe mensagens amigáveis ao usuário.
     *
     * Fluxo do Método: 1. Define a área "Manutenção" como critério de
     * filtragem. 2. Prepara e executa a consulta SQL para buscar técnicos dessa
     * área. 3. Atualiza a tabela `tblEquiSet` com os registros obtidos. 4.
     * Captura e trata possíveis erros.
     */
    private void pesquisarTecnicoIni() {

        String area = "Manutenção";

        String sql = " select iduser as 'R.E', nome_usuario as 'Técnico', tipo_usuario as 'Especialidade' from usuarios where area_usuario = ? and tipo_usuario like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, area);
            pst.setString(2, "%".concat(txtTipEqui.getText()).split(" ")[0].concat("%"));
            rs = pst.executeQuery();
            tblEquiSet.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `pesquisarEquipamentoIni`. Este método realiza uma consulta ao
     * banco de dados para recuperar informações sobre equipamentos de um setor
     * específico. Ele exibe os resultados na tabela `tblEquiSet`, apresentando
     * o número do equipamento, seu nome, código e tipo.
     *
     * Funcionalidades: - **Montagem da Query SQL**: - Recupera os seguintes
     * atributos dos equipamentos: - `id_equi_set` (Número do equipamento). -
     * `nome_equi_set` (Nome do equipamento). - `cod_equi_set` (Código do
     * equipamento). - `tipo_equip` (Tipo do equipamento). - Filtra os
     * equipamentos pelo setor associado (`WHERE id_setor = ?`).
     *
     * - **Execução da Consulta**: - Utiliza `PreparedStatement` para evitar
     * vulnerabilidades e melhorar o desempenho. - O valor do setor é obtido a
     * partir do campo de texto `txtIdSetPrev` e passado como parâmetro na
     * consulta.
     *
     * - **Exibição dos Resultados**: - Usa `DbUtils.resultSetToTableModel(rs)`
     * para converter os resultados em um modelo de tabela e exibir os dados na
     * interface.
     *
     * - **Tratamento de Exceções**: - Captura falhas na execução da consulta
     * SQL (`SQLException`) e exibe mensagens amigáveis ao usuário.
     *
     * Fluxo do Método: 1. Define a consulta SQL para recuperar os equipamentos
     * do setor selecionado. 2. Obtém o valor do setor do campo `txtIdSetPrev` e
     * define como parâmetro na consulta. 3. Executa a consulta e obtém os
     * resultados. 4. Atualiza a tabela `tblEquiSet` com os registros
     * retornados. 5. Captura e trata possíveis erros.
     */
    private void pesquisarEquipamentoIni() {

        String sql = "select id_equi_set as 'N°', nome_equi_set as 'Equipamento',  cod_equi_set as 'Código', tipo_equip as 'Tipo' from equipamento_setor where id_setor = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtIdSetPrev.getText());
            rs = pst.executeQuery();
            tblEquiSet.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método `pesquisar_prev`. Este método realiza uma busca no banco de dados
     * para encontrar setores que correspondam ao texto inserido pelo usuário.
     * Ele retorna setores filtrados com base no andar ou nome do setor e exibe
     * os resultados na tabela `tblEquiSet`.
     *
     * Funcionalidades: - **Montagem da Query SQL**: - Recupera três atributos
     * dos setores: - `id_setor` (Código do setor). - `andar` (Localização do
     * setor). - `setor` (Nome do setor). - Usa `LIKE` para buscar setores que
     * tenham um andar ou nome parcialmente correspondente ao texto inserido.
     *
     * - **Execução da Consulta**: - Utiliza `PreparedStatement` para evitar
     * vulnerabilidades e melhorar a eficiência da busca. - Obtém o texto
     * digitado em `txtPesEqui` e adiciona o operador `%` para permitir buscas
     * parciais.
     *
     * - **Exibição dos Resultados**: - Usa `DbUtils.resultSetToTableModel(rs)`
     * para exibir os registros na tabela `tblEquiSet`.
     *
     * - **Tratamento de Erros**: - Captura falhas na execução da consulta SQL
     * (`SQLException`) e exibe mensagens amigáveis ao usuário.
     *
     * Fluxo do Método: 1. Define a consulta SQL para recuperar setores que
     * correspondam ao texto digitado. 2. Obtém o texto de busca e aplica
     * filtros usando `LIKE`. 3. Executa a consulta e recupera os resultados. 4.
     * Atualiza a tabela `tblEquiSet` com os setores encontrados. 5. Captura e
     * trata possíveis erros.
     */
    private void pesquisar_prev() {
        String sql = "select id_setor as 'Cod. Setor', andar as 'Andar', setor as 'Setor' from setor where andar like ? or setor like ? ";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtPesEqui.getText() + "%");
            pst.setString(2, txtPesEqui.getText() + "%");
            rs = pst.executeQuery();
            tblEquiSet.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `setar_campos_prev`. Este método captura os dados do setor
     * selecionado na tabela `tblEquiSet` e os exibe nos campos apropriados da
     * interface. Ele também ajusta a borda dos campos para manter um estilo
     * visual uniforme e chama `pesquisarSetorIni()` para carregar informações
     * complementares sobre o setor escolhido.
     *
     * Funcionalidades: - **Obtenção de Dados do Setor Selecionado**: - Obtém o
     * índice da linha selecionada com `getSelectedRow()`. - Define
     * `txtIdSetPrev` com o ID do setor (`id_setor`). - Define `txtSetPrev`
     * combinando o nome e o andar do setor (`setor` + `andar`).
     *
     * - **Melhoria Visual**: - Ajusta a borda dos campos `txtIdSetPrev` e
     * `txtSetPrev` para manter a padronização da interface.
     *
     * - **Atualização de Dados Complementares**: - Chama `pesquisarSetorIni()`
     * para buscar informações adicionais sobre o setor.
     *
     * Fluxo do Método: 1. Obtém a linha selecionada na tabela. 2. Recupera e
     * exibe os dados do setor nos campos correspondentes. 3. Ajusta visualmente
     * os campos. 4. Executa uma pesquisa complementar sobre o setor.
     */
    private void setar_campos_prev() {
        int setar = tblEquiSet.getSelectedRow();
        txtIdSetPrev.setText(tblEquiSet.getModel().getValueAt(setar, 0).toString());
        txtSetPrev.setText(tblEquiSet.getModel().getValueAt(setar, 1).toString() + " " + (tblEquiSet.getModel().getValueAt(setar, 2).toString()));
        txtIdSetPrev.setBorder(new LineBorder(new Color(240, 240, 240)));
        txtSetPrev.setBorder(new LineBorder(new Color(240, 240, 240)));
        pesquisarSetorIni();
    }

    /**
     * Método `pesquisar_tecnico_prev`. Este método realiza uma busca no banco
     * de dados para encontrar técnicos da área de manutenção com base no texto
     * digitado pelo usuário. Ele permite a pesquisa por Registro de Empregado
     * (`R.E.`) ou pelo nome do técnico, e exibe os resultados na tabela
     * `tblEquiSet`.
     *
     * Funcionalidades: - **Filtragem Específica**: - Busca apenas técnicos
     * pertencentes à área "Manutenção". - Permite pesquisa por `iduser` (R.E.)
     * ou `nome_usuario` (Nome do técnico).
     *
     * - **Montagem e Execução da Query SQL**: - Recupera os seguintes atributos
     * dos técnicos: - `iduser` (Registro de Empregado - R.E.). - `nome_usuario`
     * (Nome do técnico). - `tipo_usuario` (Especialidade do técnico). - Usa
     * `LIKE` para encontrar registros que contenham parcialmente o texto
     * digitado pelo usuário. - Aplica a condição `OR` para permitir pesquisa
     * tanto por `R.E.` quanto pelo nome do técnico.
     *
     * - **Exibição dos Resultados**: - Usa `DbUtils.resultSetToTableModel(rs)`
     * para converter os resultados em um modelo de tabela e exibi-los na
     * interface.
     *
     * - **Tratamento de Erros**: - Captura falhas na execução da consulta SQL
     * (`SQLException`) e exibe mensagens amigáveis ao usuário.
     *
     * Fluxo do Método: 1. Define a área "Manutenção" como critério de
     * filtragem. 2. Obtém o texto digitado pelo usuário para pesquisa. 3.
     * Prepara e executa a consulta SQL utilizando `LIKE` para pesquisa parcial.
     * 4. Atualiza a tabela `tblEquiSet` com os registros encontrados. 5.
     * Captura e trata possíveis erros.
     */
    private void pesquisar_tecnico_prev() {

        String area = "Manutenção";

        String sql = " select iduser as 'R.E', nome_usuario as 'Técnico', tipo_usuario as 'Especialidade' from usuarios where iduser like ? and area_usuario = ? and tipo_usuario like ? or nome_usuario like? and area_usuario = ? and tipo_usuario like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtPesEqui.getText() + "%");
            pst.setString(2, area);
            pst.setString(3, "%".concat(txtTipEqui.getText()).split(" ")[0].concat("%"));
            pst.setString(4, txtPesEqui.getText() + "%");
            pst.setString(5, area);
            pst.setString(6, "%".concat(txtTipEqui.getText()).split(" ")[0].concat("%"));
            rs = pst.executeQuery();
            tblEquiSet.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `setar_tecnico_prev`. Este método captura os dados do técnico
     * selecionado na tabela `tblEquiSet` e os exibe nos campos de texto
     * correspondentes. Ele também chama o método `pesquisarEquipamentoIni()`
     * para atualizar os equipamentos relacionados ao técnico escolhido. Além
     * disso, ajusta a borda dos campos de entrada para garantir um estilo
     * uniforme na interface.
     *
     * Funcionalidades: - **Obtenção de Dados Selecionados**: - Recupera a linha
     * selecionada pelo usuário usando `getSelectedRow()`. - Obtém e exibe os
     * valores correspondentes do técnico nos seguintes campos: -
     * `txtIdTecPrev`: Código do técnico (`iduser`). - `txtTecPrev`: Nome do
     * técnico (`nome_usuario`). - `txtTipTec`: Especialidade do técnico
     * (`tipo_usuario`).
     *
     * - **Atualização de Equipamentos Relacionados**: - Chama
     * `pesquisarEquipamentoIni()` para exibir os equipamentos associados ao
     * técnico selecionado.
     *
     * - **Ajuste da Interface**: - Define a borda dos campos `txtIdTecPrev`,
     * `txtTipTec` e `txtTecPrev` para uma cor uniforme (`LineBorder` de cor
     * cinza claro).
     *
     * - **Opções para Melhorias Futuras**: - Linhas comentadas (`TODO`) indicam
     * funcionalidades adicionais que podem ser implementadas, como: - Limpeza
     * da tabela (`setRowCount(0)`). - Reset do campo de pesquisa
     * (`txtPesTecPrev.setText(null)`).
     *
     * Fluxo do Método: 1. Obtém a linha selecionada na tabela `tblEquiSet`. 2.
     * Recupera e exibe os dados do técnico selecionado nos campos
     * correspondentes. 3. Chama `pesquisarEquipamentoIni()` para carregar os
     * equipamentos do técnico. 4. Ajusta a aparência dos campos de entrada.
     */
    private void setar_tecnico_prev() {
        int setar = tblEquiSet.getSelectedRow();
        txtIdTecPrev.setText(tblEquiSet.getModel().getValueAt(setar, 0).toString());
        txtTecPrev.setText(tblEquiSet.getModel().getValueAt(setar, 1).toString());
        txtTipTec.setText(tblEquiSet.getModel().getValueAt(setar, 2).toString());
        pesquisarEquipamentoIni();
//        ((DefaultTableModel) tblEquiSet.getModel()).setRowCount(0);
//        txtPesTecPrev.setText(null);
        txtIdTecPrev.setBorder(new LineBorder(new Color(240, 240, 240)));
        txtTipTec.setBorder(new LineBorder(new Color(240, 240, 240)));
        txtTecPrev.setBorder(new LineBorder(new Color(240, 240, 240)));
    }

    /**
     * Método `pesquisar_equipamento`. Este método realiza uma busca no banco de
     * dados para encontrar equipamentos que correspondam ao texto digitado pelo
     * usuário. Ele permite a pesquisa por número do equipamento
     * (`id_equi_set`), código do equipamento (`cod_equi_set`) ou nome do
     * equipamento (`nome_equi_set`), garantindo que os resultados sejam
     * filtrados pelo setor especificado (`id_setor`).
     *
     * Funcionalidades: - **Filtragem Específica**: - Retorna apenas
     * equipamentos pertencentes ao setor indicado pelo usuário. - Permite
     * pesquisa por três critérios distintos: - `id_equi_set` (Número do
     * equipamento). - `cod_equi_set` (Código do equipamento). - `nome_equi_set`
     * (Nome do equipamento).
     *
     * - **Montagem e Execução da Query SQL**: - Usa `LIKE` para encontrar
     * registros que contenham parcialmente o texto digitado pelo usuário. -
     * Aplica condições `OR` para permitir pesquisa por qualquer um dos três
     * critérios dentro do setor.
     *
     * - **Exibição dos Resultados**: - Usa `DbUtils.resultSetToTableModel(rs)`
     * para converter os resultados em um modelo de tabela e exibi-los na
     * interface.
     *
     * - **Tratamento de Erros**: - Captura falhas na execução da consulta SQL
     * (`SQLException`) e exibe mensagens amigáveis ao usuário.
     *
     * Fluxo do Método: 1. Define a consulta SQL para buscar equipamentos por
     * número, código ou nome dentro do setor. 2. Obtém o texto digitado pelo
     * usuário para pesquisa. 3. Prepara e executa a consulta SQL utilizando
     * `LIKE` para pesquisa parcial. 4. Atualiza a tabela `tblEquiSet` com os
     * equipamentos encontrados. 5. Captura e trata possíveis erros.
     */
    private void pesquisar_equipamento() {

        String sql = "select id_equi_set as 'N°', nome_equi_set as 'Equipamento',  cod_equi_set as 'Código', tipo_equip as 'Tipo' from equipamento_setor where  id_equi_set like ? and id_setor = ? or cod_equi_set like ? and id_setor = ? or nome_equi_set like ? and id_setor = ?";
        try {
            pst = conexao.prepareStatement(sql);

            pst.setString(1, txtPesEqui.getText() + "%");
            pst.setString(2, txtIdSetPrev.getText());
            pst.setString(3, txtPesEqui.getText() + "%");
            pst.setString(4, txtIdSetPrev.getText());
            pst.setString(5, txtPesEqui.getText() + "%");
            pst.setString(6, txtIdSetPrev.getText());
            rs = pst.executeQuery();
            tblEquiSet.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método `setar_equipamento`. Este método captura os dados do equipamento
     * selecionado na tabela `tblEquiSet` e os exibe nos campos de texto
     * correspondentes. Ele também chama `pesquisarEquipamentoIni()` para
     * atualizar os equipamentos relacionados ao setor do item selecionado. Além
     * disso, ajusta a borda dos campos de entrada para garantir um estilo
     * uniforme na interface.
     *
     * Funcionalidades: - **Obtenção de Dados Selecionados**: - Recupera a linha
     * selecionada pelo usuário usando `getSelectedRow()`. - Obtém e exibe os
     * valores correspondentes do equipamento nos seguintes campos: -
     * `txtIdEquiSet`: Número do equipamento (`id_equi_set`). - `txtNonEquiSet`:
     * Nome do equipamento (`nome_equi_set`). - `txtCodEquiSet`: Código do
     * equipamento (`cod_equi_set`). - `txtTipEqui`: Tipo do equipamento
     * (`tipo_equip`).
     *
     * - **Atualização de Equipamentos Relacionados**: - Chama
     * `pesquisarEquipamentoIni()` para exibir os equipamentos associados ao
     * setor do item selecionado.
     *
     * - **Ajuste da Interface**: - Define a borda dos campos `txtIdEquiSet`,
     * `txtCodEquiSet`, `txtNonEquiSet` e `txtTipEqui` para uma cor uniforme
     * (`LineBorder` de cor cinza claro).
     *
     * - **Limpeza de Campos**: - Limpa o campo de pesquisa `txtPesEqui`,
     * garantindo que ele fique pronto para uma nova busca. - O trecho comentado
     * `setRowCount(0)` pode ser ativado futuramente para resetar a tabela após
     * a seleção do equipamento.
     *
     * Fluxo do Método: 1. Obtém a linha selecionada na tabela `tblEquiSet`. 2.
     * Recupera e exibe os dados do equipamento selecionado nos campos
     * correspondentes. 3. Chama `pesquisarEquipamentoIni()` para carregar os
     * equipamentos relacionados ao setor. 4. Limpa o campo de pesquisa
     * `txtPesEqui`. 5. Ajusta a aparência dos campos de entrada.
     */
    private void setar_equipamento() {

        int setar = tblEquiSet.getSelectedRow();
        txtIdEquiSet.setText(tblEquiSet.getModel().getValueAt(setar, 0).toString());
        txtNonEquiSet.setText(tblEquiSet.getModel().getValueAt(setar, 1).toString());
        txtCodEquiSet.setText(tblEquiSet.getModel().getValueAt(setar, 2).toString());
        txtTipEqui.setText(tblEquiSet.getModel().getValueAt(setar, 3).toString());
        rbtTecnico.setEnabled(true);
        pesquisarEquipamentoIni();
//        ((DefaultTableModel) tblEquiSet.getModel()).setRowCount(0);
        txtPesEqui.setText(null);
        txtIdEquiSet.setBorder(new LineBorder(new Color(240, 240, 240)));
        txtCodEquiSet.setBorder(new LineBorder(new Color(240, 240, 240)));
        txtNonEquiSet.setBorder(new LineBorder(new Color(240, 240, 240)));
        txtTipEqui.setBorder(new LineBorder(new Color(240, 240, 240)));
        selecionarPreventiva();
        cboTipPrev.setEnabled(true);

    }

    /**
     * Método `preventiva`. Este método realiza ações com base no valor da
     * variável `preve`, que pode indicar se a busca está relacionada a "setor",
     * "equipamento" ou "técnico". Dependendo da opção escolhida, ele executa
     * funções para limpar a interface, atualizar a tabela de resultados e
     * chamar métodos apropriados para pesquisar os dados.
     *
     * Funcionalidades: - **Lógica Condicional**: - Executa operações diferentes
     * dependendo do valor de `preve`: - `"setor"`: Limpa dados e busca setores
     * disponíveis. - `"equipamento"`: Filtra equipamentos específicos. -
     * `"tecnico"`: Busca técnicos associados à manutenção.
     *
     * - **Configuração Inicial**: - `limpar()`: Remove dados anteriores,
     * garantindo uma exibição organizada. - `rbtEquipamento.setEnabled(false)`:
     * Desativa o botão de seleção de equipamentos na interface. -
     * `txtPesEqui.requestFocus()`: Direciona o foco para o campo de pesquisa. -
     * `((DefaultTableModel) tblEquiSet.getModel()).setRowCount(0)`: Limpa os
     * dados da tabela antes de realizar uma nova busca.
     *
     * - **Pesquisa e Atualização da Tabela**: - Chama `pesquisar_prev()`,
     * `pesquisar_equipamento()`, ou `pesquisar_tecnico_prev()` dependendo do
     * valor de `preve`, garantindo que a busca traga resultados adequados. -
     * Invoca `formTabela()` para organizar os dados e ajustar visualmente as
     * colunas e células da tabela.
     *
     * Fluxo do Método: 1. Verifica o valor de `preve` e executa a lógica
     * correspondente. 2. Limpa dados antigos e prepara a interface. 3. Executa
     * a pesquisa apropriada e exibe os resultados na tabela. 4. Ajusta a
     * formatação da tabela com `formTabela()`.
     */
    private void preventiva() {

        if (preve.equals("setor")) {
            limpar();
            rbtEquipamento.setEnabled(false);
            txtPesEqui.requestFocus();
            ((DefaultTableModel) tblEquiSet.getModel()).setRowCount(0);
            pesquisarSetorIni();
            formTabela();
            cboTipPrev.setEnabled(false);
            cboTipPrev.setModel(new DefaultComboBoxModel());
            cboTipPrev.addItem("-Selecione-");
        }
        if (preve.equals("equipamento")) {
            ((DefaultTableModel) tblEquiSet.getModel()).setRowCount(0);
            rbtTecnico.setEnabled(false);
            txtIdTecPrev.setText(null);
            txtTecPrev.setText(null);
            txtTipTec.setText(null);
            pesquisarEquipamentoIni();
            txtPesEqui.requestFocus();
            formTabela();
        }
        if (preve.equals("tecnico")) {
            ((DefaultTableModel) tblEquiSet.getModel()).setRowCount(0);
            pesquisarTecnicoIni();
            txtPesEqui.requestFocus();
            formTabela();
        }

    }

    /**
     * Método `setarPreventiva`. Este método identifica o contexto da variável
     * `preve` e executa ações apropriadas para definir os dados do setor,
     * equipamento ou técnico selecionado. Ele facilita a seleção de itens na
     * interface gráfica e garante que as informações corretas sejam exibidas
     * conforme a categoria escolhida pelo usuário.
     *
     * Funcionalidades: - **Execução Condicional**: - Verifica o valor de
     * `preve` e chama métodos correspondentes: - `"setor"` →
     * `setar_campos_prev()` para configurar os dados do setor. -
     * `"equipamento"` → `setar_equipamento()` para exibir informações do
     * equipamento. - `"tecnico"` → `setar_tecnico_prev()` para preencher os
     * dados do técnico.
     *
     * - **Ativação de Elementos da Interface**: - Caso `preve` seja `"setor"`,
     * o botão `rbtEquipamento` é ativado (`setEnabled(true)`) para permitir a
     * seleção de equipamentos relacionados ao setor.
     *
     * Fluxo do Método: 1. Identifica o contexto baseado na variável `preve`. 2.
     * Executa o método correspondente (`setar_campos_prev()`,
     * `setar_equipamento()` ou `setar_tecnico_prev()`). 3. Se o contexto for
     * `"setor"`, ativa o botão `rbtEquipamento`.
     */
    private void setarPreventiva() {

        if (preve.equals("setor")) {
            setar_campos_prev();
            rbtEquipamento.setEnabled(true);

        }
        if (preve.equals("equipamento")) {
            setar_equipamento();
        }
        if (preve.equals("tecnico")) {
            setar_tecnico_prev();
        }

    }

    /**
     * Método `vazio`. Este método verifica se os campos de entrada
     * (`txtIdSetPrev`, `txtIdEquiSet`, `txtIdTecPrev`) estão vazios. Caso um ou
     * mais campos estejam sem preenchimento, ele altera a borda para vermelho,
     * indicando visualmente ao usuário que os campos são obrigatórios.
     *
     * Funcionalidades: - **Validação de Campos Vazios**: - Verifica se os
     * campos `txtIdSetPrev`, `txtIdEquiSet` e `txtIdTecPrev` estão vazios. - Se
     * estiverem em branco (`""`), aplica uma borda vermelha nos respectivos
     * campos para alertar o usuário.
     *
     * - **Destaque Visual para Campos Inválidos**: - Se `txtIdSetPrev` estiver
     * vazio, aplica borda vermelha também no campo `txtSetPrev`. - Se
     * `txtIdEquiSet` estiver vazio, aplica borda vermelha em `txtCodEquiSet`,
     * `txtNonEquiSet` e `txtTipEqui`. - Se `txtIdTecPrev` estiver vazio, aplica
     * borda vermelha em `txtTipTec` e `txtTecPrev`.
     *
     * - **Tratamento de Exceções**: - Captura erros inesperados dentro do bloco
     * `try-catch` e exibe uma mensagem ao usuário em caso de falha.
     *
     * Fluxo do Método: 1. Obtém os valores dos campos de entrada. 2. Verifica
     * quais campos estão vazios. 3. Aplica borda vermelha nos campos
     * obrigatórios não preenchidos. 4. Captura e trata possíveis erros.
     */
    private void vazio() {

        va = txtIdSetPrev.getText();
        va1 = txtIdEquiSet.getText();
        va2 = txtIdTecPrev.getText();

        try {
            if (va.equals("")) {
                txtIdSetPrev.setBorder(new LineBorder(Color.RED));
                txtSetPrev.setBorder(new LineBorder(Color.RED));
            }
            if (va1.equals("")) {
                txtIdEquiSet.setBorder(new LineBorder(Color.RED));
                txtCodEquiSet.setBorder(new LineBorder(Color.RED));
                txtNonEquiSet.setBorder(new LineBorder(Color.RED));
                txtTipEqui.setBorder(new LineBorder(Color.RED));
            }
            if (va2.equals("")) {
                txtIdTecPrev.setBorder(new LineBorder(Color.RED));
                txtTipTec.setBorder(new LineBorder(Color.RED));
                txtTecPrev.setBorder(new LineBorder(Color.RED));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `iniciar_prev`. Este método realiza a validação e inserção de uma
     * nova manutenção preventiva no banco de dados, garantindo que todos os
     * campos obrigatórios estejam preenchidos antes da execução da operação.
     *
     * Funcionalidades: - **Registro da Data e Hora Atual**: - Usa
     * `DateTimeFormatter` para definir a data e hora atual no formato médio e
     * atribui a `txtDatPrev`.
     *
     * - **Validação da Escolha do Tipo de Preventiva**: - Se o usuário não
     * selecionar um tipo de manutenção (`"-Selecione-"`), exibe uma mensagem de
     * alerta e marca o campo `cboTipPrev` com borda vermelha.
     *
     * - **Inserção de Dados no Banco**: - Se um tipo válido de preventiva for
     * escolhido (`Mensal`, `Trimestral`, `Semestral`), o método prepara uma
     * query `INSERT` correspondente para adicionar os dados no banco. - Usa
     * `PreparedStatement` para definir os valores dinamicamente e evitar
     * vulnerabilidades.
     *
     * - **Verificação de Campos Obrigatórios**: - Se algum dos campos estiver
     * vazio, ativa a função `vazio()`, exibe uma mensagem de erro e impede a
     * inserção.
     *
     * - **Execução da Query e Feedback ao Usuário**: - Se a inserção for
     * bem-sucedida, exibe um alerta informando que a preventiva foi cadastrada.
     * - Em caso de falha, exibe um alerta de erro e encerra a conexão.
     *
     * Fluxo do Método: 1. Define a data e hora atual. 2. Valida se um tipo de
     * preventiva foi selecionado. 3. Prepara a inserção dos dados conforme o
     * tipo de preventiva escolhido. 4. Verifica se todos os campos obrigatórios
     * estão preenchidos. 5. Executa a inserção no banco de dados. 6. Exibe
     * mensagens de sucesso ou erro conforme o resultado da operação.
     */
    private void iniciar_prev() {
        Color c;
        DateTimeFormatter data_hora = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
        ZonedDateTime zdtNow = ZonedDateTime.now();
        String format = data_hora.format(zdtNow);
        txtDatPrev.setText(format);

        String perfil = cboTipPrev.getSelectedItem().toString();
        try {
            if (perfil.equals("-Selecione-")) {
                c = Color.RED;
                txtDatPrev.setText(null);
                JOptionPane.showMessageDialog(null, "Escolha um Tipo de Preventiva!");
                cboTipPrev.setBorder(new LineBorder(c));
                Toolkit.getDefaultToolkit().beep();
            } else {
                cboTipPrev.setBorder(new LineBorder(new Color(240, 240, 240)));
            }
            if (perfil.equals("Eletrica - Mensal")) {
                cboTipPrev.setBorder(new LineBorder(new Color(240, 240, 240)));
                String sql = "insert into form_eletrica_mensal(id_setor, setor_prev, id_equi_set, nome_equi_set, cod_equi_set, tipo_equi, iduser, nome_prev, tipo_usuario, tipo_prev, tempo_dis ) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtIdSetPrev.getText());
                pst.setString(2, txtSetPrev.getText());
                pst.setString(3, txtIdEquiSet.getText());
                pst.setString(4, txtNonEquiSet.getText());
                pst.setString(5, txtCodEquiSet.getText());
                pst.setString(6, txtTipEqui.getText());
                pst.setString(7, txtIdTecPrev.getText());
                pst.setString(8, txtTecPrev.getText());
                pst.setString(9, txtTipTec.getText());
                pst.setString(10, cboTipPrev.getSelectedItem().toString());
                pst.setString(11, txtDatPrev.getText());
                if ((txtIdSetPrev.getText().isEmpty()) || (txtSetPrev.getText().isEmpty()) || (txtIdTecPrev.getText().isEmpty()) || (txtIdEquiSet.getText().isEmpty()) || (txtNonEquiSet.getText().isEmpty()) || (txtCodEquiSet.getText().isEmpty()) || (txtTecPrev.getText().isEmpty()) || (cboTipPrev.getSelectedItem().toString().isEmpty()) || (txtDatPrev.getText().isEmpty())) {
                    txtDatPrev.setText(null);
                    vazio();
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(null, "Preencha os Campos Obrigatórios!");
                } else {
                    int Adicionar = pst.executeUpdate();

                    if (Adicionar > 0) {
                        JOptionPane.showMessageDialog(null, "Preventiva Cadastrada com Sucesso!");
                        limpar();
                        conexao.close();
                        dispose();

                    } else {
                        Toolkit.getDefaultToolkit().beep();
                        JOptionPane.showMessageDialog(null, "Erro ao Cadastrar Setor!");
                        limpar();
                        conexao.close();

                    }
                }

            }
            if (perfil.equals("Eletrica - Trimestral")) {
                String sql = "insert into form_eletrica_trimestral(id_setor, setor_prev, id_equi_set, nome_equi_set, cod_equi_set, tipo_equi, iduser, nome_prev, tipo_usuario, tipo_prev, tempo_dis ) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtIdSetPrev.getText());
                pst.setString(2, txtSetPrev.getText());
                pst.setString(3, txtIdEquiSet.getText());
                pst.setString(4, txtNonEquiSet.getText());
                pst.setString(5, txtCodEquiSet.getText());
                pst.setString(6, txtTipEqui.getText());
                pst.setString(7, txtIdTecPrev.getText());
                pst.setString(8, txtTecPrev.getText());
                pst.setString(9, txtTipTec.getText());
                pst.setString(10, cboTipPrev.getSelectedItem().toString());
                pst.setString(11, txtDatPrev.getText());
                if ((txtIdSetPrev.getText().isEmpty()) || (txtSetPrev.getText().isEmpty()) || (txtIdTecPrev.getText().isEmpty()) || (txtIdEquiSet.getText().isEmpty()) || (txtNonEquiSet.getText().isEmpty()) || (txtCodEquiSet.getText().isEmpty()) || (txtTecPrev.getText().isEmpty()) || (cboTipPrev.getSelectedItem().toString().isEmpty()) || (txtDatPrev.getText().isEmpty())) {
                    txtDatPrev.setText(null);
                    vazio();
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(null, "Preencha os Campos Obrigatórios!");
                } else {
                    int Adicionar = pst.executeUpdate();

                    if (Adicionar > 0) {
                        JOptionPane.showMessageDialog(null, "Preventiva Cadastrada com Sucesso!");
                        limpar();
                        conexao.close();
                        dispose();

                    } else {
                        JOptionPane.showMessageDialog(null, "Erro ao Cadastrar Setor!");
                        limpar();
                        conexao.close();

                    }
                }

            }
            if (perfil.equals("Eletrica - Semestral")) {
                String sql = "insert into form_eletrica_semestral(id_setor, setor_prev, id_equi_set, nome_equi_set, cod_equi_set, tipo_equi, iduser, nome_prev, tipo_usuario, tipo_prev, tempo_dis ) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtIdSetPrev.getText());
                pst.setString(2, txtSetPrev.getText());
                pst.setString(3, txtIdEquiSet.getText());
                pst.setString(4, txtNonEquiSet.getText());
                pst.setString(5, txtCodEquiSet.getText());
                pst.setString(6, txtTipEqui.getText());
                pst.setString(7, txtIdTecPrev.getText());
                pst.setString(8, txtTecPrev.getText());
                pst.setString(9, txtTipTec.getText());
                pst.setString(10, cboTipPrev.getSelectedItem().toString());
                pst.setString(11, txtDatPrev.getText());
                if ((txtIdSetPrev.getText().isEmpty()) || (txtSetPrev.getText().isEmpty()) || (txtIdTecPrev.getText().isEmpty()) || (txtIdEquiSet.getText().isEmpty()) || (txtNonEquiSet.getText().isEmpty()) || (txtCodEquiSet.getText().isEmpty()) || (txtTecPrev.getText().isEmpty()) || (cboTipPrev.getSelectedItem().toString().isEmpty()) || (txtDatPrev.getText().isEmpty())) {
                    txtDatPrev.setText(null);
                    vazio();
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(null, "Preencha os Campos Obrigatórios!");
                } else {
                    int Adicionar = pst.executeUpdate();

                    if (Adicionar > 0) {
                        JOptionPane.showMessageDialog(null, "Preventiva Cadastrada com Sucesso!");
                        limpar();
                        conexao.close();
                        dispose();

                    } else {
                        JOptionPane.showMessageDialog(null, "Erro ao Cadastrar Setor!");
                        limpar();
                        conexao.close();

                    }
                }

            }

            if (perfil.equals("Civil - Mensal")) {
                String sql = "insert into form_civil_mensal(id_setor, setor_prev, id_equi_set, nome_equi_set, cod_equi_set, tipo_equi, iduser, nome_prev, tipo_usuario, tipo_prev, tempo_dis ) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtIdSetPrev.getText());
                pst.setString(2, txtSetPrev.getText());
                pst.setString(3, txtIdEquiSet.getText());
                pst.setString(4, txtNonEquiSet.getText());
                pst.setString(5, txtCodEquiSet.getText());
                pst.setString(6, txtTipEqui.getText());
                pst.setString(7, txtIdTecPrev.getText());
                pst.setString(8, txtTecPrev.getText());
                pst.setString(9, txtTipTec.getText());
                pst.setString(10, cboTipPrev.getSelectedItem().toString());
                pst.setString(11, txtDatPrev.getText());
                if ((txtIdSetPrev.getText().isEmpty()) || (txtSetPrev.getText().isEmpty()) || (txtIdTecPrev.getText().isEmpty()) || (txtIdEquiSet.getText().isEmpty()) || (txtNonEquiSet.getText().isEmpty()) || (txtCodEquiSet.getText().isEmpty()) || (txtTecPrev.getText().isEmpty()) || (cboTipPrev.getSelectedItem().toString().isEmpty()) || (txtDatPrev.getText().isEmpty())) {
                    txtDatPrev.setText(null);
                    vazio();
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(null, "Preencha os Campos Obrigatórios!");
                } else {
                    int Adicionar = pst.executeUpdate();

                    if (Adicionar > 0) {
                        JOptionPane.showMessageDialog(null, "Preventiva Cadastrada com Sucesso!");
                        limpar();
                        conexao.close();
                        dispose();

                    } else {
                        JOptionPane.showMessageDialog(null, "Erro ao Cadastrar Setor!");
                        limpar();
                        conexao.close();

                    }
                }

            }

            if (perfil.equals("Civil - Trimestral")) {
                String sql = "insert into form_civil_trimestral(id_setor, setor_prev, id_equi_set, nome_equi_set, cod_equi_set, tipo_equi, iduser, nome_prev, tipo_usuario, tipo_prev, tempo_dis ) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtIdSetPrev.getText());
                pst.setString(2, txtSetPrev.getText());
                pst.setString(3, txtIdEquiSet.getText());
                pst.setString(4, txtNonEquiSet.getText());
                pst.setString(5, txtCodEquiSet.getText());
                pst.setString(6, txtTipEqui.getText());
                pst.setString(7, txtIdTecPrev.getText());
                pst.setString(8, txtTecPrev.getText());
                pst.setString(9, txtTipTec.getText());
                pst.setString(10, cboTipPrev.getSelectedItem().toString());
                pst.setString(11, txtDatPrev.getText());
                if ((txtIdSetPrev.getText().isEmpty()) || (txtSetPrev.getText().isEmpty()) || (txtIdTecPrev.getText().isEmpty()) || (txtIdEquiSet.getText().isEmpty()) || (txtNonEquiSet.getText().isEmpty()) || (txtCodEquiSet.getText().isEmpty()) || (txtTecPrev.getText().isEmpty()) || (cboTipPrev.getSelectedItem().toString().isEmpty()) || (txtDatPrev.getText().isEmpty())) {
                    txtDatPrev.setText(null);
                    vazio();
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(null, "Preencha os Campos Obrigatórios!");
                } else {
                    int Adicionar = pst.executeUpdate();

                    if (Adicionar > 0) {
                        JOptionPane.showMessageDialog(null, "Preventiva Cadastrada com Sucesso!");
                        limpar();
                        conexao.close();
                        dispose();

                    } else {
                        JOptionPane.showMessageDialog(null, "Erro ao Cadastrar Setor!");
                        limpar();
                        conexao.close();

                    }
                }

            }

            if (perfil.equals("Civil - Semestral")) {
                String sql = "insert into form_civil_semestral(id_setor, setor_prev, id_equi_set, nome_equi_set, cod_equi_set, tipo_equi, iduser, nome_prev, tipo_usuario, tipo_prev, tempo_dis ) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtIdSetPrev.getText());
                pst.setString(2, txtSetPrev.getText());
                pst.setString(3, txtIdEquiSet.getText());
                pst.setString(4, txtNonEquiSet.getText());
                pst.setString(5, txtCodEquiSet.getText());
                pst.setString(6, txtTipEqui.getText());
                pst.setString(7, txtIdTecPrev.getText());
                pst.setString(8, txtTecPrev.getText());
                pst.setString(9, txtTipTec.getText());
                pst.setString(10, cboTipPrev.getSelectedItem().toString());
                pst.setString(11, txtDatPrev.getText());
                if ((txtIdSetPrev.getText().isEmpty()) || (txtSetPrev.getText().isEmpty()) || (txtIdTecPrev.getText().isEmpty()) || (txtIdEquiSet.getText().isEmpty()) || (txtNonEquiSet.getText().isEmpty()) || (txtCodEquiSet.getText().isEmpty()) || (txtTecPrev.getText().isEmpty()) || (cboTipPrev.getSelectedItem().toString().isEmpty()) || (txtDatPrev.getText().isEmpty())) {
                    txtDatPrev.setText(null);
                    vazio();
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(null, "Preencha os Campos Obrigatórios!");
                } else {
                    int Adicionar = pst.executeUpdate();

                    if (Adicionar > 0) {
                        JOptionPane.showMessageDialog(null, "Preventiva Cadastrada com Sucesso!");
                        limpar();
                        conexao.close();
                        dispose();

                    } else {
                        JOptionPane.showMessageDialog(null, "Erro ao Cadastrar Setor!");
                        limpar();
                        conexao.close();

                    }
                }

            }

            if (perfil.equals("Hidráulica - Mensal")) {
                String sql = "insert into form_hidraulica_mensal(id_setor, setor_prev, id_equi_set, nome_equi_set, cod_equi_set, tipo_equi, iduser, nome_prev, tipo_usuario, tipo_prev, tempo_dis ) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtIdSetPrev.getText());
                pst.setString(2, txtSetPrev.getText());
                pst.setString(3, txtIdEquiSet.getText());
                pst.setString(4, txtNonEquiSet.getText());
                pst.setString(5, txtCodEquiSet.getText());
                pst.setString(6, txtTipEqui.getText());
                pst.setString(7, txtIdTecPrev.getText());
                pst.setString(8, txtTecPrev.getText());
                pst.setString(9, txtTipTec.getText());
                pst.setString(10, cboTipPrev.getSelectedItem().toString());
                pst.setString(11, txtDatPrev.getText());
                if ((txtIdSetPrev.getText().isEmpty()) || (txtSetPrev.getText().isEmpty()) || (txtIdTecPrev.getText().isEmpty()) || (txtIdEquiSet.getText().isEmpty()) || (txtNonEquiSet.getText().isEmpty()) || (txtCodEquiSet.getText().isEmpty()) || (txtTecPrev.getText().isEmpty()) || (cboTipPrev.getSelectedItem().toString().isEmpty()) || (txtDatPrev.getText().isEmpty())) {
                    txtDatPrev.setText(null);
                    vazio();
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(null, "Preencha os Campos Obrigatórios!");
                } else {
                    int Adicionar = pst.executeUpdate();

                    if (Adicionar > 0) {
                        JOptionPane.showMessageDialog(null, "Preventiva Cadastrada com Sucesso!");
                        limpar();
                        conexao.close();
                        dispose();

                    } else {
                        JOptionPane.showMessageDialog(null, "Erro ao Cadastrar Setor!");
                        limpar();
                        conexao.close();

                    }
                }

            }

            if (perfil.equals("Hidráulica - Trimestral")) {
                String sql = "insert into form_hidraulica_trimestral(id_setor, setor_prev, id_equi_set, nome_equi_set, cod_equi_set, tipo_equi, iduser, nome_prev, tipo_usuario, tipo_prev, tempo_dis ) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtIdSetPrev.getText());
                pst.setString(2, txtSetPrev.getText());
                pst.setString(3, txtIdEquiSet.getText());
                pst.setString(4, txtNonEquiSet.getText());
                pst.setString(5, txtCodEquiSet.getText());
                pst.setString(6, txtTipEqui.getText());
                pst.setString(7, txtIdTecPrev.getText());
                pst.setString(8, txtTecPrev.getText());
                pst.setString(9, txtTipTec.getText());
                pst.setString(10, cboTipPrev.getSelectedItem().toString());
                pst.setString(11, txtDatPrev.getText());
                if ((txtIdSetPrev.getText().isEmpty()) || (txtSetPrev.getText().isEmpty()) || (txtIdTecPrev.getText().isEmpty()) || (txtIdEquiSet.getText().isEmpty()) || (txtNonEquiSet.getText().isEmpty()) || (txtCodEquiSet.getText().isEmpty()) || (txtTecPrev.getText().isEmpty()) || (cboTipPrev.getSelectedItem().toString().isEmpty()) || (txtDatPrev.getText().isEmpty())) {
                    txtDatPrev.setText(null);
                    vazio();
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(null, "Preencha os Campos Obrigatórios!");
                } else {
                    int Adicionar = pst.executeUpdate();

                    if (Adicionar > 0) {
                        JOptionPane.showMessageDialog(null, "Preventiva Cadastrada com Sucesso!");
                        limpar();
                        conexao.close();
                        dispose();

                    } else {
                        JOptionPane.showMessageDialog(null, "Erro ao Cadastrar Setor!");
                        limpar();
                        conexao.close();

                    }
                }

            }

            if (perfil.equals("Hidráulica - Semestral")) {
                String sql = "insert into form_hidraulica_semestral(id_setor, setor_prev, id_equi_set, nome_equi_set, cod_equi_set, tipo_equi, iduser, nome_prev, tipo_usuario, tipo_prev, tempo_dis ) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtIdSetPrev.getText());
                pst.setString(2, txtSetPrev.getText());
                pst.setString(3, txtIdEquiSet.getText());
                pst.setString(4, txtNonEquiSet.getText());
                pst.setString(5, txtCodEquiSet.getText());
                pst.setString(6, txtTipEqui.getText());
                pst.setString(7, txtIdTecPrev.getText());
                pst.setString(8, txtTecPrev.getText());
                pst.setString(9, txtTipTec.getText());
                pst.setString(10, cboTipPrev.getSelectedItem().toString());
                pst.setString(11, txtDatPrev.getText());
                if ((txtIdSetPrev.getText().isEmpty()) || (txtSetPrev.getText().isEmpty()) || (txtIdTecPrev.getText().isEmpty()) || (txtIdEquiSet.getText().isEmpty()) || (txtNonEquiSet.getText().isEmpty()) || (txtCodEquiSet.getText().isEmpty()) || (txtTecPrev.getText().isEmpty()) || (cboTipPrev.getSelectedItem().toString().isEmpty()) || (txtDatPrev.getText().isEmpty())) {
                    txtDatPrev.setText(null);
                    vazio();
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(null, "Preencha os Campos Obrigatórios!");
                } else {
                    int Adicionar = pst.executeUpdate();

                    if (Adicionar > 0) {
                        JOptionPane.showMessageDialog(null, "Preventiva Cadastrada com Sucesso!");
                        limpar();
                        conexao.close();
                        dispose();

                    } else {
                        JOptionPane.showMessageDialog(null, "Erro ao Cadastrar Setor!");
                        limpar();
                        conexao.close();

                    }
                }

            }

            if (perfil.equals("Refrigeração - Mensal")) {
                String sql = "insert into form_refrigeracao_mensal(id_setor, setor_prev, id_equi_set, nome_equi_set, cod_equi_set, tipo_equi, iduser, nome_prev, tipo_usuario, tipo_prev, tempo_dis ) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtIdSetPrev.getText());
                pst.setString(2, txtSetPrev.getText());
                pst.setString(3, txtIdEquiSet.getText());
                pst.setString(4, txtNonEquiSet.getText());
                pst.setString(5, txtCodEquiSet.getText());
                pst.setString(6, txtTipEqui.getText());
                pst.setString(7, txtIdTecPrev.getText());
                pst.setString(8, txtTecPrev.getText());
                pst.setString(9, txtTipTec.getText());
                pst.setString(10, cboTipPrev.getSelectedItem().toString());
                pst.setString(11, txtDatPrev.getText());
                if ((txtIdSetPrev.getText().isEmpty()) || (txtSetPrev.getText().isEmpty()) || (txtIdTecPrev.getText().isEmpty()) || (txtIdEquiSet.getText().isEmpty()) || (txtNonEquiSet.getText().isEmpty()) || (txtCodEquiSet.getText().isEmpty()) || (txtTecPrev.getText().isEmpty()) || (cboTipPrev.getSelectedItem().toString().isEmpty()) || (txtDatPrev.getText().isEmpty())) {
                    txtDatPrev.setText(null);
                    vazio();
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(null, "Preencha os Campos Obrigatórios!");
                } else {
                    int Adicionar = pst.executeUpdate();

                    if (Adicionar > 0) {
                        JOptionPane.showMessageDialog(null, "Preventiva Cadastrada com Sucesso!");
                        limpar();
                        conexao.close();
                        dispose();

                    } else {
                        JOptionPane.showMessageDialog(null, "Erro ao Cadastrar Setor!");
                        limpar();
                        conexao.close();

                    }
                }

            }

            if (perfil.equals("Refrigeração - Trimestral")) {
                String sql = "insert into form_refrigeracao_trimestral(id_setor, setor_prev, id_equi_set, nome_equi_set, cod_equi_set, tipo_equi, iduser, nome_prev, tipo_usuario, tipo_prev, tempo_dis ) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtIdSetPrev.getText());
                pst.setString(2, txtSetPrev.getText());
                pst.setString(3, txtIdEquiSet.getText());
                pst.setString(4, txtNonEquiSet.getText());
                pst.setString(5, txtCodEquiSet.getText());
                pst.setString(6, txtTipEqui.getText());
                pst.setString(7, txtIdTecPrev.getText());
                pst.setString(8, txtTecPrev.getText());
                pst.setString(9, txtTipTec.getText());
                pst.setString(10, cboTipPrev.getSelectedItem().toString());
                pst.setString(11, txtDatPrev.getText());
                if ((txtIdSetPrev.getText().isEmpty()) || (txtSetPrev.getText().isEmpty()) || (txtIdTecPrev.getText().isEmpty()) || (txtIdEquiSet.getText().isEmpty()) || (txtNonEquiSet.getText().isEmpty()) || (txtCodEquiSet.getText().isEmpty()) || (txtTecPrev.getText().isEmpty()) || (cboTipPrev.getSelectedItem().toString().isEmpty()) || (txtDatPrev.getText().isEmpty())) {
                    txtDatPrev.setText(null);
                    vazio();
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(null, "Preencha os Campos Obrigatórios!");
                } else {
                    int Adicionar = pst.executeUpdate();

                    if (Adicionar > 0) {
                        JOptionPane.showMessageDialog(null, "Preventiva Cadastrada com Sucesso!");
                        limpar();
                        conexao.close();
                        dispose();

                    } else {
                        JOptionPane.showMessageDialog(null, "Erro ao Cadastrar Setor!");
                        limpar();
                        conexao.close();

                    }
                }

            }

            if (perfil.equals("Refrigeração - Semestral")) {
                String sql = "insert into form_refrigeracao_semestral(id_setor, setor_prev, id_equi_set, nome_equi_set, cod_equi_set, tipo_equi, iduser, nome_prev, tipo_usuario, tipo_prev, tempo_dis ) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtIdSetPrev.getText());
                pst.setString(2, txtSetPrev.getText());
                pst.setString(3, txtIdEquiSet.getText());
                pst.setString(4, txtNonEquiSet.getText());
                pst.setString(5, txtCodEquiSet.getText());
                pst.setString(6, txtTipEqui.getText());
                pst.setString(7, txtIdTecPrev.getText());
                pst.setString(8, txtTecPrev.getText());
                pst.setString(9, txtTipTec.getText());
                pst.setString(10, cboTipPrev.getSelectedItem().toString());
                pst.setString(11, txtDatPrev.getText());
                if ((txtIdSetPrev.getText().isEmpty()) || (txtSetPrev.getText().isEmpty()) || (txtIdTecPrev.getText().isEmpty()) || (txtIdEquiSet.getText().isEmpty()) || (txtNonEquiSet.getText().isEmpty()) || (txtCodEquiSet.getText().isEmpty()) || (txtTecPrev.getText().isEmpty()) || (cboTipPrev.getSelectedItem().toString().isEmpty()) || (txtDatPrev.getText().isEmpty())) {
                    txtDatPrev.setText(null);
                    vazio();
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(null, "Preencha os Campos Obrigatórios!");
                } else {
                    int Adicionar = pst.executeUpdate();

                    if (Adicionar > 0) {
                        JOptionPane.showMessageDialog(null, "Preventiva Cadastrada com Sucesso!");
                        limpar();
                        conexao.close();
                        dispose();

                    } else {
                        JOptionPane.showMessageDialog(null, "Erro ao Cadastrar Setor!");
                        limpar();
                        conexao.close();

                    }
                }

            }

        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Pesquisa técnicos disponíveis para modificação, de acordo com a
     * especialidade relacionada ao equipamento informado no campo txtTipEqui.
     *
     * Comportamento principal: - Define a área como "Manutenção". - Monta e
     * executa a consulta SQL na tabela de usuários, filtrando por area_usuario
     * e tipo_usuario semelhante ao texto do equipamento. - Atualiza o modelo da
     * tabela tblEquiSet com os resultados obtidos do banco de dados. - Chama o
     * método formTabela() para ajustar a exibição da tabela.
     *
     * Tratamento de erros: - Captura SQLException e exibe mensagem em
     * JOptionPane.
     *
     * Pré-condições: - txtTipEqui deve conter texto referente ao tipo de
     * equipamento, pois o filtro é montado a partir dele.
     *
     * Pós-condições: - tblEquiSet é atualizado com os técnicos compatíveis à
     * busca. - O layout da tabela é ajustado por formTabela().
     *
     * @see #formTabela()
     */
    private void pesquisarTecnicoModificar() {

        String area = "Manutenção";

        String sql = " select iduser as 'R.E', nome_usuario as 'Técnico', tipo_usuario as 'Especialidade' from usuarios where area_usuario = ? and tipo_usuario like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, area);
            pst.setString(2, "%".concat(txtTipEqui.getText()).split(" ")[0].concat("%"));
            rs = pst.executeQuery();
            tblEquiSet.setModel(DbUtils.resultSetToTableModel(rs));
            formTabela();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Realiza a pesquisa e o carregamento de uma preventiva no formulário, a
     * partir do identificador presente em txtIdPre, considerando o perfil/tipo
     * selecionado em tipo (por exemplo: "Eletrica - Mensal", "Civil -
     * Trimestral", "Hidráulica - Semestral", "Refrigeração - Mensal", etc.).
     *
     * Comportamento principal: - Inicializa buffer1, define o perfil de
     * execução como técnico e ajusta o estado de componentes da interface
     * (textos, botões e rádio buttons). - Monta e executa a consulta SQL
     * adequada à periodicidade e área (Elétrica, Civil, Hidráulica,
     * Refrigeração), utilizando o ID do formulário como parâmetro. - Ao
     * encontrar registro, preenche os campos de setor, equipamento, técnico,
     * tipo de usuário, tipo de preventiva e data prevista, além de acumular o
     * valor de tipo_prev em buffer1. - Após o preenchimento, invoca
     * selecionarPreventivaCbo() e pesquisarTecnicoModificar() para atualizar
     * seletores e dados dependentes.
     *
     * Efeitos colaterais: - Modifica diretamente diversos componentes da UI
     * (habilita/desabilita botões e rádios, altera textos). - Efetua leitura no
     * banco de dados por meio de conexao, pst e rs.
     *
     * Tratamento de erros: - Captura HeadlessException e SQLException, exibindo
     * a mensagem via JOptionPane.
     *
     * Pré-condições: - tipo deve conter um dos rótulos esperados de perfil. -
     * txtIdPre deve conter um ID válido existente na tabela alvo.
     *
     * Pós-condições: - Campos do formulário preenchidos conforme o registro
     * encontrado. - Controles da UI atualizados e callbacks auxiliares
     * executados.
     *
     * @implNote Este método não retorna valor; atua por efeitos colaterais na
     * UI e no estado interno do objeto.
     * @see #selecionarPreventivaCbo()
     * @see #pesquisarTecnicoModificar()
     */
    public void pesquisar_pre() {

        buffer1 = new StringBuffer();
        String perfil = tipo;
        preve = "tecnico";
        txtIdPre.setText(idprev);
        cboTipPrev.setEnabled(false);
        btnCadPre.setEnabled(false);
        btnApaPre.setEnabled(true);
        btnAlrPre.setEnabled(true);
        rbtSetor.setEnabled(false);
        rbtTecnico.setEnabled(true);
        rbtTecnico.setSelected(true);
        try {
            if (perfil.equals("Eletrica - Mensal")) {
                String sql = "select id_setor, setor_prev, id_equi_set, nome_equi_set, cod_equi_set, tipo_equi, iduser, nome_prev, tipo_usuario, tipo_prev, tempo_dis from form_eletrica_mensal where id_form_ele_mensal = ?";
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtIdPre.getText());
                rs = pst.executeQuery();
                if (rs.next()) {
                    txtIdSetPrev.setText(rs.getString(1));
                    txtSetPrev.setText(rs.getString(2));
                    txtIdEquiSet.setText(rs.getString(3));
                    txtNonEquiSet.setText(rs.getString(4));
                    txtCodEquiSet.setText(rs.getString(5));
                    txtTipEqui.setText(rs.getString(6));
                    txtIdTecPrev.setText(rs.getString(7));
                    txtTecPrev.setText(rs.getString(8));
                    txtTipTec.setText(rs.getString(9));
                    buffer1.append(rs.getString(10));
                    txtDatPrev.setText(rs.getString(11));
                    selecionarPreventivaCbo();
                    pesquisarTecnicoModificar();

                }
            }
            if (perfil.equals("Eletrica - Trimestral")) {
                String sql = "select id_setor, setor_prev, id_equi_set, nome_equi_set, cod_equi_set, tipo_equi, iduser, nome_prev, tipo_usuario, tipo_prev, tempo_dis from form_eletrica_trimestral where id_form_ele_trimestral = ?";
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtIdPre.getText());
                rs = pst.executeQuery();
                if (rs.next()) {
                    txtIdSetPrev.setText(rs.getString(1));
                    txtSetPrev.setText(rs.getString(2));
                    txtIdEquiSet.setText(rs.getString(3));
                    txtNonEquiSet.setText(rs.getString(4));
                    txtCodEquiSet.setText(rs.getString(5));
                    txtTipEqui.setText(rs.getString(6));
                    txtIdTecPrev.setText(rs.getString(7));
                    txtTecPrev.setText(rs.getString(8));
                    txtTipTec.setText(rs.getString(9));
                    buffer1.append(rs.getString(10));
                    txtDatPrev.setText(rs.getString(11));
                    selecionarPreventivaCbo();
                    pesquisarTecnicoModificar();
                }
            }
            if (perfil.equals("Eletrica - Semestral")) {
                String sql = "select id_setor, setor_prev, id_equi_set, nome_equi_set, cod_equi_set, tipo_equi, iduser, nome_prev, tipo_usuario, tipo_prev, tempo_dis from form_eletrica_semestral where id_form_ele_semestral = ?";
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtIdPre.getText());
                rs = pst.executeQuery();
                if (rs.next()) {
                    txtIdSetPrev.setText(rs.getString(1));
                    txtSetPrev.setText(rs.getString(2));
                    txtIdEquiSet.setText(rs.getString(3));
                    txtNonEquiSet.setText(rs.getString(4));
                    txtCodEquiSet.setText(rs.getString(5));
                    txtTipEqui.setText(rs.getString(6));
                    txtIdTecPrev.setText(rs.getString(7));
                    txtTecPrev.setText(rs.getString(8));
                    txtTipTec.setText(rs.getString(9));
                    buffer1.append(rs.getString(10));
                    txtDatPrev.setText(rs.getString(11));
                    selecionarPreventivaCbo();
                    pesquisarTecnicoModificar();

                }
            }

            if (perfil.equals("Civil - Mensal")) {
                String sql = "select id_setor, setor_prev, id_equi_set, nome_equi_set, cod_equi_set, tipo_equi, iduser, nome_prev, tipo_usuario, tipo_prev, tempo_dis from form_civil_mensal where id_civil_mensal = ?";
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtIdPre.getText());
                rs = pst.executeQuery();
                if (rs.next()) {
                    txtIdSetPrev.setText(rs.getString(1));
                    txtSetPrev.setText(rs.getString(2));
                    txtIdEquiSet.setText(rs.getString(3));
                    txtNonEquiSet.setText(rs.getString(4));
                    txtCodEquiSet.setText(rs.getString(5));
                    txtTipEqui.setText(rs.getString(6));
                    txtIdTecPrev.setText(rs.getString(7));
                    txtTecPrev.setText(rs.getString(8));
                    txtTipTec.setText(rs.getString(9));
                    buffer1.append(rs.getString(10));
                    txtDatPrev.setText(rs.getString(11));
                    selecionarPreventivaCbo();
                    pesquisarTecnicoModificar();
                }

            }

            if (perfil.equals("Civil - Trimestral")) {
                String sql = "select id_setor, setor_prev, id_equi_set, nome_equi_set, cod_equi_set, tipo_equi, iduser, nome_prev, tipo_usuario, tipo_prev, tempo_dis from form_civil_trimestral where id_civil_trimestral = ?";
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtIdPre.getText());
                rs = pst.executeQuery();
                if (rs.next()) {
                    txtIdSetPrev.setText(rs.getString(1));
                    txtSetPrev.setText(rs.getString(2));
                    txtIdEquiSet.setText(rs.getString(3));
                    txtNonEquiSet.setText(rs.getString(4));
                    txtCodEquiSet.setText(rs.getString(5));
                    txtTipEqui.setText(rs.getString(6));
                    txtIdTecPrev.setText(rs.getString(7));
                    txtTecPrev.setText(rs.getString(8));
                    txtTipTec.setText(rs.getString(9));
                    buffer1.append(rs.getString(10));
                    txtDatPrev.setText(rs.getString(11));
                    selecionarPreventivaCbo();
                    pesquisarTecnicoModificar();
                }

            }
            if (perfil.equals("Civil - Semestral")) {
                String sql = "select id_setor, setor_prev, id_equi_set, nome_equi_set, cod_equi_set, tipo_equi, iduser, nome_prev, tipo_usuario, tipo_prev, tempo_dis from form_civil_semestral where id_civil_semestral = ?";
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtIdPre.getText());
                rs = pst.executeQuery();
                if (rs.next()) {
                    txtIdSetPrev.setText(rs.getString(1));
                    txtSetPrev.setText(rs.getString(2));
                    txtIdEquiSet.setText(rs.getString(3));
                    txtNonEquiSet.setText(rs.getString(4));
                    txtCodEquiSet.setText(rs.getString(5));
                    txtTipEqui.setText(rs.getString(6));
                    txtIdTecPrev.setText(rs.getString(7));
                    txtTecPrev.setText(rs.getString(8));
                    txtTipTec.setText(rs.getString(9));
                    buffer1.append(rs.getString(10));
                    txtDatPrev.setText(rs.getString(11));
                    selecionarPreventivaCbo();
                    pesquisarTecnicoModificar();
                }

            }
            if (perfil.equals("Hidráulica - Mensal")) {
                String sql = "select id_setor, setor_prev, id_equi_set, nome_equi_set, cod_equi_set, tipo_equi, iduser, nome_prev, tipo_usuario, tipo_prev, tempo_dis from form_hidraulica_mensal where id_hidraulica_mensal = ?";
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtIdPre.getText());
                rs = pst.executeQuery();
                if (rs.next()) {
                    txtIdSetPrev.setText(rs.getString(1));
                    txtSetPrev.setText(rs.getString(2));
                    txtIdEquiSet.setText(rs.getString(3));
                    txtNonEquiSet.setText(rs.getString(4));
                    txtCodEquiSet.setText(rs.getString(5));
                    txtTipEqui.setText(rs.getString(6));
                    txtIdTecPrev.setText(rs.getString(7));
                    txtTecPrev.setText(rs.getString(8));
                    txtTipTec.setText(rs.getString(9));
                    buffer1.append(rs.getString(10));
                    txtDatPrev.setText(rs.getString(11));
                    selecionarPreventivaCbo();
                    pesquisarTecnicoModificar();
                }

            }
            if (perfil.equals("Hidráulica - Trimestral")) {
                String sql = "select id_setor, setor_prev, id_equi_set, nome_equi_set, cod_equi_set, tipo_equi, iduser, nome_prev, tipo_usuario, tipo_prev, tempo_dis from form_hidraulica_trimestral where id_hidraulica_trimestral = ?";
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtIdPre.getText());
                rs = pst.executeQuery();
                if (rs.next()) {
                    txtIdSetPrev.setText(rs.getString(1));
                    txtSetPrev.setText(rs.getString(2));
                    txtIdEquiSet.setText(rs.getString(3));
                    txtNonEquiSet.setText(rs.getString(4));
                    txtCodEquiSet.setText(rs.getString(5));
                    txtTipEqui.setText(rs.getString(6));
                    txtIdTecPrev.setText(rs.getString(7));
                    txtTecPrev.setText(rs.getString(8));
                    txtTipTec.setText(rs.getString(9));
                    buffer1.append(rs.getString(10));
                    txtDatPrev.setText(rs.getString(11));
                    selecionarPreventivaCbo();
                    pesquisarTecnicoModificar();
                }

            }

            if (perfil.equals("Hidráulica - Semestral")) {
                String sql = "select id_setor, setor_prev, id_equi_set, nome_equi_set, cod_equi_set, tipo_equi, iduser, nome_prev, tipo_usuario, tipo_prev, tempo_dis from form_hidraulica_semestral where id_hidraulica_semestral = ?";
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtIdPre.getText());
                rs = pst.executeQuery();
                if (rs.next()) {
                    txtIdSetPrev.setText(rs.getString(1));
                    txtSetPrev.setText(rs.getString(2));
                    txtIdEquiSet.setText(rs.getString(3));
                    txtNonEquiSet.setText(rs.getString(4));
                    txtCodEquiSet.setText(rs.getString(5));
                    txtTipEqui.setText(rs.getString(6));
                    txtIdTecPrev.setText(rs.getString(7));
                    txtTecPrev.setText(rs.getString(8));
                    txtTipTec.setText(rs.getString(9));
                    buffer1.append(rs.getString(10));
                    txtDatPrev.setText(rs.getString(11));
                    selecionarPreventivaCbo();
                    pesquisarTecnicoModificar();
                }

            }
            if (perfil.equals("Refrigeração - Mensal")) {
                String sql = "select id_setor, setor_prev, id_equi_set, nome_equi_set, cod_equi_set, tipo_equi, iduser, nome_prev, tipo_usuario, tipo_prev, tempo_dis from form_refrigeracao_mensal where id_refrigeracao_mensal = ?";
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtIdPre.getText());
                rs = pst.executeQuery();
                if (rs.next()) {
                    txtIdSetPrev.setText(rs.getString(1));
                    txtSetPrev.setText(rs.getString(2));
                    txtIdEquiSet.setText(rs.getString(3));
                    txtNonEquiSet.setText(rs.getString(4));
                    txtCodEquiSet.setText(rs.getString(5));
                    txtTipEqui.setText(rs.getString(6));
                    txtIdTecPrev.setText(rs.getString(7));
                    txtTecPrev.setText(rs.getString(8));
                    txtTipTec.setText(rs.getString(9));
                    buffer1.append(rs.getString(10));
                    txtDatPrev.setText(rs.getString(11));
                    selecionarPreventivaCbo();
                    pesquisarTecnicoModificar();
                }

            }

            if (perfil.equals("Refrigeração - Trimestral")) {
                String sql = "select id_setor, setor_prev, id_equi_set, nome_equi_set, cod_equi_set, tipo_equi, iduser, nome_prev, tipo_usuario, tipo_prev, tempo_dis from form_refrigeracao_trimestral where id_refrigeracao_trimestral = ?";
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtIdPre.getText());
                rs = pst.executeQuery();
                if (rs.next()) {
                    txtIdSetPrev.setText(rs.getString(1));
                    txtSetPrev.setText(rs.getString(2));
                    txtIdEquiSet.setText(rs.getString(3));
                    txtNonEquiSet.setText(rs.getString(4));
                    txtCodEquiSet.setText(rs.getString(5));
                    txtTipEqui.setText(rs.getString(6));
                    txtIdTecPrev.setText(rs.getString(7));
                    txtTecPrev.setText(rs.getString(8));
                    txtTipTec.setText(rs.getString(9));
                    buffer1.append(rs.getString(10));
                    txtDatPrev.setText(rs.getString(11));
                    selecionarPreventivaCbo();
                    pesquisarTecnicoModificar();
                }

            }

            if (perfil.equals("Refrigeração - Semestral")) {
                String sql = "select id_setor, setor_prev, id_equi_set, nome_equi_set, cod_equi_set, tipo_equi, iduser, nome_prev, tipo_usuario, tipo_prev, tempo_dis from form_refrigeracao_semestral where id_refrigeracao_semestral = ?";
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtIdPre.getText());
                rs = pst.executeQuery();
                if (rs.next()) {
                    txtIdSetPrev.setText(rs.getString(1));
                    txtSetPrev.setText(rs.getString(2));
                    txtIdEquiSet.setText(rs.getString(3));
                    txtNonEquiSet.setText(rs.getString(4));
                    txtCodEquiSet.setText(rs.getString(5));
                    txtTipEqui.setText(rs.getString(6));
                    txtIdTecPrev.setText(rs.getString(7));
                    txtTecPrev.setText(rs.getString(8));
                    txtTipTec.setText(rs.getString(9));
                    buffer1.append(rs.getString(10));
                    txtDatPrev.setText(rs.getString(11));
                    selecionarPreventivaCbo();
                    pesquisarTecnicoModificar();
                }

            }

        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    private void alterar_prev() {

        DateTimeFormatter data_hora = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
        ZonedDateTime zdtNow = ZonedDateTime.now();
        String format = data_hora.format(zdtNow);
        txtDatPrev.setText(format);

        String perfil = cboTipPrev.getSelectedItem().toString();
        try {
            if (perfil.equals("Eletrica - Mensal")) {
                String sql = "update form_eletrica_mensal set  id_setor = ?, setor_prev = ?, id_equi_set = ?, nome_equi_set = ?, cod_equi_set = ?, tipo_equi = ?, iduser = ?, nome_prev = ?, tipo_usuario = ?, tipo_prev = ?, tempo_dis = ? where id_form_ele_mensal = ?";
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtIdSetPrev.getText());
                pst.setString(2, txtSetPrev.getText());
                pst.setString(3, txtIdEquiSet.getText());
                pst.setString(4, txtNonEquiSet.getText());
                pst.setString(5, txtCodEquiSet.getText());
                pst.setString(6, txtTipEqui.getText());
                pst.setString(7, txtIdTecPrev.getText());
                pst.setString(8, txtTecPrev.getText());
                pst.setString(9, txtTipTec.getText());
                pst.setString(10, cboTipPrev.getSelectedItem().toString());
                pst.setString(11, txtDatPrev.getText());
                pst.setString(12, txtIdPre.getText());
                if ((txtIdSetPrev.getText().isEmpty()) || (txtSetPrev.getText().isEmpty()) || (txtIdTecPrev.getText().isEmpty()) || (txtIdEquiSet.getText().isEmpty()) || (txtNonEquiSet.getText().isEmpty()) || (txtCodEquiSet.getText().isEmpty()) || (txtTecPrev.getText().isEmpty()) || (cboTipPrev.getSelectedItem().toString().isEmpty()) || (txtDatPrev.getText().isEmpty())) {
                    JOptionPane.showMessageDialog(null, "Preencha os Campos Obrigatórios!");
                } else {
                    int Adicionar = pst.executeUpdate();

                    if (Adicionar > 0) {
                        JOptionPane.showMessageDialog(null, "Preventiva Cadastrada com Sucesso!");
                        limpar();
                        conexao.close();
                        dispose();

                    } else {
                        JOptionPane.showMessageDialog(null, "Erro ao Cadastrar Setor!");
                        limpar();
                        conexao.close();

                    }
                }

            }
            if (perfil.equals("Eletrica - Trimestral")) {
                String sql = "update form_eletrica_trimestral set id_setor = ?, setor_prev = ?, id_equi_set = ?, nome_equi_set = ?, cod_equi_set = ?, tipo_equi = ?, iduser = ?, nome_prev = ?, tipo_usuario = ?, tipo_prev = ?, tempo_dis = ? where id_form_ele_trimestral = ?";
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtIdSetPrev.getText());
                pst.setString(2, txtSetPrev.getText());
                pst.setString(3, txtIdEquiSet.getText());
                pst.setString(4, txtNonEquiSet.getText());
                pst.setString(5, txtCodEquiSet.getText());
                pst.setString(6, txtTipEqui.getText());
                pst.setString(7, txtIdTecPrev.getText());
                pst.setString(8, txtTecPrev.getText());
                pst.setString(9, txtTipTec.getText());
                pst.setString(10, cboTipPrev.getSelectedItem().toString());
                pst.setString(11, txtDatPrev.getText());
                pst.setString(12, txtIdPre.getText());
                if ((txtIdSetPrev.getText().isEmpty()) || (txtSetPrev.getText().isEmpty()) || (txtIdTecPrev.getText().isEmpty()) || (txtIdEquiSet.getText().isEmpty()) || (txtNonEquiSet.getText().isEmpty()) || (txtCodEquiSet.getText().isEmpty()) || (txtTecPrev.getText().isEmpty()) || (cboTipPrev.getSelectedItem().toString().isEmpty()) || (txtDatPrev.getText().isEmpty())) {
                    JOptionPane.showMessageDialog(null, "Preencha os Campos Obrigatórios!");
                } else {
                    int Adicionar = pst.executeUpdate();

                    if (Adicionar > 0) {
                        JOptionPane.showMessageDialog(null, "Preventiva Cadastrada com Sucesso!");
                        limpar();
                        conexao.close();
                        dispose();

                    } else {
                        JOptionPane.showMessageDialog(null, "Erro ao Cadastrar Setor!");
                        limpar();
                        conexao.close();

                    }
                }

            }
            if (perfil.equals("Eletrica - Semestral")) {
                String sql = "update form_eletrica_semestral set id_setor = ?, setor_prev = ?, id_equi_set = ?, nome_equi_set = ?, cod_equi_set = ?, tipo_equi = ?, iduser = ?, nome_prev = ?, tipo_usuario = ?, tipo_prev = ?, tempo_dis = ? where id_form_ele_semestral = ?";
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtIdSetPrev.getText());
                pst.setString(2, txtSetPrev.getText());
                pst.setString(3, txtIdEquiSet.getText());
                pst.setString(4, txtNonEquiSet.getText());
                pst.setString(5, txtCodEquiSet.getText());
                pst.setString(6, txtTipEqui.getText());
                pst.setString(7, txtIdTecPrev.getText());
                pst.setString(8, txtTecPrev.getText());
                pst.setString(9, txtTipTec.getText());
                pst.setString(10, cboTipPrev.getSelectedItem().toString());
                pst.setString(11, txtDatPrev.getText());
                pst.setString(12, txtIdPre.getText());
                if ((txtIdSetPrev.getText().isEmpty()) || (txtSetPrev.getText().isEmpty()) || (txtIdTecPrev.getText().isEmpty()) || (txtIdEquiSet.getText().isEmpty()) || (txtNonEquiSet.getText().isEmpty()) || (txtCodEquiSet.getText().isEmpty()) || (txtTecPrev.getText().isEmpty()) || (cboTipPrev.getSelectedItem().toString().isEmpty()) || (txtDatPrev.getText().isEmpty())) {
                    JOptionPane.showMessageDialog(null, "Preencha os Campos Obrigatórios!");
                } else {
                    int Adicionar = pst.executeUpdate();

                    if (Adicionar > 0) {
                        JOptionPane.showMessageDialog(null, "Preventiva Cadastrada com Sucesso!");
                        limpar();
                        conexao.close();
                        dispose();

                    } else {
                        JOptionPane.showMessageDialog(null, "Erro ao Cadastrar Setor!");
                        limpar();
                        conexao.close();

                    }
                }

            }

            if (perfil.equals("Civil - Mensal")) {
                String sql = "update form_civil_mensal set id_setor = ?, setor_prev = ?, id_equi_set = ?, nome_equi_set = ?, cod_equi_set = ?, tipo_equi = ?, iduser = ?, nome_prev = ?, tipo_usuario = ?, tipo_prev = ?, tempo_dis = ? where id_civil_mensal = ?";
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtIdSetPrev.getText());
                pst.setString(2, txtSetPrev.getText());
                pst.setString(3, txtIdEquiSet.getText());
                pst.setString(4, txtNonEquiSet.getText());
                pst.setString(5, txtCodEquiSet.getText());
                pst.setString(6, txtTipEqui.getText());
                pst.setString(7, txtIdTecPrev.getText());
                pst.setString(8, txtTecPrev.getText());
                pst.setString(9, txtTipTec.getText());
                pst.setString(10, cboTipPrev.getSelectedItem().toString());
                pst.setString(11, txtDatPrev.getText());
                pst.setString(12, txtIdPre.getText());
                if ((txtIdSetPrev.getText().isEmpty()) || (txtSetPrev.getText().isEmpty()) || (txtIdTecPrev.getText().isEmpty()) || (txtIdEquiSet.getText().isEmpty()) || (txtNonEquiSet.getText().isEmpty()) || (txtCodEquiSet.getText().isEmpty()) || (txtTecPrev.getText().isEmpty()) || (cboTipPrev.getSelectedItem().toString().isEmpty()) || (txtDatPrev.getText().isEmpty())) {
                    JOptionPane.showMessageDialog(null, "Preencha os Campos Obrigatórios!");
                } else {
                    int Adicionar = pst.executeUpdate();

                    if (Adicionar > 0) {
                        JOptionPane.showMessageDialog(null, "Preventiva Cadastrada com Sucesso!");
                        limpar();
                        conexao.close();
                        dispose();

                    } else {
                        JOptionPane.showMessageDialog(null, "Erro ao Cadastrar Setor!");
                        limpar();
                        conexao.close();

                    }
                }

            }

            if (perfil.equals("Civil - Trimestral")) {
                String sql = "update form_civil_trimestral set id_setor = ?, setor_prev = ?, id_equi_set = ?, nome_equi_set = ?, cod_equi_set = ?, tipo_equi = ?, iduser = ?, nome_prev = ?, tipo_usuario = ?, tipo_prev = ?, tempo_dis = ? where id_civil_trimestral = ?";
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtIdSetPrev.getText());
                pst.setString(1, txtIdSetPrev.getText());
                pst.setString(2, txtSetPrev.getText());
                pst.setString(3, txtIdEquiSet.getText());
                pst.setString(4, txtNonEquiSet.getText());
                pst.setString(5, txtCodEquiSet.getText());
                pst.setString(6, txtTipEqui.getText());
                pst.setString(7, txtIdTecPrev.getText());
                pst.setString(8, txtTecPrev.getText());
                pst.setString(9, txtTipTec.getText());
                pst.setString(10, cboTipPrev.getSelectedItem().toString());
                pst.setString(11, txtDatPrev.getText());
                pst.setString(12, txtIdPre.getText());
                if ((txtIdSetPrev.getText().isEmpty()) || (txtSetPrev.getText().isEmpty()) || (txtIdTecPrev.getText().isEmpty()) || (txtIdEquiSet.getText().isEmpty()) || (txtNonEquiSet.getText().isEmpty()) || (txtCodEquiSet.getText().isEmpty()) || (txtTecPrev.getText().isEmpty()) || (cboTipPrev.getSelectedItem().toString().isEmpty()) || (txtDatPrev.getText().isEmpty())) {
                    JOptionPane.showMessageDialog(null, "Preencha os Campos Obrigatórios!");
                } else {
                    int Adicionar = pst.executeUpdate();

                    if (Adicionar > 0) {
                        JOptionPane.showMessageDialog(null, "Preventiva Cadastrada com Sucesso!");
                        limpar();
                        conexao.close();
                        dispose();

                    } else {
                        JOptionPane.showMessageDialog(null, "Erro ao Cadastrar Setor!");
                        limpar();
                        conexao.close();

                    }
                }

            }

            if (perfil.equals("Civil - Semestral")) {
                String sql = "update form_civil_semestral set id_setor = ?, setor_prev = ?, id_equi_set = ?, nome_equi_set = ?, cod_equi_set = ?, tipo_equi = ?, iduser = ?, nome_prev = ?, tipo_usuario = ?, tipo_prev = ?, tempo_dis = ? where id_civil_semestral = ?";
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtIdSetPrev.getText());
                pst.setString(2, txtSetPrev.getText());
                pst.setString(3, txtIdEquiSet.getText());
                pst.setString(4, txtNonEquiSet.getText());
                pst.setString(5, txtCodEquiSet.getText());
                pst.setString(6, txtTipEqui.getText());
                pst.setString(7, txtIdTecPrev.getText());
                pst.setString(8, txtTecPrev.getText());
                pst.setString(9, txtTipTec.getText());
                pst.setString(10, cboTipPrev.getSelectedItem().toString());
                pst.setString(11, txtDatPrev.getText());
                pst.setString(12, txtIdPre.getText());
                if ((txtIdSetPrev.getText().isEmpty()) || (txtSetPrev.getText().isEmpty()) || (txtIdTecPrev.getText().isEmpty()) || (txtIdEquiSet.getText().isEmpty()) || (txtNonEquiSet.getText().isEmpty()) || (txtCodEquiSet.getText().isEmpty()) || (txtTecPrev.getText().isEmpty()) || (cboTipPrev.getSelectedItem().toString().isEmpty()) || (txtDatPrev.getText().isEmpty())) {
                    JOptionPane.showMessageDialog(null, "Preencha os Campos Obrigatórios!");
                } else {
                    int Adicionar = pst.executeUpdate();

                    if (Adicionar > 0) {
                        JOptionPane.showMessageDialog(null, "Preventiva Cadastrada com Sucesso!");
                        limpar();
                        conexao.close();
                        dispose();

                    } else {
                        JOptionPane.showMessageDialog(null, "Erro ao Cadastrar Setor!");
                        limpar();
                        conexao.close();

                    }
                }

            }

            if (perfil.equals("Hidráulica - Mensal")) {
                String sql = "update form_hidraulica_mensal set id_setor = ?, setor_prev = ?, id_equi_set = ?, nome_equi_set = ?, cod_equi_set = ?, tipo_equi = ?, iduser = ?, nome_prev = ?, tipo_usuario = ?, tipo_prev = ?, tempo_dis = ? where id_hidraulica_mensal = ?";
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtIdSetPrev.getText());
                pst.setString(2, txtSetPrev.getText());
                pst.setString(3, txtIdEquiSet.getText());
                pst.setString(4, txtNonEquiSet.getText());
                pst.setString(5, txtCodEquiSet.getText());
                pst.setString(6, txtTipEqui.getText());
                pst.setString(7, txtIdTecPrev.getText());
                pst.setString(8, txtTecPrev.getText());
                pst.setString(9, txtTipTec.getText());
                pst.setString(10, cboTipPrev.getSelectedItem().toString());
                pst.setString(11, txtDatPrev.getText());
                pst.setString(12, txtIdPre.getText());
                if ((txtIdSetPrev.getText().isEmpty()) || (txtSetPrev.getText().isEmpty()) || (txtIdTecPrev.getText().isEmpty()) || (txtIdEquiSet.getText().isEmpty()) || (txtNonEquiSet.getText().isEmpty()) || (txtCodEquiSet.getText().isEmpty()) || (txtTecPrev.getText().isEmpty()) || (cboTipPrev.getSelectedItem().toString().isEmpty()) || (txtDatPrev.getText().isEmpty())) {
                    JOptionPane.showMessageDialog(null, "Preencha os Campos Obrigatórios!");
                } else {
                    int Adicionar = pst.executeUpdate();

                    if (Adicionar > 0) {
                        JOptionPane.showMessageDialog(null, "Preventiva Cadastrada com Sucesso!");
                        limpar();
                        conexao.close();
                        dispose();

                    } else {
                        JOptionPane.showMessageDialog(null, "Erro ao Cadastrar Setor!");
                        limpar();
                        conexao.close();

                    }
                }

            }

            if (perfil.equals("Hidráulica - Trimestral")) {
                String sql = "update form_hidraulica_trimestral set id_setor = ?, setor_prev = ?, id_equi_set = ?, nome_equi_set = ?, cod_equi_set = ?, tipo_equi = ?, iduser = ?, nome_prev = ?, tipo_usuario = ?, tipo_prev = ?, tempo_dis = ? where id_hidraulica_trimestral = ?";
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtIdSetPrev.getText());
                pst.setString(2, txtSetPrev.getText());
                pst.setString(3, txtIdEquiSet.getText());
                pst.setString(4, txtNonEquiSet.getText());
                pst.setString(5, txtCodEquiSet.getText());
                pst.setString(6, txtTipEqui.getText());
                pst.setString(7, txtIdTecPrev.getText());
                pst.setString(8, txtTecPrev.getText());
                pst.setString(9, txtTipTec.getText());
                pst.setString(10, cboTipPrev.getSelectedItem().toString());
                pst.setString(11, txtDatPrev.getText());
                pst.setString(12, txtIdPre.getText());
                if ((txtIdSetPrev.getText().isEmpty()) || (txtSetPrev.getText().isEmpty()) || (txtIdTecPrev.getText().isEmpty()) || (txtIdEquiSet.getText().isEmpty()) || (txtNonEquiSet.getText().isEmpty()) || (txtCodEquiSet.getText().isEmpty()) || (txtTecPrev.getText().isEmpty()) || (cboTipPrev.getSelectedItem().toString().isEmpty()) || (txtDatPrev.getText().isEmpty())) {
                    JOptionPane.showMessageDialog(null, "Preencha os Campos Obrigatórios!");
                } else {
                    int Adicionar = pst.executeUpdate();

                    if (Adicionar > 0) {
                        JOptionPane.showMessageDialog(null, "Preventiva Cadastrada com Sucesso!");
                        limpar();
                        conexao.close();
                        dispose();

                    } else {
                        JOptionPane.showMessageDialog(null, "Erro ao Cadastrar Setor!");
                        limpar();
                        conexao.close();

                    }
                }

            }

            if (perfil.equals("Hidráulica - Semestral")) {
                String sql = "update form_hidraulica_semestral set id_setor = ?, setor_prev = ?, id_equi_set = ?, nome_equi_set = ?, cod_equi_set = ?, tipo_equi = ?, iduser = ?, nome_prev = ?, tipo_usuario = ?, tipo_prev = ?, tempo_dis = ? where id_hidraulica_semestral = ?";
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtIdSetPrev.getText());
                pst.setString(2, txtSetPrev.getText());
                pst.setString(3, txtIdEquiSet.getText());
                pst.setString(4, txtNonEquiSet.getText());
                pst.setString(5, txtCodEquiSet.getText());
                pst.setString(6, txtTipEqui.getText());
                pst.setString(7, txtIdTecPrev.getText());
                pst.setString(8, txtTecPrev.getText());
                pst.setString(9, txtTipTec.getText());
                pst.setString(10, cboTipPrev.getSelectedItem().toString());
                pst.setString(11, txtDatPrev.getText());
                pst.setString(12, txtIdPre.getText());
                if ((txtIdSetPrev.getText().isEmpty()) || (txtSetPrev.getText().isEmpty()) || (txtIdTecPrev.getText().isEmpty()) || (txtIdEquiSet.getText().isEmpty()) || (txtNonEquiSet.getText().isEmpty()) || (txtCodEquiSet.getText().isEmpty()) || (txtTecPrev.getText().isEmpty()) || (cboTipPrev.getSelectedItem().toString().isEmpty()) || (txtDatPrev.getText().isEmpty())) {
                    JOptionPane.showMessageDialog(null, "Preencha os Campos Obrigatórios!");
                } else {
                    int Adicionar = pst.executeUpdate();

                    if (Adicionar > 0) {
                        JOptionPane.showMessageDialog(null, "Preventiva Cadastrada com Sucesso!");
                        limpar();
                        conexao.close();
                        dispose();

                    } else {
                        JOptionPane.showMessageDialog(null, "Erro ao Cadastrar Setor!");
                        limpar();
                        conexao.close();

                    }
                }

            }

            if (perfil.equals("Refrigeração - Mensal")) {
                String sql = "update form_refrigeracao_mensal set id_setor = ?, setor_prev = ?, id_equi_set = ?, nome_equi_set = ?, cod_equi_set = ?, tipo_equi = ?, iduser = ?, nome_prev = ?, tipo_usuario = ?, tipo_prev = ?, tempo_dis = ? where id_refrigeracao_mensal = ?";
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtIdSetPrev.getText());
                pst.setString(2, txtSetPrev.getText());
                pst.setString(3, txtIdEquiSet.getText());
                pst.setString(4, txtNonEquiSet.getText());
                pst.setString(5, txtCodEquiSet.getText());
                pst.setString(6, txtTipEqui.getText());
                pst.setString(7, txtIdTecPrev.getText());
                pst.setString(8, txtTecPrev.getText());
                pst.setString(9, txtTipTec.getText());
                pst.setString(10, cboTipPrev.getSelectedItem().toString());
                pst.setString(11, txtDatPrev.getText());
                pst.setString(12, txtIdPre.getText());
                if ((txtIdSetPrev.getText().isEmpty()) || (txtSetPrev.getText().isEmpty()) || (txtIdTecPrev.getText().isEmpty()) || (txtIdEquiSet.getText().isEmpty()) || (txtNonEquiSet.getText().isEmpty()) || (txtCodEquiSet.getText().isEmpty()) || (txtTecPrev.getText().isEmpty()) || (cboTipPrev.getSelectedItem().toString().isEmpty()) || (txtDatPrev.getText().isEmpty())) {
                    JOptionPane.showMessageDialog(null, "Preencha os Campos Obrigatórios!");
                } else {
                    int Adicionar = pst.executeUpdate();

                    if (Adicionar > 0) {
                        JOptionPane.showMessageDialog(null, "Preventiva Cadastrada com Sucesso!");
                        limpar();
                        conexao.close();
                        dispose();

                    } else {
                        JOptionPane.showMessageDialog(null, "Erro ao Cadastrar Setor!");
                        limpar();
                        conexao.close();

                    }
                }

            }

            if (perfil.equals("Refrigeração - Trimestral")) {
                String sql = "update form_refrigeracao_trimestral set id_setor = ?, setor_prev = ?, id_equi_set = ?, nome_equi_set = ?, cod_equi_set = ?, tipo_equi = ?, iduser = ?, nome_prev = ?, tipo_usuario = ?, tipo_prev = ?, tempo_dis = ? where id_refrigeracao_trimestral = ?";
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtIdSetPrev.getText());
                pst.setString(2, txtSetPrev.getText());
                pst.setString(3, txtIdEquiSet.getText());
                pst.setString(4, txtNonEquiSet.getText());
                pst.setString(5, txtCodEquiSet.getText());
                pst.setString(6, txtTipEqui.getText());
                pst.setString(7, txtIdTecPrev.getText());
                pst.setString(8, txtTecPrev.getText());
                pst.setString(9, txtTipTec.getText());
                pst.setString(10, cboTipPrev.getSelectedItem().toString());
                pst.setString(11, txtDatPrev.getText());
                pst.setString(12, txtIdPre.getText());
                if ((txtIdSetPrev.getText().isEmpty()) || (txtSetPrev.getText().isEmpty()) || (txtIdTecPrev.getText().isEmpty()) || (txtIdEquiSet.getText().isEmpty()) || (txtNonEquiSet.getText().isEmpty()) || (txtCodEquiSet.getText().isEmpty()) || (txtTecPrev.getText().isEmpty()) || (cboTipPrev.getSelectedItem().toString().isEmpty()) || (txtDatPrev.getText().isEmpty())) {
                    JOptionPane.showMessageDialog(null, "Preencha os Campos Obrigatórios!");
                } else {
                    int Adicionar = pst.executeUpdate();

                    if (Adicionar > 0) {
                        JOptionPane.showMessageDialog(null, "Preventiva Cadastrada com Sucesso!");
                        limpar();
                        conexao.close();
                        dispose();

                    } else {
                        JOptionPane.showMessageDialog(null, "Erro ao Cadastrar Setor!");
                        limpar();
                        conexao.close();

                    }
                }

            }

            if (perfil.equals("Refrigeração - Semestral")) {
                String sql = "update form_refrigeracao_semestral set id_setor = ?, setor_prev = ?, id_equi_set = ?, nome_equi_set = ?, cod_equi_set = ?, tipo_equi = ?, iduser = ?, nome_prev = ?, tipo_usuario = ?, tipo_prev = ?, tempo_dis = ? where id_refrigeracao_semestral = ?";
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtIdSetPrev.getText());
                pst.setString(2, txtSetPrev.getText());
                pst.setString(3, txtIdEquiSet.getText());
                pst.setString(4, txtNonEquiSet.getText());
                pst.setString(5, txtCodEquiSet.getText());
                pst.setString(6, txtTipEqui.getText());
                pst.setString(7, txtIdTecPrev.getText());
                pst.setString(8, txtTecPrev.getText());
                pst.setString(9, txtTipTec.getText());
                pst.setString(10, cboTipPrev.getSelectedItem().toString());
                pst.setString(11, txtDatPrev.getText());
                pst.setString(12, txtIdPre.getText());
                if ((txtIdSetPrev.getText().isEmpty()) || (txtSetPrev.getText().isEmpty()) || (txtIdTecPrev.getText().isEmpty()) || (txtIdEquiSet.getText().isEmpty()) || (txtNonEquiSet.getText().isEmpty()) || (txtCodEquiSet.getText().isEmpty()) || (txtTecPrev.getText().isEmpty()) || (cboTipPrev.getSelectedItem().toString().isEmpty()) || (txtDatPrev.getText().isEmpty())) {
                    JOptionPane.showMessageDialog(null, "Preencha os Campos Obrigatórios!");
                } else {
                    int Adicionar = pst.executeUpdate();

                    if (Adicionar > 0) {
                        JOptionPane.showMessageDialog(null, "Preventiva Cadastrada com Sucesso!");
                        limpar();
                        conexao.close();
                        dispose();

                    } else {
                        JOptionPane.showMessageDialog(null, "Erro ao Cadastrar Setor!");
                        limpar();
                        conexao.close();
                        dispose();

                    }
                }

            }

        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    private void apagar_prev() {

        int confirma = JOptionPane.showConfirmDialog(null, "Tem Certeza que Deseja Apagar está Preventiva?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            String perfil = cboTipPrev.getSelectedItem().toString();
            try {
                if (perfil.equals("Eletrica - Mensal")) {
                    String sql = "delete from form_eletrica_mensal where id_form_ele_mensal = ?";
                    pst = conexao.prepareStatement(sql);
                    pst.setString(1, txtIdPre.getText());
                    if ((txtIdSetPrev.getText().isEmpty()) || (txtSetPrev.getText().isEmpty()) || (txtIdTecPrev.getText().isEmpty()) || (txtIdEquiSet.getText().isEmpty()) || (txtNonEquiSet.getText().isEmpty()) || (txtCodEquiSet.getText().isEmpty()) || (txtTecPrev.getText().isEmpty()) || (cboTipPrev.getSelectedItem().toString().isEmpty()) || (txtDatPrev.getText().isEmpty())) {
                        JOptionPane.showMessageDialog(null, "Preencha os Campos Obrigatórios!");
                    } else {
                        int Apagado = pst.executeUpdate();

                        if (Apagado > 0) {
                            JOptionPane.showMessageDialog(null, "Preventiva Apagada com Sucesso!");
                            limpar();
                            conexao.close();
                            dispose();

                        } else {
                            JOptionPane.showMessageDialog(null, "Erro ao Apagar Preventiva!");
                            limpar();
                            conexao.close();

                        }
                    }

                }
                if (perfil.equals("Eletrica - Trimestral")) {
                    String sql = "delete from form_eletrica_mensal where id_form_ele_trimestral = ?";
                    pst = conexao.prepareStatement(sql);
                    pst.setString(1, txtIdPre.getText());
                    if ((txtIdSetPrev.getText().isEmpty()) || (txtSetPrev.getText().isEmpty()) || (txtIdTecPrev.getText().isEmpty()) || (txtIdEquiSet.getText().isEmpty()) || (txtNonEquiSet.getText().isEmpty()) || (txtCodEquiSet.getText().isEmpty()) || (txtTecPrev.getText().isEmpty()) || (cboTipPrev.getSelectedItem().toString().isEmpty()) || (txtDatPrev.getText().isEmpty())) {
                        JOptionPane.showMessageDialog(null, "Preencha os Campos Obrigatórios!");
                    } else {
                        int Apagado = pst.executeUpdate();

                        if (Apagado > 0) {
                            JOptionPane.showMessageDialog(null, "Preventiva Apagada com Sucesso!");
                            limpar();
                            conexao.close();
                            dispose();

                        } else {
                            JOptionPane.showMessageDialog(null, "Erro ao Apagar Preventiva!");
                            limpar();
                            conexao.close();
                        }
                    }

                }
                if (perfil.equals("Eletrica - Semestral")) {
                    String sql = "delete from form_eletrica_mensal where id_form_ele_semestral = ?";
                    pst = conexao.prepareStatement(sql);
                    pst.setString(1, txtIdPre.getText());
                    if ((txtIdSetPrev.getText().isEmpty()) || (txtSetPrev.getText().isEmpty()) || (txtIdTecPrev.getText().isEmpty()) || (txtIdEquiSet.getText().isEmpty()) || (txtNonEquiSet.getText().isEmpty()) || (txtCodEquiSet.getText().isEmpty()) || (txtTecPrev.getText().isEmpty()) || (cboTipPrev.getSelectedItem().toString().isEmpty()) || (txtDatPrev.getText().isEmpty())) {
                        JOptionPane.showMessageDialog(null, "Preencha os Campos Obrigatórios!");
                    } else {
                        int Apagado = pst.executeUpdate();

                        if (Apagado > 0) {
                            JOptionPane.showMessageDialog(null, "Preventiva Apagada com Sucesso!");
                            limpar();
                            conexao.close();
                            dispose();

                        } else {
                            JOptionPane.showMessageDialog(null, "Erro ao Apagar Preventiva!");
                            limpar();
                            conexao.close();

                        }
                    }

                }

                if (perfil.equals("Civil - Mensal")) {
                    String sql = "delete from form_civil_mensal where id_civil_mensal = ?";
                    pst = conexao.prepareStatement(sql);
                    pst.setString(1, txtIdPre.getText());
                    if ((txtIdSetPrev.getText().isEmpty()) || (txtSetPrev.getText().isEmpty()) || (txtIdTecPrev.getText().isEmpty()) || (txtIdEquiSet.getText().isEmpty()) || (txtNonEquiSet.getText().isEmpty()) || (txtCodEquiSet.getText().isEmpty()) || (txtTecPrev.getText().isEmpty()) || (cboTipPrev.getSelectedItem().toString().isEmpty()) || (txtDatPrev.getText().isEmpty())) {
                        JOptionPane.showMessageDialog(null, "Preencha os Campos Obrigatórios!");
                    } else {
                        int Apagado = pst.executeUpdate();

                        if (Apagado > 0) {
                            JOptionPane.showMessageDialog(null, "Preventiva Apagada com Sucesso!");
                            limpar();
                            conexao.close();
                            dispose();

                        } else {
                            JOptionPane.showMessageDialog(null, "Erro ao Apagar Preventiva!");
                            limpar();
                            conexao.close();

                        }
                    }

                }

                if (perfil.equals("Civil - Trimestral")) {
                    String sql = "delete from form_civil_trimestral where id_civil_trimestral = ?";
                    pst = conexao.prepareStatement(sql);
                    pst.setString(1, txtIdPre.getText());
                    if ((txtIdSetPrev.getText().isEmpty()) || (txtSetPrev.getText().isEmpty()) || (txtIdTecPrev.getText().isEmpty()) || (txtIdEquiSet.getText().isEmpty()) || (txtNonEquiSet.getText().isEmpty()) || (txtCodEquiSet.getText().isEmpty()) || (txtTecPrev.getText().isEmpty()) || (cboTipPrev.getSelectedItem().toString().isEmpty()) || (txtDatPrev.getText().isEmpty())) {
                        JOptionPane.showMessageDialog(null, "Preencha os Campos Obrigatórios!");
                    } else {
                        int Apagado = pst.executeUpdate();

                        if (Apagado > 0) {
                            JOptionPane.showMessageDialog(null, "Preventiva Apagada com Sucesso!");
                            limpar();
                            conexao.close();
                            dispose();

                        } else {
                            JOptionPane.showMessageDialog(null, "Erro ao Apagar Preventiva!");
                            limpar();
                            conexao.close();

                        }
                    }

                }

                if (perfil.equals("Civil - Semestral")) {
                    String sql = "delete from form_civil_semestral where id_civil_semestral = ?";
                    pst = conexao.prepareStatement(sql);
                    pst.setString(1, txtIdPre.getText());
                    if ((txtIdSetPrev.getText().isEmpty()) || (txtSetPrev.getText().isEmpty()) || (txtIdTecPrev.getText().isEmpty()) || (txtIdEquiSet.getText().isEmpty()) || (txtNonEquiSet.getText().isEmpty()) || (txtCodEquiSet.getText().isEmpty()) || (txtTecPrev.getText().isEmpty()) || (cboTipPrev.getSelectedItem().toString().isEmpty()) || (txtDatPrev.getText().isEmpty())) {
                        JOptionPane.showMessageDialog(null, "Preencha os Campos Obrigatórios!");
                    } else {
                        int Apagado = pst.executeUpdate();

                        if (Apagado > 0) {
                            JOptionPane.showMessageDialog(null, "Preventiva Apagada com Sucesso!");
                            limpar();
                            conexao.close();
                            dispose();

                        } else {
                            JOptionPane.showMessageDialog(null, "Erro ao Apagar Preventiva!");
                            limpar();
                            conexao.close();

                        }
                    }

                }

                if (perfil.equals("Hidráulica - Mensal")) {
                    String sql = "delete from form_hidraulica_mensal where id_hidraulica_mensal = ?";
                    pst = conexao.prepareStatement(sql);
                    pst.setString(1, txtIdPre.getText());
                    if ((txtIdSetPrev.getText().isEmpty()) || (txtSetPrev.getText().isEmpty()) || (txtIdTecPrev.getText().isEmpty()) || (txtIdEquiSet.getText().isEmpty()) || (txtNonEquiSet.getText().isEmpty()) || (txtCodEquiSet.getText().isEmpty()) || (txtTecPrev.getText().isEmpty()) || (cboTipPrev.getSelectedItem().toString().isEmpty()) || (txtDatPrev.getText().isEmpty())) {
                        JOptionPane.showMessageDialog(null, "Preencha os Campos Obrigatórios!");
                    } else {
                        int Apagado = pst.executeUpdate();

                        if (Apagado > 0) {
                            JOptionPane.showMessageDialog(null, "Preventiva Apagada com Sucesso!");
                            limpar();
                            conexao.close();
                            dispose();

                        } else {
                            JOptionPane.showMessageDialog(null, "Erro ao Apagar Preventiva!");
                            limpar();
                            conexao.close();

                        }
                    }

                }

                if (perfil.equals("Hidráulica - Trimestral")) {
                    String sql = "delete from form_hidraulica_trimestral where id_hidraulica_trimestral = ?";
                    pst = conexao.prepareStatement(sql);
                    pst.setString(1, txtIdPre.getText());
                    if ((txtIdSetPrev.getText().isEmpty()) || (txtSetPrev.getText().isEmpty()) || (txtIdTecPrev.getText().isEmpty()) || (txtIdEquiSet.getText().isEmpty()) || (txtNonEquiSet.getText().isEmpty()) || (txtCodEquiSet.getText().isEmpty()) || (txtTecPrev.getText().isEmpty()) || (cboTipPrev.getSelectedItem().toString().isEmpty()) || (txtDatPrev.getText().isEmpty())) {
                        JOptionPane.showMessageDialog(null, "Preencha os Campos Obrigatórios!");
                    } else {
                        int Apagado = pst.executeUpdate();

                        if (Apagado > 0) {
                            JOptionPane.showMessageDialog(null, "Preventiva Apagada com Sucesso!");
                            limpar();
                            conexao.close();
                            dispose();

                        } else {
                            JOptionPane.showMessageDialog(null, "Erro ao Apagar Preventiva!");
                            limpar();
                            conexao.close();
                        }
                    }

                }

                if (perfil.equals("Hidráulica - Semestral")) {
                    String sql = "delete from form_hidraulica_semestral where id_hidraulica_semestral = ?";
                    pst = conexao.prepareStatement(sql);
                    pst.setString(1, txtIdPre.getText());
                    if ((txtIdSetPrev.getText().isEmpty()) || (txtSetPrev.getText().isEmpty()) || (txtIdTecPrev.getText().isEmpty()) || (txtIdEquiSet.getText().isEmpty()) || (txtNonEquiSet.getText().isEmpty()) || (txtCodEquiSet.getText().isEmpty()) || (txtTecPrev.getText().isEmpty()) || (cboTipPrev.getSelectedItem().toString().isEmpty()) || (txtDatPrev.getText().isEmpty())) {
                        JOptionPane.showMessageDialog(null, "Preencha os Campos Obrigatórios!");
                    } else {
                        int Apagado = pst.executeUpdate();

                        if (Apagado > 0) {
                            JOptionPane.showMessageDialog(null, "Preventiva Apagada com Sucesso!");
                            limpar();
                            conexao.close();
                            dispose();

                        } else {
                            JOptionPane.showMessageDialog(null, "Erro ao Apagar Preventiva!");
                            limpar();
                            conexao.close();

                        }
                    }

                }

                if (perfil.equals("Refrigeração - Mensal")) {
                    String sql = "delete from form_refrigeracao_mensal where id_refrigeracao_mensal = ?";
                    pst = conexao.prepareStatement(sql);
                    pst.setString(1, txtIdPre.getText());
                    if ((txtIdSetPrev.getText().isEmpty()) || (txtSetPrev.getText().isEmpty()) || (txtIdTecPrev.getText().isEmpty()) || (txtIdEquiSet.getText().isEmpty()) || (txtNonEquiSet.getText().isEmpty()) || (txtCodEquiSet.getText().isEmpty()) || (txtTecPrev.getText().isEmpty()) || (cboTipPrev.getSelectedItem().toString().isEmpty()) || (txtDatPrev.getText().isEmpty())) {
                        JOptionPane.showMessageDialog(null, "Preencha os Campos Obrigatórios!");
                    } else {
                        int Apagado = pst.executeUpdate();

                        if (Apagado > 0) {
                            JOptionPane.showMessageDialog(null, "Preventiva Apagada com Sucesso!");
                            limpar();
                            conexao.close();
                            dispose();

                        } else {
                            JOptionPane.showMessageDialog(null, "Erro ao Apagar Preventiva!");
                            limpar();
                            conexao.close();
                        }
                    }

                }

                if (perfil.equals("Refrigeração - Trimestral")) {
                    String sql = "delete from form_refrigeracao_trimestral where id_refrigeracao_trimestral = ?";
                    pst = conexao.prepareStatement(sql);
                    pst.setString(1, txtIdPre.getText());
                    if ((txtIdSetPrev.getText().isEmpty()) || (txtSetPrev.getText().isEmpty()) || (txtIdTecPrev.getText().isEmpty()) || (txtIdEquiSet.getText().isEmpty()) || (txtNonEquiSet.getText().isEmpty()) || (txtCodEquiSet.getText().isEmpty()) || (txtTecPrev.getText().isEmpty()) || (cboTipPrev.getSelectedItem().toString().isEmpty()) || (txtDatPrev.getText().isEmpty())) {
                        JOptionPane.showMessageDialog(null, "Preencha os Campos Obrigatórios!");
                    } else {
                        int Apagado = pst.executeUpdate();

                        if (Apagado > 0) {
                            JOptionPane.showMessageDialog(null, "Preventiva Apagada com Sucesso!");
                            limpar();
                            conexao.close();
                            dispose();

                        } else {
                            JOptionPane.showMessageDialog(null, "Erro ao Apagar Preventiva!");
                            limpar();
                            conexao.close();

                        }
                    }

                }

                if (perfil.equals("Refrigeração - Semestral")) {
                    String sql = "delete from form_refrigeracao_semestral where id_refrigeracao_semestral = ?";
                    pst = conexao.prepareStatement(sql);
                    pst.setString(1, txtIdPre.getText());
                    if ((txtIdSetPrev.getText().isEmpty()) || (txtSetPrev.getText().isEmpty()) || (txtIdTecPrev.getText().isEmpty()) || (txtIdEquiSet.getText().isEmpty()) || (txtNonEquiSet.getText().isEmpty()) || (txtCodEquiSet.getText().isEmpty()) || (txtTecPrev.getText().isEmpty()) || (cboTipPrev.getSelectedItem().toString().isEmpty()) || (txtDatPrev.getText().isEmpty())) {
                        JOptionPane.showMessageDialog(null, "Preencha os Campos Obrigatórios!");
                    } else {
                        int Apagado = pst.executeUpdate();

                        if (Apagado > 0) {
                            JOptionPane.showMessageDialog(null, "Preventiva Apagada com Sucesso!");
                            limpar();
                            conexao.close();
                            dispose();

                        } else {
                            JOptionPane.showMessageDialog(null, "Erro ao Apagar Preventiva!");
                            limpar();
                            conexao.close();

                        }
                    }

                }

            } catch (HeadlessException | SQLException e) {
                JOptionPane.showMessageDialog(null, e);
            }

        }
    }

    /**
     * Método `selecionarPreventiva`. Este método determina automaticamente as
     * opções disponíveis na combo box `cboTipPrev` com base no tipo de
     * equipamento selecionado (`txtTipEqui`). Ele extrai a primeira palavra do
     * tipo de equipamento e configura a lista de opções de manutenção
     * preventiva correspondentes.
     *
     * Funcionalidades: - **Extração do Tipo de Equipamento**: - Usa
     * `StringBuffer` para armazenar a primeira palavra do texto `txtTipEqui`. -
     * Aplica `split(" ")[0]` para extrair apenas o primeiro termo do tipo de
     * equipamento.
     *
     * - **Definição das Opções de Preventiva**: - Com base no tipo de
     * equipamento (`Elétrica`, `Civil`, `Hidráulica`, `Refrigeração`),
     * configura `cboTipPrev` com os tipos de preventiva correspondentes. -
     * Redefine a `DefaultComboBoxModel()` antes de adicionar os itens,
     * garantindo que as opções sejam atualizadas corretamente.
     *
     * - **Tratamento de Casos Não Reconhecidos**: - Se nenhum tipo válido for
     * encontrado, exibe uma mensagem de alerta `"Nenhuma Opção Encontrada"`. -
     * Chama `formTabela()` para manter a interface organizada.
     *
     * Fluxo do Método: 1. Obtém a primeira palavra do tipo de equipamento
     * (`txtTipEqui`). 2. Determina qual conjunto de opções de preventiva deve
     * ser inserido na `cboTipPrev`. 3. Define a lista de opções correspondente
     * ao tipo de equipamento. 4. Se o tipo não for reconhecido, exibe uma
     * mensagem ao usuário e ajusta a tabela.
     */
    private void selecionarPreventiva() {

        buffer = new StringBuffer();

        buffer.append(txtTipEqui.getText().split(" ")[0]);

        switch (buffer.toString()) {

            case "Elétrica":
                cboTipPrev.setModel(new DefaultComboBoxModel());
                String cbo1[] = {"-Selecione-", "Eletrica - Mensal", "Eletrica - Trimestral", "Eletrica - Semestral"};
                for (String string : cbo1) {
                    cboTipPrev.addItem(string);
                }
                break;

            case "Civil":
                cboTipPrev.setModel(new DefaultComboBoxModel());
                String cbo2[] = {"-Selecione-", "Civil - Mensal", "Civil - Trimestral", "Civil - Semestral"};
                for (String string : cbo2) {
                    cboTipPrev.addItem(string);
                }
                break;

            case "Hidráulica":
                cboTipPrev.setModel(new DefaultComboBoxModel());
                String cbo3[] = {"-Selecione-", "Hidráulica - Mensal", "Hidráulica - Trimestral", "Hidráulica - Semestral"};
                for (String string : cbo3) {
                    cboTipPrev.addItem(string);
                }
                break;

            case "Refrigeração":
                cboTipPrev.setModel(new DefaultComboBoxModel());
                String cbo4[] = {"-Selecione-", "Refrigeração - Mensal", "Refrigeração - Trimestral", "Refrigeração - Semestral"};
                for (String string : cbo4) {
                    cboTipPrev.addItem(string);
                }
                break;

            default:
                JOptionPane.showMessageDialog(null, "Nenhuma Opção Encontada");
                formTabela();
        }

    }

    /**
     * Método `selecionarPreventivaCbo`. Este método determina automaticamente
     * as opções disponíveis na combo box `cboTipPrev` com base no tipo de
     * equipamento selecionado (`txtTipEqui`). Ele extrai a primeira palavra do
     * tipo de equipamento e configura a lista de opções de manutenção
     * preventiva correspondentes, além de definir um item selecionado.
     *
     * Funcionalidades: - **Extração do Tipo de Equipamento**: - Usa
     * `StringBuffer` para armazenar a primeira palavra do texto `txtTipEqui`. -
     * Aplica `split(" ")[0]` para extrair apenas o primeiro termo do tipo de
     * equipamento.
     *
     * - **Definição das Opções de Preventiva**: - Com base no tipo de
     * equipamento (`Elétrica`, `Civil`, `Hidráulica`, `Refrigeração`),
     * configura `cboTipPrev` com os tipos de preventiva correspondentes. -
     * Redefine a `DefaultComboBoxModel()` antes de adicionar os itens,
     * garantindo que as opções sejam atualizadas corretamente. - Seleciona o
     * item `buffer1.toString()`, garantindo que um valor seja predefinido na
     * combo box.
     *
     * - **Tratamento de Casos Não Reconhecidos**: - Se nenhum tipo válido for
     * encontrado, exibe uma mensagem de alerta `"Nenhuma Opção Encontrada"`. -
     * Chama `formTabela()` para manter a interface organizada.
     *
     * **Possível Correção**: - A variável `buffer1` não é inicializada no
     * código. Certifique-se de que ela contém um valor válido antes de definir
     * a seleção.
     *
     * Fluxo do Método: 1. Obtém a primeira palavra do tipo de equipamento
     * (`txtTipEqui`). 2. Determina qual conjunto de opções de preventiva deve
     * ser inserido na `cboTipPrev`. 3. Define a lista de opções correspondente
     * ao tipo de equipamento e seleciona um item. 4. Se o tipo não for
     * reconhecido, exibe uma mensagem ao usuário e ajusta a tabela.
     */
    private void selecionarPreventivaCbo() {

        buffer = new StringBuffer();

        buffer.append(txtTipEqui.getText().split(" ")[0]);

        switch (buffer.toString()) {

            case "Elétrica":
                cboTipPrev.setModel(new DefaultComboBoxModel());
                String cbo1[] = {"-Selecione-", "Eletrica - Mensal", "Eletrica - Trimestral", "Eletrica - Semestral"};
                for (String string : cbo1) {
                    cboTipPrev.addItem(string);
                }
                cboTipPrev.setSelectedItem(buffer1.toString());
                break;

            case "Civil":
                cboTipPrev.setModel(new DefaultComboBoxModel());
                String cbo2[] = {"-Selecione-", "Civil - Mensal", "Civil - Trimestral", "Civil - Semestral"};
                for (String string : cbo2) {
                    cboTipPrev.addItem(string);
                }
                cboTipPrev.setSelectedItem(buffer1.toString());
                break;

            case "Hidráulica":
                cboTipPrev.setModel(new DefaultComboBoxModel());
                String cbo3[] = {"-Selecione-", "Hidráulica - Mensal", "Hidráulica - Trimestral", "Hidráulica - Semestral"};
                for (String string : cbo3) {
                    cboTipPrev.addItem(string);
                }
                cboTipPrev.setSelectedItem(buffer1.toString());
                break;

            case "Refrigeração":
                cboTipPrev.setModel(new DefaultComboBoxModel());
                String cbo4[] = {"-Selecione-", "Refrigeração - Mensal", "Refrigeração - Trimestral", "Refrigeração - Semestral"};
                for (String string : cbo4) {
                    cboTipPrev.addItem(string);
                }
                cboTipPrev.setSelectedItem(buffer1.toString());
                break;

            default:
                JOptionPane.showMessageDialog(null, "Nenhuma Opção Encontada");
                formTabela();
        }

    }

    /**
     * Método `limpar`. Este método redefine os campos da interface gráfica para
     * seus valores padrões, garantindo que nenhuma informação residual
     * permaneça após uma operação anterior. Ele é útil para preparar a
     * interface para uma nova entrada de dados.
     *
     * Funcionalidades: - **Limpeza da Tabela**: - `((DefaultTableModel)
     * tblEquiSet.getModel()).setRowCount(0)`: Remove todas as linhas da tabela
     * `tblEquiSet`, garantindo que ela fique vazia.
     *
     * - **Reset de Campos de Texto**: - Define os seguintes campos de texto
     * como `null`: - `txtIdSetPrev`, `txtSetPrev`, `txtIdEquiSet`,
     * `txtNonEquiSet`, `txtCodEquiSet`. - `txtIdTecPrev`, `txtTecPrev`,
     * `txtTipEqui`, `txtTipTec`, `txtIdPre`, `txtDatPrev`.
     *
     * - **Reset da Combobox**: - `cboTipPrev.setSelectedItem("-Selecione-")`:
     * Define a combobox para sua opção padrão, garantindo que nenhum tipo de
     * preventiva permaneça selecionado.
     *
     * Fluxo do Método: 1. Limpa os dados da tabela `tblEquiSet`. 2. Reseta os
     * valores dos campos de entrada. 3. Define a combobox no estado inicial.
     */
    private void limpar() {

        ((DefaultTableModel) tblEquiSet.getModel()).setRowCount(0);
        txtIdSetPrev.setText(null);
        txtSetPrev.setText(null);
        txtIdEquiSet.setText(null);
        txtNonEquiSet.setText(null);
        txtCodEquiSet.setText(null);
        txtIdTecPrev.setText(null);
        txtTecPrev.setText(null);
        txtTipEqui.setText(null);
        txtTipTec.setText(null);
        txtIdPre.setText(null);
        cboTipPrev.setSelectedItem("-Selecione-");
        txtDatPrev.setText(null);
        rbtTecnico.setEnabled(false);
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
        txtPesEqui = new javax.swing.JTextField();
        txtIdSetPrev = new javax.swing.JTextField();
        txtSetPrev = new javax.swing.JTextField();
        txtIdTecPrev = new javax.swing.JTextField();
        txtTecPrev = new javax.swing.JTextField();
        txtDatPrev = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblEquiSet = new javax.swing.JTable();
        btnCadPre = new javax.swing.JButton();
        btnAlrPre = new javax.swing.JButton();
        cboTipPrev = new javax.swing.JComboBox<>();
        txtIdEquiSet = new javax.swing.JTextField();
        txtCodEquiSet = new javax.swing.JTextField();
        txtNonEquiSet = new javax.swing.JTextField();
        txtTipTec = new javax.swing.JTextField();
        txtTipEqui = new javax.swing.JTextField();
        btnApaPre = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        txtIdPre = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        rbtSetor = new javax.swing.JRadioButton();
        rbtEquipamento = new javax.swing.JRadioButton();
        rbtTecnico = new javax.swing.JRadioButton();
        lblButton = new com.prjmanutencao.telas.LabelPersonalizada();
        lblPesqGeral = new javax.swing.JLabel();
        lblDisPrev = new com.prjmanutencao.telas.LabelPersonalizada();
        jLabel1 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Distribuição de Preventivas");
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

        txtPesEqui.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtPesEqui.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPesEquiKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPesEquiKeyTyped(evt);
            }
        });
        getContentPane().add(txtPesEqui);
        txtPesEqui.setBounds(359, 158, 232, 28);

        txtIdSetPrev.setEditable(false);
        txtIdSetPrev.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtIdSetPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdSetPrevActionPerformed(evt);
            }
        });
        txtIdSetPrev.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtIdSetPrevKeyReleased(evt);
            }
        });
        getContentPane().add(txtIdSetPrev);
        txtIdSetPrev.setBounds(80, 325, 126, 28);

        txtSetPrev.setEditable(false);
        txtSetPrev.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(txtSetPrev);
        txtSetPrev.setBounds(80, 377, 280, 28);

        txtIdTecPrev.setEditable(false);
        txtIdTecPrev.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(txtIdTecPrev);
        txtIdTecPrev.setBounds(634, 325, 126, 28);

        txtTecPrev.setEditable(false);
        txtTecPrev.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(txtTecPrev);
        txtTecPrev.setBounds(634, 429, 280, 28);

        txtDatPrev.setEditable(false);
        txtDatPrev.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        txtDatPrev.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(txtDatPrev);
        txtDatPrev.setBounds(634, 481, 126, 28);

        jScrollPane1.setEnabled(false);

        tblEquiSet = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIdex, int colIndex){
                return false;
            }
        };
        tblEquiSet.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "N° Equip.", "Nome ", "Cód. E.", "Equip."
            }
        ));
        tblEquiSet.getTableHeader().setReorderingAllowed(false);
        tblEquiSet.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblEquiSetMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblEquiSet);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(180, 197, 600, 98);

        btnCadPre.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/48755_add_document_add_document.png"))); // NOI18N
        btnCadPre.setToolTipText("Cadastrar Preventiva");
        btnCadPre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadPreActionPerformed(evt);
            }
        });
        getContentPane().add(btnCadPre);
        btnCadPre.setBounds(550, 536, 64, 64);

        btnAlrPre.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/48757_document_edit_edit_document.png"))); // NOI18N
        btnAlrPre.setToolTipText("Alterar Preventiva");
        btnAlrPre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlrPreActionPerformed(evt);
            }
        });
        getContentPane().add(btnAlrPre);
        btnAlrPre.setBounds(660, 536, 64, 64);

        getContentPane().add(cboTipPrev);
        cboTipPrev.setBounds(402, 325, 184, 28);

        txtIdEquiSet.setEditable(false);
        txtIdEquiSet.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(txtIdEquiSet);
        txtIdEquiSet.setBounds(80, 429, 126, 28);

        txtCodEquiSet.setEditable(false);
        txtCodEquiSet.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(txtCodEquiSet);
        txtCodEquiSet.setBounds(233, 429, 126, 28);

        txtNonEquiSet.setEditable(false);
        txtNonEquiSet.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(txtNonEquiSet);
        txtNonEquiSet.setBounds(80, 481, 280, 28);

        txtTipTec.setEditable(false);
        txtTipTec.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(txtTipTec);
        txtTipTec.setBounds(634, 377, 170, 28);

        txtTipEqui.setEditable(false);
        txtTipEqui.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(txtTipEqui);
        txtTipEqui.setBounds(80, 534, 170, 28);

        btnApaPre.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/48756_delete_delete_document_document.png"))); // NOI18N
        btnApaPre.setToolTipText("Excluir Preventiva");
        btnApaPre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnApaPreActionPerformed(evt);
            }
        });
        getContentPane().add(btnApaPre);
        btnApaPre.setBounds(772, 536, 64, 64);

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/48755_add_document_add_document.png"))); // NOI18N
        getContentPane().add(jLabel3);
        jLabel3.setBounds(724, 60, 64, 64);

        txtIdPre.setEditable(false);
        txtIdPre.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(txtIdPre);
        txtIdPre.setBounds(786, 481, 126, 28);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 51));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("N° Preventiva");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(786, 460, 126, 20);

        jLabel2.setFont(new java.awt.Font("Arial", 1, 44)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 51));
        jLabel2.setText("Distribuição de Preventivas");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(146, 60, 574, 60);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 51));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Tipo de Equipamento");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(80, 513, 170, 20);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 51));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Setor");
        getContentPane().add(jLabel6);
        jLabel6.setBounds(80, 356, 280, 20);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 51));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Código Equipamento");
        getContentPane().add(jLabel7);
        jLabel7.setBounds(233, 408, 126, 20);

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 51));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Nome Equipamento");
        getContentPane().add(jLabel8);
        jLabel8.setBounds(80, 460, 280, 20);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 51));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("N° Equipamento");
        getContentPane().add(jLabel9);
        jLabel9.setBounds(80, 408, 126, 20);

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 0, 51));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Área Preventiva");
        getContentPane().add(jLabel10);
        jLabel10.setBounds(402, 305, 184, 20);

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 0, 51));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("R.E Técnico");
        getContentPane().add(jLabel11);
        jLabel11.setBounds(634, 305, 126, 20);

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 0, 51));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("Especialidade Técnico");
        getContentPane().add(jLabel12);
        jLabel12.setBounds(634, 356, 170, 20);

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 0, 51));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("Nome");
        getContentPane().add(jLabel13);
        jLabel13.setBounds(634, 408, 280, 20);

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 0, 51));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("Data");
        getContentPane().add(jLabel14);
        jLabel14.setBounds(634, 460, 126, 20);

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(0, 0, 51));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("Codigo Setor");
        getContentPane().add(jLabel15);
        jLabel15.setBounds(80, 305, 126, 20);

        buttonGroup1.add(rbtSetor);
        rbtSetor.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        rbtSetor.setForeground(new java.awt.Color(0, 0, 51));
        rbtSetor.setText("Setor");
        rbtSetor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtSetorActionPerformed(evt);
            }
        });
        getContentPane().add(rbtSetor);
        rbtSetor.setBounds(800, 197, 110, 28);

        buttonGroup1.add(rbtEquipamento);
        rbtEquipamento.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        rbtEquipamento.setForeground(new java.awt.Color(0, 0, 51));
        rbtEquipamento.setText("Equipamento");
        rbtEquipamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtEquipamentoActionPerformed(evt);
            }
        });
        getContentPane().add(rbtEquipamento);
        rbtEquipamento.setBounds(800, 232, 110, 28);

        buttonGroup1.add(rbtTecnico);
        rbtTecnico.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        rbtTecnico.setForeground(new java.awt.Color(0, 0, 51));
        rbtTecnico.setText("Técnico");
        rbtTecnico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtTecnicoActionPerformed(evt);
            }
        });
        getContentPane().add(rbtTecnico);
        rbtTecnico.setBounds(800, 267, 110, 28);
        getContentPane().add(lblButton);
        lblButton.setBounds(530, 528, 410, 80);

        lblPesqGeral.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblPesqGeral.setForeground(new java.awt.Color(0, 0, 51));
        lblPesqGeral.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPesqGeral.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/search.png"))); // NOI18N
        lblPesqGeral.setText("Técnico");
        getContentPane().add(lblPesqGeral);
        lblPesqGeral.setBounds(274, 158, 80, 28);
        getContentPane().add(lblDisPrev);
        lblDisPrev.setBounds(22, 140, 950, 480);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/telas/imagens_fundo/Os0.jpg"))); // NOI18N
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        getContentPane().add(jLabel1);
        jLabel1.setBounds(-6, -10, 1010, 660);

        setBounds(0, 0, 1007, 672);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Método `txtPesEquiKeyReleased`. Este método é acionado automaticamente
     * quando o usuário digita uma tecla dentro do campo de pesquisa
     * `txtPesEqui`. Ele detecta qual tipo de busca deve ser realizado com base
     * na variável `preve` e executa a pesquisa correspondente, atualizando
     * dinamicamente os dados na tabela `tblEquiSet`.
     *
     * Funcionalidades: - **Detecção de Tecla Pressionada**: - O evento
     * `KeyReleased` é disparado quando uma tecla é solta dentro do campo de
     * pesquisa. - Isso permite realizar buscas dinâmicas sem a necessidade de
     * pressionar "Enter".
     *
     * - **Execução Condicional**: - Determina o tipo de busca com base no valor
     * de `preve`: - `"setor"` → Chama `pesquisar_prev()` para buscar setores. -
     * `"equipamento"` → Chama `pesquisar_equipamento()` para buscar
     * equipamentos. - `"tecnico"` → Chama `pesquisar_tecnico_prev()` para
     * buscar técnicos.
     *
     * - **Atualização da Tabela**: - Após a pesquisa, invoca `formTabela()`
     * para organizar visualmente os dados exibidos na tabela `tblEquiSet`.
     *
     * Fluxo do Método: 1. Detecta a tecla pressionada e verifica o valor da
     * variável `preve`. 2. Executa a pesquisa correspondente com base no
     * contexto. 3. Atualiza a tabela com os novos resultados e ajusta sua
     * exibição.
     */
    private void txtPesEquiKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesEquiKeyReleased

        if (preve.equals("setor")) {
            pesquisar_prev();
            formTabela();
        }
        if (preve.equals("equipamento")) {
            pesquisar_equipamento();
            formTabela();
        }
        if (preve.equals("tecnico")) {
            pesquisar_tecnico_prev();
            formTabela();
        }
    }//GEN-LAST:event_txtPesEquiKeyReleased

    /**
     * Método `tblEquiSetMouseClicked`. Este método é acionado automaticamente
     * quando o usuário clica em uma linha da tabela `tblEquiSet`. Ele limpa o
     * campo de pesquisa, identifica o tipo de busca com base na variável
     * `preve` e executa ações apropriadas, garantindo que os dados sejam
     * atualizados conforme o contexto da seleção.
     *
     * Funcionalidades: - **Limpeza do Campo de Pesquisa**: - Define
     * `txtPesEqui.setText("")`, removendo qualquer texto presente no campo de
     * pesquisa.
     *
     * - **Execução Condicional Baseada em `preve`**: - Se
     * `preve.equals("setor")`: - Chama `setarPreventiva()` para definir os
     * valores do setor selecionado. - Executa `pesquisarSetorIni()` para
     * atualizar os setores na tabela. - Chama `formTabela()` para garantir que
     * a interface esteja visualmente organizada. - Se
     * `preve.equals("equipamento")`: - Chama `setarPreventiva()` para definir
     * os valores do equipamento selecionado. - Executa
     * `pesquisarEquipamentoIni()` para buscar equipamentos associados. - Ajusta
     * a tabela com `formTabela()`. - Se `preve.equals("tecnico")`: - Chama
     * `setarPreventiva()` para configurar os dados do técnico. - Executa
     * `pesquisarTecnicoIni()` para obter informações dos técnicos cadastrados.
     * - Organiza a tabela com `formTabela()`.
     *
     * Fluxo do Método: 1. Detecta o clique do usuário na tabela. 2. Limpa o
     * campo de pesquisa `txtPesEqui`. 3. Executa ações específicas com base no
     * tipo de busca (`setor`, `equipamento` ou `técnico`). 4. Atualiza a tabela
     * para garantir a exibição correta dos dados.
     */
    private void tblEquiSetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblEquiSetMouseClicked

        txtPesEqui.setText("");
        if (preve.equals("setor")) {
            setarPreventiva();
            pesquisarSetorIni();
            formTabela();

        }
        if (preve.equals("equipamento")) {
            setarPreventiva();
            pesquisarEquipamentoIni();
            formTabela();
        }
        if (preve.equals("tecnico")) {
            setarPreventiva();
            pesquisarTecnicoIni();
            formTabela();
        }
    }//GEN-LAST:event_tblEquiSetMouseClicked

    /**
     * Método `btnCadPreActionPerformed`. Este método é acionado automaticamente
     * quando o usuário clica no botão de cadastro de preventiva (`btnCadPre`).
     * Ele chama o método `iniciar_prev()`, que realiza a validação dos campos e
     * a inserção da manutenção preventiva no banco de dados.
     *
     * Funcionalidades: - **Ação no Clique do Botão**: - Quando o usuário
     * pressiona `btnCadPre`, o evento `ActionEvent` é disparado e chama
     * `iniciar_prev()`.
     *
     * - **Execução do Cadastro**: - `iniciar_prev()` valida os campos
     * obrigatórios antes de permitir o cadastro da preventiva. - Caso os dados
     * estejam corretos, a manutenção preventiva é registrada no banco de dados.
     *
     * Fluxo do Método: 1. Detecta o clique do usuário no botão `btnCadPre`. 2.
     * Chama `iniciar_prev()` para iniciar o processo de validação e cadastro.
     */
    private void btnCadPreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadPreActionPerformed

        iniciar_prev();
    }//GEN-LAST:event_btnCadPreActionPerformed

    /**
     * Método `btnAlrPreActionPerformed`. Este método é acionado automaticamente
     * quando o usuário clica no botão de alteração de preventiva (`btnAlrPre`).
     * Ele chama o método `alterar_prev()`, que realiza a atualização dos dados
     * da manutenção preventiva no banco de dados.
     *
     * Funcionalidades: - **Ação no Clique do Botão**: - Quando o usuário
     * pressiona `btnAlrPre`, o evento `ActionEvent` é disparado e chama
     * `alterar_prev()`.
     *
     * - **Execução da Alteração**: - `alterar_prev()` verifica os campos
     * modificados e atualiza a manutenção preventiva no banco de dados.
     *
     * Fluxo do Método: 1. Detecta o clique do usuário no botão `btnAlrPre`. 2.
     * Chama `alterar_prev()` para iniciar o processo de atualização dos dados.
     */
    private void btnAlrPreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlrPreActionPerformed

        alterar_prev();
    }//GEN-LAST:event_btnAlrPreActionPerformed

    /**
     * Método `btnApaPreActionPerformed`. Este método é acionado automaticamente
     * quando o usuário clica no botão de apagar preventiva (`btnApaPre`). Ele
     * chama o método `apagar_prev()`, que realiza a remoção da manutenção
     * preventiva do banco de dados.
     *
     * Funcionalidades: - **Ação no Clique do Botão**: - Quando o usuário
     * pressiona `btnApaPre`, o evento `ActionEvent` é disparado e chama
     * `apagar_prev()`.
     *
     * - **Execução da Remoção**: - `apagar_prev()` verifica se a manutenção
     * preventiva existe e executa a exclusão no banco de dados.
     *
     * Fluxo do Método: 1. Detecta o clique do usuário no botão `btnApaPre`. 2.
     * Chama `apagar_prev()` para iniciar o processo de remoção da preventiva.
     */
    private void btnApaPreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApaPreActionPerformed

        apagar_prev();
    }//GEN-LAST:event_btnApaPreActionPerformed

    /**
     * Método `txtIdSetPrevKeyReleased`. Este método é acionado automaticamente
     * quando o usuário digita ou solta uma tecla no campo `txtIdSetPrev`. Ele
     * verifica se o campo está vazio e chama o método `vazio()` para validar os
     * campos obrigatórios.
     *
     * Funcionalidades: - **Detecção de Tecla Pressionada**: - O evento
     * `KeyReleased` captura qualquer tecla solta no campo `txtIdSetPrev`,
     * permitindo validação instantânea.
     *
     * - **Validação de Campo**: - Define `v = txtIdSetPrev.getText() + "%"`,
     * incluindo um caractere `%` para futuras operações de busca. - Se
     * `v.equals("")`, chama `vazio()` para marcar os campos obrigatórios. -
     * Caso contrário, também chama `vazio()`, resultando em lógica redundante.
     *
     * **Sugestão de Melhoria**: - O código está chamando `vazio()`
     * independentemente da condição, o que torna a verificação desnecessária. -
     * Melhor abordagem: ```java if (txtIdSetPrev.getText().isEmpty()) {
     * vazio(); } ```
     *
     * Fluxo do Método: 1. Captura a entrada de teclas no campo `txtIdSetPrev`.
     * 2. Verifica se o campo está vazio. 3. Se estiver vazio, chama `vazio()`
     * para validar campos obrigatórios.
     */
    private void txtIdSetPrevKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIdSetPrevKeyReleased

        String v;
        v = txtIdSetPrev.getText() + "%";
        if (v.equals("")) {
            vazio();
        } else {
            vazio();
        }
    }//GEN-LAST:event_txtIdSetPrevKeyReleased

    /**
     * Método `txtIdSetPrevActionPerformed`. Este método é acionado
     * automaticamente quando o usuário realiza uma ação no campo
     * `txtIdSetPrev`, como pressionar "Enter". Ele verifica o tamanho do texto
     * inserido e executa a validação apropriada com `vazio()`.
     *
     * Funcionalidades: - **Captura de Ação no Campo de Texto**: - Disparado
     * quando o usuário interage com `txtIdSetPrev`, como pressionar "Enter".
     *
     * - **Verificação do Comprimento do Texto**: - Obtém o número de caracteres
     * inseridos com `txtIdSetPrev.getText().length()`. - Se `v < 0`, chama
     * `vazio()` para validar os campos obrigatórios.
     *
     * **Correção Necessária**: - A condição `if (v < 0)` nunca será
     * verdadeira, pois `length()` sempre retorna valores ≥ 0. - Melhor
     * abordagem: ```java if (v == 0) { vazio(); } ```
     *
     * Fluxo do Método: 1. Detecta a ação realizada no campo de entrada. 2.
     * Verifica o número de caracteres inseridos. 3. Se estiver vazio (`v ==
     * 0`), chama `vazio()` para validar os campos obrigatórios.
     */
    private void txtIdSetPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdSetPrevActionPerformed

        v = txtIdSetPrev.getText().length();
        if (v < 0) {
            vazio();
        }
    }//GEN-LAST:event_txtIdSetPrevActionPerformed

    /**
     * Método `rbtSetorActionPerformed`. Este método é acionado automaticamente
     * quando o usuário seleciona o botão de opção (`rbtSetor`), que define o
     * tipo de preventiva como "setor" e executa o método `preventiva()` para
     * carregar os dados necessários.
     *
     * Funcionalidades: - **Definição do Tipo de Preventiva**: - Atribui
     * `"setor"` à variável `preve`, indicando que a ação está relacionada à
     * gestão de setores.
     *
     * - **Chamada do Método `preventiva()`**: - Executa `preventiva()` para
     * limpar a interface, realizar pesquisas e atualizar a tabela com os
     * setores disponíveis.
     *
     * Fluxo do Método: 1. Detecta o clique no botão `rbtSetor`. 2. Define
     * `preve = "setor"` para determinar o contexto da preventiva. 3. Chama
     * `preventiva()` para processar a atualização dos setores.
     */
    private void rbtSetorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtSetorActionPerformed

        preve = "setor";
        preventiva();
    }//GEN-LAST:event_rbtSetorActionPerformed

    /**
     * Método `rbtEquipamentoActionPerformed`. Este método é acionado
     * automaticamente quando o usuário seleciona o botão de opção
     * (`rbtEquipamento`), que define o tipo de preventiva como "equipamento" e
     * executa o método `preventiva()` para carregar os dados necessários.
     *
     * Funcionalidades: - **Definição do Tipo de Preventiva**: - Atribui
     * `"equipamento"` à variável `preve`, indicando que a ação está relacionada
     * à gestão de equipamentos.
     *
     * - **Chamada do Método `preventiva()`**: - Executa `preventiva()` para
     * limpar a interface, realizar pesquisas e atualizar a tabela com os
     * equipamentos disponíveis.
     *
     * Fluxo do Método: 1. Detecta o clique no botão `rbtEquipamento`. 2. Define
     * `preve = "equipamento"` para determinar o contexto da preventiva. 3.
     * Chama `preventiva()` para processar a atualização dos equipamentos.
     */
    private void rbtEquipamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtEquipamentoActionPerformed

        preve = "equipamento";
        preventiva();
    }//GEN-LAST:event_rbtEquipamentoActionPerformed

    /**
     * Método `rbtTecnicoActionPerformed`. Este método é acionado
     * automaticamente quando o usuário seleciona o botão de opção
     * (`rbtTecnico`), que define o tipo de preventiva como "técnico" e executa
     * o método `preventiva()` para carregar os dados necessários.
     *
     * Funcionalidades: - **Definição do Tipo de Preventiva**: - Atribui
     * `"tecnico"` à variável `preve`, indicando que a ação está relacionada à
     * seleção de técnicos.
     *
     * - **Chamada do Método `preventiva()`**: - Executa `preventiva()` para
     * limpar a interface, realizar pesquisas e atualizar a tabela com os
     * técnicos disponíveis.
     *
     * Fluxo do Método: 1. Detecta o clique no botão `rbtTecnico`. 2. Define
     * `preve = "tecnico"` para determinar o contexto da preventiva. 3. Chama
     * `preventiva()` para processar a atualização dos técnicos.
     */
    private void rbtTecnicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtTecnicoActionPerformed

        preve = "tecnico";
        preventiva();
    }//GEN-LAST:event_rbtTecnicoActionPerformed

    /**
     * Método `formInternalFrameOpened`. Este método é acionado automaticamente
     * quando a janela interna (`JInternalFrame`) é aberta. Ele configura
     * diversos aspectos visuais da interface, define valores padrão e
     * inicializa a tabela para exibição correta dos setores.
     *
     * Funcionalidades: - **Configuração Visual**: - Ajusta o fundo dos
     * componentes `lblDisPrev` e `lblButton` para um tom semi-transparente
     * (`new Color(240, 240, 240, 65)`). - Define a política de rolagem vertical
     * do `jScrollPane1` como `VERTICAL_SCROLLBAR_ALWAYS`, garantindo que a
     * barra de rolagem esteja sempre visível.
     *
     * - **Configuração Inicial da Tabela**: - Limpa todas as linhas da tabela
     * `tblEquiSet` com `setRowCount(0)`. - Define o modo de seleção para
     * permitir apenas uma linha por vez (`SINGLE_SELECTION`).
     *
     * - **Estado Inicial dos Botões**: - Desabilita os botões `btnApaPre` e
     * `btnAlrPre`, garantindo que não possam ser acionados até que uma ação
     * apropriada ocorra.
     *
     * - **Definição do Tipo de Preventiva**: - Define `preve = "setor"`,
     * indicando que a janela começa no contexto de busca por setores. - Ativa
     * `rbtSetor` (`setSelected(true)`) e desabilita `rbtEquipamento`
     * (`setEnabled(false)`).
     *
     * - **Foco na Pesquisa**: - Move o cursor automaticamente para o campo de
     * pesquisa `txtPesEqui` (`requestFocus()`).
     *
     * - **Inicialização de Dados**: - Chama `pesquisarSetorIni()` para carregar
     * os setores disponíveis. - Invoca `formTabela()` para configurar a
     * exibição da tabela.
     *
     * - **Centralização do Cabeçalho da Tabela**: - Obtém o cabeçalho da tabela
     * (`getTableHeader()`) e define seu alinhamento como centralizado
     * (`setHorizontalAlignment(SwingConstants.CENTER)`).
     *
     * Fluxo do Método: 1. Ajusta elementos visuais e configurações da
     * interface. 2. Limpa e organiza a tabela para exibição correta. 3. Define
     * estado inicial dos botões e opções de seleção. 4. Inicia a busca de
     * setores e ajusta a tabela conforme necessário.
     */
    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        lblDisPrev.setBackground(new Color(240, 240, 240, 65));
        lblButton.setBackground(new Color(240, 240, 240, 65));
        ((DefaultTableModel) tblEquiSet.getModel()).setRowCount(0);
        btnApaPre.setEnabled(false);
        btnAlrPre.setEnabled(false);
        tblEquiSet.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
        preve = "setor";
        rbtSetor.setSelected(true);
        rbtEquipamento.setEnabled(false);
        rbtTecnico.setEnabled(false);
        txtPesEqui.requestFocus();
        pesquisarSetorIni();
        formTabela();

        JTableHeader header;

        header = tblEquiSet.getTableHeader();
        DefaultTableCellRenderer centralizado1 = (DefaultTableCellRenderer) header.getDefaultRenderer();
        centralizado1.setHorizontalAlignment(SwingConstants.CENTER);
        cboTipPrev.setEnabled(false);
        cboTipPrev.addItem("-Selecione-");
    }//GEN-LAST:event_formInternalFrameOpened

    private void txtPesEquiKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesEquiKeyTyped
        String caracter = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789 ";
        if (!caracter.contains(evt.getKeyChar() + "")) {
            evt.consume();
//            Toolkit.getDefaultToolkit().beep();
        }
    }//GEN-LAST:event_txtPesEquiKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAlrPre;
    private javax.swing.JButton btnApaPre;
    private javax.swing.JButton btnCadPre;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cboTipPrev;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private com.prjmanutencao.telas.LabelPersonalizada lblButton;
    private com.prjmanutencao.telas.LabelPersonalizada lblDisPrev;
    private javax.swing.JLabel lblPesqGeral;
    private javax.swing.JRadioButton rbtEquipamento;
    private javax.swing.JRadioButton rbtSetor;
    private javax.swing.JRadioButton rbtTecnico;
    private javax.swing.JTable tblEquiSet;
    private javax.swing.JTextField txtCodEquiSet;
    private javax.swing.JTextField txtDatPrev;
    private javax.swing.JTextField txtIdEquiSet;
    private javax.swing.JTextField txtIdPre;
    private javax.swing.JTextField txtIdSetPrev;
    private javax.swing.JTextField txtIdTecPrev;
    private javax.swing.JTextField txtNonEquiSet;
    private javax.swing.JTextField txtPesEqui;
    private javax.swing.JTextField txtSetPrev;
    private javax.swing.JTextField txtTecPrev;
    private javax.swing.JTextField txtTipEqui;
    private javax.swing.JTextField txtTipTec;
    // End of variables declaration//GEN-END:variables
}
