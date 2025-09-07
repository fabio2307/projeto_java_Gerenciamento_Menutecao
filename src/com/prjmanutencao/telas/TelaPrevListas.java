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

import static com.prjmanutencao.telas.TelaDisPreventiva.idprev;
import static com.prjmanutencao.telas.TelaDisPreventiva.tipo;
import static com.prjmanutencao.telas.TelaLogin.tip;

import static com.prjmanutencao.telas.TelaEletricaMen.id_prev;
import static com.prjmanutencao.telas.TelaHidraulicaMen1.id_prev1;
import static com.prjmanutencao.telas.TelaCivilMen1.id_prev2;
import static com.prjmanutencao.telas.TelaCivilSem.id_prev9;
import static com.prjmanutencao.telas.TelaCivilTrimestral.id_prev6;
import static com.prjmanutencao.telas.TelaEletricaSem.id_prev8;
import static com.prjmanutencao.telas.TelaEletricaTrimestral.id_prev4;
import static com.prjmanutencao.telas.TelaHidraulicaSem.id_prev10;
import static com.prjmanutencao.telas.TelaHidraulicaTrimestral.id_prev5;
import static com.prjmanutencao.telas.TelaRefrigeracaoMen1.id_prev3;

import static com.prjmanutencao.telas.TelaLogin.id;
import static com.prjmanutencao.telas.TelaRefrigeracaoSem.id_prev11;
import static com.prjmanutencao.telas.TelaRefrigeracaoTrimestral.id_prev7;
import java.beans.PropertyVetoException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

/**
 * Mostra as listas de preventivas que devem ser iniciadas, dando a opção de
 * retornar a tela de distribuição de preventivas e a inicialização da mesma.
 *
 * @author Fábio S. Oliveira
 * @version 1.1
 */
public class TelaPrevListas extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    String usu = null;
    public static String nome = null;
    String si = null;
    String set = null;
    String situacao = null;
    String situacao1 = null;
    String t = null;
    String tempo = null;

    /**
     * Construtor `TelaPrevListas`. Este método inicializa a interface gráfica
     * da tela de listas de preventivas e estabelece a conexão com o banco de
     * dados.
     *
     * Funcionalidades: - **Inicialização da Interface**: - Chama
     * `initComponents()`, que configura os elementos da interface gráfica
     * automaticamente.
     *
     * - **Estabelecimento da Conexão com o Banco de Dados**: - Invoca
     * `ModuloConexao.conector()`, garantindo que a aplicação tenha acesso ao
     * banco de dados. - A variável `conexao` armazena a conexão ativa,
     * permitindo operações de consulta e inserção posteriormente.
     *
     * Fluxo do Método: 1. Inicializa os componentes da interface gráfica. 2.
     * Estabelece a conexão com o banco de dados usando `ModuloConexao`.
     *
     * @author Fábio S. Oliveira
     * @version 1.1
     */
    public TelaPrevListas() {
        initComponents();
        conexao = ModuloConexao.conector();
    }

    /**
     * Método `tabelaFormat`. Este método ajusta as configurações visuais da
     * tabela `tblPreEle`, garantindo que cada coluna tenha um tamanho
     * apropriado e que o cabeçalho e as células sejam centralizados para melhor
     * legibilidade.
     *
     * Funcionalidades: - **Definição de Largura das Colunas**: - Define
     * larguras específicas para cada coluna, garantindo uma distribuição
     * adequada dos dados. - Evita que colunas fiquem desorganizadas ou com
     * espaçamento inadequado.
     *
     * - **Centralização do Cabeçalho**: - Obtém o cabeçalho da tabela
     * (`getTableHeader()`). - Ajusta seu alinhamento para o centro
     * (`setHorizontalAlignment(SwingConstants.CENTER)`), melhorando a leitura
     * dos títulos das colunas.
     *
     * - **Configuração da Redimensionamento da Tabela**: - Desativa o ajuste
     * automático (`setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF)`),
     * permitindo controle manual sobre a largura das colunas.
     *
     * - **Centralização do Conteúdo das Células**: - Usa
     * `DefaultTableCellRenderer` para alinhar todos os dados ao centro. -
     * Aplica o alinhamento a todas as colunas da tabela usando um loop.
     *
     * - **Tratamento de Erros**: - Captura falhas e exibe uma mensagem ao
     * usuário caso a formatação da tabela não possa ser aplicada.
     *
     * Fluxo do Método: 1. Define a largura de cada coluna. 2. Obtém e
     * centraliza o cabeçalho da tabela. 3. Configura o modo de
     * redimensionamento da tabela. 4. Aplica centralização às células de todas
     * as colunas. 5. Captura e trata possíveis erros.
     */
    private void tabelaFormat() {

        int width = 90, width1 = 90, width2 = 200, width3 = 180, width4 = 220, width5 = 200, width6 = 90, width7 = 100;
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
            tblPreEle.getColumnModel().getColumn(5).setPreferredWidth(width5);
            tblPreEle.getColumnModel().getColumn(6).setPreferredWidth(width6);
            tblPreEle.getColumnModel().getColumn(7).setPreferredWidth(width7);

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
     * Método `tabelaFormat1`. Este método ajusta as configurações visuais da
     * tabela `tblPreHid`, garantindo que cada coluna tenha um tamanho
     * apropriado e que o cabeçalho e as células sejam centralizados para melhor
     * legibilidade.
     *
     * Funcionalidades: - **Definição de Largura das Colunas**: - Define
     * larguras específicas para cada coluna, garantindo uma distribuição
     * adequada dos dados. - Evita que colunas fiquem desorganizadas ou com
     * espaçamento inadequado.
     *
     * - **Centralização do Cabeçalho**: - Obtém o cabeçalho da tabela
     * (`getTableHeader()`). - Ajusta seu alinhamento para o centro
     * (`setHorizontalAlignment(SwingConstants.CENTER)`), melhorando a leitura
     * dos títulos das colunas.
     *
     * - **Configuração da Redimensionamento da Tabela**: - Desativa o ajuste
     * automático (`setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF)`),
     * permitindo controle manual sobre a largura das colunas.
     *
     * - **Centralização do Conteúdo das Células**: - Usa
     * `DefaultTableCellRenderer` para alinhar todos os dados ao centro. -
     * Aplica o alinhamento a todas as colunas da tabela usando um loop.
     *
     * - **Tratamento de Erros**: - Captura falhas e exibe uma mensagem ao
     * usuário caso a formatação da tabela não possa ser aplicada.
     *
     * Fluxo do Método: 1. Define a largura de cada coluna. 2. Obtém e
     * centraliza o cabeçalho da tabela. 3. Configura o modo de
     * redimensionamento da tabela. 4. Aplica centralização às células de todas
     * as colunas. 5. Captura e trata possíveis erros.
     */
    private void tabelaFormat1() {

        int width = 90, width1 = 90, width2 = 200, width3 = 180, width4 = 220, width5 = 200, width6 = 90, width7 = 100;
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
            tblPreHid.getColumnModel().getColumn(6).setPreferredWidth(width6);
            tblPreHid.getColumnModel().getColumn(7).setPreferredWidth(width7);

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
     * Método `tabelaFormat2`. Este método ajusta as configurações visuais da
     * tabela `tblPreCivil`, garantindo que cada coluna tenha um tamanho
     * apropriado e que o cabeçalho e as células sejam centralizados para melhor
     * legibilidade.
     *
     * Funcionalidades: - **Definição de Largura das Colunas**: - Define
     * larguras específicas para cada coluna, garantindo uma distribuição
     * adequada dos dados. - Evita que colunas fiquem desorganizadas ou com
     * espaçamento inadequado.
     *
     * - **Centralização do Cabeçalho**: - Obtém o cabeçalho da tabela
     * (`getTableHeader()`). - Ajusta seu alinhamento para o centro
     * (`setHorizontalAlignment(SwingConstants.CENTER)`), melhorando a leitura
     * dos títulos das colunas.
     *
     * - **Configuração da Redimensionamento da Tabela**: - Desativa o ajuste
     * automático (`setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF)`),
     * permitindo controle manual sobre a largura das colunas.
     *
     * - **Centralização do Conteúdo das Células**: - Usa
     * `DefaultTableCellRenderer` para alinhar todos os dados ao centro. -
     * Aplica o alinhamento a todas as colunas da tabela usando um loop.
     *
     * - **Tratamento de Erros**: - Captura falhas e exibe uma mensagem ao
     * usuário caso a formatação da tabela não possa ser aplicada.
     *
     * Fluxo do Método: 1. Define a largura de cada coluna. 2. Obtém e
     * centraliza o cabeçalho da tabela. 3. Configura o modo de
     * redimensionamento da tabela. 4. Aplica centralização às células de todas
     * as colunas. 5. Captura e trata possíveis erros.
     */
    private void tabelaFormat2() {

        int width = 90, width1 = 90, width2 = 200, width3 = 180, width4 = 220, width5 = 200, width6 = 90, width7 = 100;
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
            tblPreCivil.getColumnModel().getColumn(6).setPreferredWidth(width6);
            tblPreCivil.getColumnModel().getColumn(7).setPreferredWidth(width7);

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
     * Método `tabelaFormat3`. Este método ajusta as configurações visuais da
     * tabela `tblPreCivil`, garantindo que cada coluna tenha um tamanho
     * apropriado e que o cabeçalho e as células sejam centralizados para melhor
     * legibilidade.
     *
     * Funcionalidades: - **Definição de Largura das Colunas**: - Define
     * larguras específicas para cada coluna, garantindo uma distribuição
     * adequada dos dados. - Evita que colunas fiquem desorganizadas ou com
     * espaçamento inadequado.
     *
     * - **Centralização do Cabeçalho**: - Obtém o cabeçalho da tabela
     * (`getTableHeader()`). - Ajusta seu alinhamento para o centro
     * (`setHorizontalAlignment(SwingConstants.CENTER)`), melhorando a leitura
     * dos títulos das colunas.
     *
     * - **Configuração da Redimensionamento da Tabela**: - Desativa o ajuste
     * automático (`setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF)`),
     * permitindo controle manual sobre a largura das colunas.
     *
     * - **Centralização do Conteúdo das Células**: - Usa
     * `DefaultTableCellRenderer` para alinhar todos os dados ao centro. -
     * Aplica o alinhamento a todas as colunas da tabela usando um loop.
     *
     * - **Tratamento de Erros**: - Captura falhas e exibe uma mensagem ao
     * usuário caso a formatação da tabela não possa ser aplicada.
     *
     * Fluxo do Método: 1. Define a largura de cada coluna. 2. Obtém e
     * centraliza o cabeçalho da tabela. 3. Configura o modo de
     * redimensionamento da tabela. 4. Aplica centralização às células de todas
     * as colunas. 5. Captura e trata possíveis erros.
     */
    private void tabelaFormat3() {

        int width = 90, width1 = 90, width2 = 200, width3 = 180, width4 = 220, width5 = 200, width6 = 90, width7 = 100;
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
            tblPreRef.getColumnModel().getColumn(6).setPreferredWidth(width6);
            tblPreRef.getColumnModel().getColumn(7).setPreferredWidth(width7);

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
     * Método `inicializacao`. Este método configura a interface visual de
     * acordo com o estado da variável `t`, definindo uma imagem apropriada para
     * representar a lista de preventivas ou preventivas iniciadas. Caso ocorra
     * um erro ao carregar os ícones, uma mensagem de erro é exibida ao usuário.
     *
     * Funcionalidades: - **Configuração de Ícones**: - Se `t.equals("Listas")`,
     * define `lblPreEnc` com a imagem `"lista de preventivas.png"`. - Se
     * `t.equals("Iniciadas")`, define `lblPreEnc` com a imagem `"prev
     * abertas.png"`. - Os ícones são obtidos via `getResource()` dentro da
     * estrutura de diretórios `com/prjmanutencao/telas/imagens_fundo/`.
     *
     * - **Tratamento de Erros**: - Captura exceções caso ocorra falha ao
     * carregar as imagens e exibe um `JOptionPane` ao usuário.
     *
     * - **Possível Expansão**: - O código comentado (`selec_encerrad()` e
     * `selec_aberta()`) pode ser ativado para incluir lógica adicional, como a
     * seleção automática de dados quando a tela é inicializada.
     *
     * Fluxo do Método: 1. Verifica o estado da variável `t`. 2. Define o ícone
     * correspondente à opção selecionada. 3. Captura e exibe erros caso ocorram
     * problemas ao carregar a imagem.
     */
    private void inicializacao() {

        try {

            if (t.equals("Listas")) {
                ImageIcon ico = new ImageIcon(getClass().getResource("/com/prjmanutencao/telas/imagens_fundo/lista de preventivas.png"));
                lblPreEnc.setIcon(ico);
            }
            if (t.equals("Iniciadas")) {
                ImageIcon ico = new ImageIcon(getClass().getResource("/com/prjmanutencao/telas/imagens_fundo/prev abertas.png"));
                lblPreEnc.setIcon(ico);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método `selec_encerrad`. Este método realiza a filtragem de preventivas
     * encerradas com base no período (`Mensal`, `Trimestral`, `Semestral`) e
     * organiza os dados na interface. Ele primeiro limpa os registros
     * existentes e, em seguida, executa funções específicas para cada período
     * de manutenção.
     *
     * Funcionalidades: - **Filtragem de Usuário**: - Chama `filtroUsu()`,
     * garantindo que a seleção das preventivas esteja de acordo com a permissão
     * do usuário.
     *
     * - **Limpeza de Dados**: - Executa `limpar()`, garantindo que os registros
     * anteriores sejam removidos antes da nova busca.
     *
     * - **Execução Condicional por Período**: - Se `si.equals("Mensal")`: -
     * Chama funções relacionadas à manutenção elétrica, hidráulica, civil e de
     * refrigeração do tipo mensal. - Aplica `tabelaFormat()`,
     * `tabelaFormat1()`, `tabelaFormat2()` e `tabelaFormat3()` para formatar
     * tabelas correspondentes. - Obtém as quantidades de registros mensais com
     * funções auxiliares (`ele_men_quantida()`, etc.). - Se
     * `si.equals("Trimestral")`: - Executa a lógica similar para preventivas
     * trimestrais. - Se `si.equals("Semestral")`: - Executa a lógica para
     * preventivas semestrais.
     *
     * - **Tratamento de Exceções**: - Captura possíveis erros ao processar os
     * dados e exibe uma mensagem ao usuário.
     *
     * Fluxo do Método: 1. Filtra preventivas de acordo com a permissão do
     * usuário. 2. Limpa registros antigos da interface. 3. Determina qual
     * período de manutenção será processado (`Mensal`, `Trimestral`,
     * `Semestral`). 4. Chama métodos para buscar e organizar os dados. 5.
     * Aplica formatação às tabelas e obtém quantidades de registros. 6. Trata
     * erros, garantindo que falhas não impactem a execução.
     */
    private void selec_encerrad() {
        filtroUsu();
        limpar();
        try {

            if (si.equals("Mensal")) {
                ele_men();
                tabelaFormat();
                ele_men_quantida();
                hid_men();
                tabelaFormat1();
                hid_men_quantida();
                civ_men();
                tabelaFormat2();
                civ_men_quantida();
                ref_men();
                tabelaFormat3();
                ref_men_quantida();
            }

            if (si.equals("Trimestral")) {
                ele_tri();
                tabelaFormat();
                ele_tri_quantida();
                hid_tri();
                tabelaFormat1();
                hid_tri_quantida();
                civ_tri();
                tabelaFormat2();
                civ_tri_quantida();
                ref_tri();
                tabelaFormat3();
                ref_tri_quantida();

            }
            if (si.equals("Semestral")) {
                ele_sem();
                tabelaFormat();
                ele_sem_quantida();
                hid_sem();
                tabelaFormat1();
                hid_sem_quantida();
                civ_sem();
                tabelaFormat2();
                civ_sem_quantida();
                ref_sem();
                tabelaFormat3();
                ref_sem_quantida();

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método `selec_aberta`. Este método realiza a filtragem e carregamento de
     * preventivas abertas com base no período (`Mensal`, `Trimestral`,
     * `Semestral`), organizando os dados na interface. Ele primeiro limpa os
     * registros existentes e, em seguida, executa funções específicas para cada
     * período de manutenção.
     *
     * Funcionalidades: - **Filtragem de Usuário**: - Chama `filtroUsu()`,
     * garantindo que a seleção das preventivas esteja de acordo com a permissão
     * do usuário.
     *
     * - **Limpeza de Dados**: - Executa `limpar()`, garantindo que os registros
     * anteriores sejam removidos antes da nova busca.
     *
     * - **Execução Condicional por Período**: - Se `si.equals("Mensal")`: -
     * Chama funções relacionadas à manutenção elétrica, hidráulica, civil e de
     * refrigeração do tipo mensal. - Aplica `tabelaFormat()`,
     * `tabelaFormat1()`, `tabelaFormat2()` e `tabelaFormat3()` para formatar
     * tabelas correspondentes. - Obtém as quantidades de registros mensais com
     * funções auxiliares (`ele_men_quantida1()`, etc.). - Se
     * `si.equals("Trimestral")`: - Executa a lógica similar para preventivas
     * trimestrais. - Se `si.equals("Semestral")`: - Executa a lógica para
     * preventivas semestrais.
     *
     * - **Tratamento de Exceções**: - Captura possíveis erros ao processar os
     * dados e exibe uma mensagem ao usuário.
     *
     * Fluxo do Método: 1. Filtra preventivas de acordo com a permissão do
     * usuário. 2. Limpa registros antigos da interface. 3. Determina qual
     * período de manutenção será processado (`Mensal`, `Trimestral`,
     * `Semestral`). 4. Chama métodos para buscar e organizar os dados. 5.
     * Aplica formatação às tabelas e obtém quantidades de registros. 6. Trata
     * erros, garantindo que falhas não impactem a execução.
     */
    private void selec_aberta() {
        filtroUsu();
        limpar();
        try {

            if (si.equals("Mensal")) {
                ele_men_ini();
                tabelaFormat();
                ele_men_quantida1();
                hid_men_ini();
                tabelaFormat1();
                hid_men_quantida1();
                civ_men_ini();
                tabelaFormat2();
                civ_men_quantida1();
                ref_men_ini();
                tabelaFormat3();
                ref_men_quantida1();
            }

            if (si.equals("Trimestral")) {
                ele_tri_ini();
                tabelaFormat();
                ele_tri_quantida1();
                hid_tri_ini();
                tabelaFormat1();
                hid_tri_quantida1();
                civ_tri_ini();
                tabelaFormat2();
                civ_tri_quantida1();
                ref_tri_ini();
                tabelaFormat3();
                ref_tri_quantida1();

            }
            if (si.equals("Semestral")) {
                ele_sem_ini();
                tabelaFormat();
                ele_sem_quantida1();
                hid_sem_ini();
                tabelaFormat1();
                hid_sem_quantida1();
                civ_sem_ini();
                tabelaFormat2();
                civ_sem_quantida1();
                ref_sem_ini();
                tabelaFormat3();
                ref_sem_quantida1();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método `filtroUsu`. Este método realiza uma consulta ao banco de dados
     * para verificar o tipo de usuário com base no `iduser`. Ele obtém a
     * informação e, caso encontrada, aciona o método `fitro()` para aplicar
     * possíveis filtros de acesso.
     *
     * Funcionalidades: - **Consulta SQL**: - Usa `SELECT tipo_usuario FROM
     * usuarios WHERE iduser = ?` para recuperar o tipo de usuário
     * correspondente ao `iduser`.
     *
     * - **Execução da Query**: - Prepara e executa a query usando
     * `PreparedStatement`, garantindo segurança contra injeção SQL.
     *
     * - **Verificação de Resultado**: - Se a consulta retornar um resultado
     * (`rs.next()`), a variável `usu` recebe o tipo de usuário e `fitro()` é
     * chamado.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe uma
     * mensagem ao usuário em caso de falha.
     *
     * Fluxo do Método: 1. Prepara a query SQL para obter o tipo de usuário. 2.
     * Executa a consulta e verifica se há resultado. 3. Define `usu` com o tipo
     * de usuário encontrado. 4. Chama `fitro()` para aplicar filtros de acesso
     * conforme necessário. 5. Captura e trata erros para evitar falhas
     * silenciosas.
     */
    private void filtroUsu() {

        String sql = "select tipo_usuario from usuarios where iduser = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            rs = pst.executeQuery();
            if (rs.next()) {
                usu = (rs.getString(1));
                fitro();
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `fitro`. Este método aplica um filtro visual às tabelas com base
     * no tipo de usuário (`usu`), alterando a cor do texto das tabelas que não
     * correspondem à área de especialização do técnico. Isso ajuda a destacar
     * quais preventivas não pertencem à área de atuação do usuário.
     *
     * Funcionalidades: - **Definição de Cores**: - Modifica a cor do texto das
     * tabelas (`setForeground(Color.red)`) para diferenciar os setores que o
     * técnico não gerencia.
     *
     * - **Filtro por Especialidade**: - Cada caso do `switch(usu)` verifica o
     * tipo de técnico (`Elétrica`, `Hidráulica`, `Civil`, `Refrigeração`,
     * `Manutenção`). - As tabelas que não pertencem à especialidade do usuário
     * são marcadas em vermelho, indicando não compatibilidade.
     *
     * - **Tratamento de Erros**: - Captura exceções (`Exception`) e exibe uma
     * mensagem ao usuário caso ocorra falha na execução.
     *
     * Fluxo do Método: 1. Verifica o tipo de usuário (`usu`). 2. Aplica a cor
     * vermelha às tabelas que não correspondem à especialidade do usuário. 3.
     * Caso ocorra erro, exibe um alerta informando o problema.
     */
    private void fitro() {
        try {
            switch (usu) {

                case "Técnico - Elétrica":

                    tblPreRef.setForeground(java.awt.Color.red);
                    tblPreHid.setForeground(java.awt.Color.red);
                    tblPreCivil.setForeground(java.awt.Color.red);
                    break;
                case "Técnico - Hidráulica":

                    tblPreEle.setForeground(java.awt.Color.red);
                    tblPreCivil.setForeground(java.awt.Color.red);
                    tblPreRef.setForeground(java.awt.Color.red);
                    break;
                case "Técnico - Civil":

                    tblPreRef.setForeground(java.awt.Color.red);
                    tblPreHid.setForeground(java.awt.Color.red);
                    tblPreEle.setForeground(java.awt.Color.red);
                    break;
                case "Técnico - Refrigeração":

                    tblPreEle.setForeground(java.awt.Color.red);
                    tblPreHid.setForeground(java.awt.Color.red);
                    tblPreCivil.setForeground(java.awt.Color.red);
                    break;
                case "Técnico - Manutenção":
                    tblPreEle.setForeground(java.awt.Color.red);
                    tblPreRef.setForeground(java.awt.Color.red);
                    tblPreHid.setForeground(java.awt.Color.red);
                    tblPreCivil.setForeground(java.awt.Color.red);
                    break;
                default:
                    break;

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `ele_men`. Este método consulta o banco de dados para obter
     * preventivas elétricas mensais associadas a um usuário específico. Ele
     * recupera informações detalhadas sobre as preventivas e exibe os
     * resultados na tabela `tblPreEle`, formatando a exibição para melhor
     * legibilidade.
     *
     * Funcionalidades: - **Definição da Situação Inicial**: - A variável
     * `situacao1` é inicializada como `" "`, representando uma possível
     * situação de preventiva.
     *
     * - **Consulta SQL**: - Busca preventivas mensais na tabela
     * `form_eletrica_mensal` com base no `iduser`. - Retorna registros onde
     * `situacao_prev` está `null` ou igual a `" "`. - Formata os nomes das
     * colunas para exibição: - `"N°"` → Identificador da preventiva. - `"R.E"`
     * → ID do usuário responsável. - `"Técnico"` → Nome do técnico responsável.
     * - `"Preventiva"` → Tipo da manutenção preventiva. - `"Setor"` → Setor
     * onde a preventiva foi realizada. - `"Equipamento"` → Nome do equipamento.
     * - `"Código"` → Código de identificação do equipamento. - `"Distribuição"`
     * → Tempo de distribuição da preventiva.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Atribui o resultado à tabela `tblPreEle` via
     * `DbUtils.resultSetToTableModel(rs)`.
     *
     * - **Formatação da Tabela**: - Chama `tabelaFormat()` para ajustar
     * visualmente a tabela.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * Fluxo do Método: 1. Define a situação inicial das preventivas. 2. Prepara
     * e executa a consulta SQL. 3. Obtém os resultados e os exibe na tabela
     * `tblPreEle`. 4. Aplica formatação à tabela para melhor visualização. 5.
     * Trata erros, garantindo estabilidade da execução.
     */
    private void ele_men() {
        situacao1 = " ";

        String sql = "select id_form_ele_mensal as 'N°', iduser as 'R.E', nome_prev as Técnico, tipo_prev as 'Preventiva', setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', tempo_dis as Distribuição from  form_eletrica_mensal where iduser = ? and situacao_prev is null or iduser = ? and situacao_prev = ? ";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, id);
            pst.setString(3, situacao1);
            rs = pst.executeQuery();
            tblPreEle.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `ele_men_pes`. Este método permite a pesquisa de preventivas
     * elétricas mensais no banco de dados com base no usuário logado (`iduser`)
     * e no critério de busca inserido pelo usuário (`txtPesqEle`). Ele filtra
     * as preventivas por número de formulário (`id_form_ele_mensal`) ou setor
     * (`setor_prev`) e exibe os resultados na tabela `tblPreEle`.
     *
     * Funcionalidades: - **Consulta SQL com Filtro de Pesquisa**: - Busca
     * registros na tabela `form_eletrica_mensal` onde `situacao_prev` é `null`
     * e `iduser` corresponde ao usuário logado. - Permite busca por
     * `id_form_ele_mensal` ou `setor_prev` usando a cláusula `LIKE` com `%`,
     * garantindo pesquisa parcial. - Retorna informações organizadas com alias
     * para melhor exibição na tabela: - `"N°"` → Número do formulário da
     * preventiva. - `"R.E"` → Registro do usuário responsável. - `"Técnico"` →
     * Nome do técnico responsável. - `"Preventiva"` → Tipo da manutenção
     * preventiva. - `"Setor"` → Setor onde foi realizada a preventiva. -
     * `"Equipamento"` → Nome do equipamento. - `"Código"` → Código de
     * identificação do equipamento. - `"Distribuição"` → Tempo de distribuição
     * da preventiva.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Aplica filtros de pesquisa a partir da entrada do usuário
     * (`txtPesqEle.getText()`). - Atribui os resultados à tabela `tblPreEle`
     * via `DbUtils.resultSetToTableModel(rs)`.
     *
     * - **Formatação da Tabela**: - Chama `tabelaFormat()` para organizar
     * visualmente os dados exibidos na tabela.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * **Possível Melhoria**: - A cláusula `OR` na consulta pode causar
     * resultados inesperados se não for bem estruturada. Para evitar erros, uma
     * abordagem mais segura seria: ```sql SELECT id_form_ele_mensal AS 'N°',
     * iduser AS 'R.E', nome_prev AS Técnico, tipo_prev AS 'Preventiva',
     * setor_prev AS Setor, nome_equi_set AS Equipamento, cod_equi_set AS
     * 'Código', tempo_dis AS Distribuição FROM form_eletrica_mensal WHERE
     * iduser = ? AND situacao_prev IS NULL AND (id_form_ele_mensal LIKE ? OR
     * setor_prev LIKE ?) ```
     *
     * Fluxo do Método: 1. Obtém o critério de pesquisa inserido pelo usuário.
     * 2. Prepara e executa a consulta SQL segura. 3. Exibe os resultados
     * filtrados na tabela `tblPreEle`. 4. Aplica formatação à tabela para
     * melhor visualização. 5. Trata erros para evitar falhas na execução.
     */
    private void ele_men_pes() {

        String sql = "select id_form_ele_mensal as 'N°', iduser as 'R.E', nome_prev as Técnico, tipo_prev as 'Preventiva', setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', tempo_dis as Distribuição from  form_eletrica_mensal where iduser = ? and situacao_prev is null and id_form_ele_mensal like ? or iduser = ? and situacao_prev is null and setor_prev like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, txtPesqEle.getText() + "%");
            pst.setString(3, id);
            pst.setString(4, txtPesqEle.getText() + "%");
            rs = pst.executeQuery();
            tblPreEle.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `ele_men_quantida`. Este método consulta o banco de dados para
     * contar o número de preventivas elétricas mensais associadas ao usuário
     * logado (`iduser`) que ainda estão pendentes (`situacao_prev IS NULL`). O
     * resultado é exibido no campo `txtQuaEle`, permitindo um rápido
     * acompanhamento da quantidade de tarefas abertas.
     *
     * Funcionalidades: - **Consulta SQL com Contagem de Registros**: - Usa
     * `COUNT(*)` para retornar o número total de preventivas elétricas
     * pendentes. - Filtra por `iduser`, garantindo que apenas preventivas do
     * usuário logado sejam contabilizadas.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Obtém o resultado e atribui ao campo `txtQuaEle`.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * Fluxo do Método: 1. Prepara e executa a consulta SQL segura. 2. Obtém a
     * contagem de preventivas elétricas pendentes. 3. Exibe o resultado no
     * campo `txtQuaEle`. 4. Trata erros para evitar problemas na execução.
     */
    private void ele_men_quantida() {

        String sql = "select count(*) from form_eletrica_mensal where iduser = ?  and situacao_prev is null ";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            rs = pst.executeQuery();
            if (rs.next()) {
                txtQuaEle.setText(rs.getString(1));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `ele_tri`. Este método consulta o banco de dados para obter
     * preventivas elétricas trimestrais associadas a um usuário específico. Ele
     * recupera informações detalhadas sobre as preventivas e exibe os
     * resultados na tabela `tblPreEle`, formatando a exibição para melhor
     * legibilidade.
     *
     * Funcionalidades: - **Definição da Situação Inicial**: - A variável
     * `situacao1` é inicializada como `" "`, representando uma possível
     * situação de preventiva.
     *
     * - **Consulta SQL**: - Busca preventivas trimestrais na tabela
     * `form_eletrica_trimestral` com base no `iduser`. - Retorna registros onde
     * `situacao_prev` está `null` ou igual a `" "`. - Formata os nomes das
     * colunas para exibição: - `"N°"` → Identificador da preventiva. - `"R.E"`
     * → ID do usuário responsável. - `"Técnico"` → Nome do técnico responsável.
     * - `"Preventiva"` → Tipo da manutenção preventiva. - `"Setor"` → Setor
     * onde a preventiva foi realizada. - `"Equipamento"` → Nome do equipamento.
     * - `"Código"` → Código de identificação do equipamento. - `"Distribuição"`
     * → Tempo de distribuição da preventiva.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Atribui o resultado à tabela `tblPreEle` via
     * `DbUtils.resultSetToTableModel(rs)`.
     *
     * - **Formatação da Tabela**: - Chama `tabelaFormat()` para ajustar
     * visualmente a tabela.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * **Possível Melhoria**: - A cláusula `OR` na consulta pode causar
     * resultados inesperados se não for bem estruturada. Para evitar erros, uma
     * abordagem mais segura seria: ```sql SELECT id_form_ele_trimestral AS
     * 'N°', iduser AS 'R.E', nome_prev AS Técnico, tipo_prev AS 'Preventiva',
     * setor_prev AS Setor, nome_equi_set AS Equipamento, cod_equi_set AS
     * 'Código', tempo_dis AS Distribuição FROM form_eletrica_trimestral WHERE
     * iduser = ? AND (situacao_prev IS NULL OR situacao_prev = ?) ```
     *
     * Fluxo do Método: 1. Define a situação inicial das preventivas. 2. Prepara
     * e executa a consulta SQL. 3. Obtém os resultados e os exibe na tabela
     * `tblPreEle`. 4. Aplica formatação à tabela para melhor visualização. 5.
     * Trata erros para evitar falhas na execução.
     */
    private void ele_tri() {
        situacao1 = " ";

        String sql = "select id_form_ele_trimestral as 'N°', iduser as 'R.E', nome_prev as Técnico, tipo_prev as 'Preventiva', setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', tempo_dis as Distribuição from  form_eletrica_trimestral where iduser = ? and situacao_prev is null or iduser = ? and situacao_prev = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, id);
            pst.setString(3, situacao1);
            rs = pst.executeQuery();
            tblPreEle.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `ele_tri_pes`. Este método permite a pesquisa de preventivas
     * elétricas trimestrais no banco de dados com base no usuário logado
     * (`iduser`) e no critério de busca inserido pelo usuário (`txtPesqEle`).
     * Ele filtra as preventivas por número de formulário
     * (`id_form_ele_trimestral`) ou setor (`setor_prev`) e exibe os resultados
     * na tabela `tblPreEle`.
     *
     * Funcionalidades: - **Consulta SQL com Filtro de Pesquisa**: - Busca
     * registros na tabela `form_eletrica_trimestral` onde `situacao_prev` é
     * `null` e `iduser` corresponde ao usuário logado. - Permite busca por
     * `id_form_ele_trimestral` ou `setor_prev` usando a cláusula `LIKE` com
     * `%`, garantindo pesquisa parcial. - Retorna informações organizadas com
     * alias para melhor exibição na tabela: - `"N°"` → Número do formulário da
     * preventiva. - `"R.E"` → Registro do usuário responsável. - `"Técnico"` →
     * Nome do técnico responsável. - `"Preventiva"` → Tipo da manutenção
     * preventiva. - `"Setor"` → Setor onde foi realizada a preventiva. -
     * `"Equipamento"` → Nome do equipamento. - `"Código"` → Código de
     * identificação do equipamento. - `"Distribuição"` → Tempo de distribuição
     * da preventiva.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Aplica filtros de pesquisa a partir da entrada do usuário
     * (`txtPesqEle.getText()`). - Atribui os resultados à tabela `tblPreEle`
     * via `DbUtils.resultSetToTableModel(rs)`.
     *
     * - **Formatação da Tabela**: - Chama `tabelaFormat()` para organizar
     * visualmente os dados exibidos na tabela.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * **Correção Necessária**: - A consulta contém um erro na cláusula
     * `WHERE`: está usando `id_form_ele_mensal` quando deveria ser
     * `id_form_ele_trimestral`. - Além disso, a estrutura com `OR` pode gerar
     * resultados inesperados. Uma abordagem mais correta seria: ```sql SELECT
     * id_form_ele_trimestral AS 'N°', iduser AS 'R.E', nome_prev AS Técnico,
     * tipo_prev AS 'Preventiva', setor_prev AS Setor, nome_equi_set AS
     * Equipamento, cod_equi_set AS 'Código', tempo_dis AS Distribuição FROM
     * form_eletrica_trimestral WHERE iduser = ? AND situacao_prev IS NULL AND
     * (id_form_ele_trimestral LIKE ? OR setor_prev LIKE ?) ```
     *
     * Fluxo do Método: 1. Obtém o critério de pesquisa inserido pelo usuário.
     * 2. Prepara e executa a consulta SQL segura. 3. Exibe os resultados
     * filtrados na tabela `tblPreEle`. 4. Aplica formatação à tabela para
     * melhor visualização. 5. Trata erros para evitar problemas na execução.
     */
    private void ele_tri_pes() {

        String sql = "select id_form_ele_trimestral as 'N°', iduser as 'R.E', nome_prev as Técnico, tipo_prev as 'Preventiva', setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', tempo_dis as Distribuição from  form_eletrica_trimestral where iduser = ? and situacao_prev is null and id_form_ele_mensal like ? or iduser = ? and situacao_prev is null and setor_prev like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, txtPesqEle.getText() + "%");
            pst.setString(3, id);
            pst.setString(4, txtPesqEle.getText() + "%");
            rs = pst.executeQuery();
            tblPreEle.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `ele_tri_quantida`. Este método consulta o banco de dados para
     * contar o número de preventivas elétricas trimestrais associadas ao
     * usuário logado (`iduser`) que ainda estão pendentes (`situacao_prev IS
     * NULL`). O resultado é exibido no campo `txtQuaEle`, permitindo um rápido
     * acompanhamento da quantidade de tarefas abertas.
     *
     * Funcionalidades: - **Consulta SQL com Contagem de Registros**: - Usa
     * `COUNT(*)` para retornar o número total de preventivas elétricas
     * trimestrais pendentes. - Filtra por `iduser`, garantindo que apenas
     * preventivas do usuário logado sejam contabilizadas.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Obtém o resultado e atribui ao campo `txtQuaEle`.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * Fluxo do Método: 1. Prepara e executa a consulta SQL segura. 2. Obtém a
     * contagem de preventivas elétricas trimestrais pendentes. 3. Exibe o
     * resultado no campo `txtQuaEle`. 4. Trata erros para evitar problemas na
     * execução.
     */
    private void ele_tri_quantida() {

        String sql = "select count(*) from form_eletrica_trimestral where iduser = ?  and situacao_prev is null ";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            rs = pst.executeQuery();
            if (rs.next()) {
                txtQuaEle.setText(rs.getString(1));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `ele_sem`. Este método consulta o banco de dados para obter
     * preventivas elétricas semestrais associadas a um usuário específico. Ele
     * recupera informações detalhadas sobre as preventivas e exibe os
     * resultados na tabela `tblPreEle`, formatando a exibição para melhor
     * legibilidade.
     *
     * Funcionalidades: - **Definição da Situação Inicial**: - A variável
     * `situacao1` é inicializada como `" "`, representando uma possível
     * situação de preventiva.
     *
     * - **Consulta SQL**: - Busca preventivas semestrais na tabela
     * `form_eletrica_semestral` com base no `iduser`. - Retorna registros onde
     * `situacao_prev` está `null` ou igual a `" "`. - Formata os nomes das
     * colunas para exibição: - `"N°"` → Identificador da preventiva. - `"R.E"`
     * → ID do usuário responsável. - `"Técnico"` → Nome do técnico responsável.
     * - `"Preventiva"` → Tipo da manutenção preventiva. - `"Setor"` → Setor
     * onde a preventiva foi realizada. - `"Equipamento"` → Nome do equipamento.
     * - `"Código"` → Código de identificação do equipamento. - `"Distribuição"`
     * → Tempo de distribuição da preventiva.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Atribui o resultado à tabela `tblPreEle` via
     * `DbUtils.resultSetToTableModel(rs)`.
     *
     * - **Formatação da Tabela**: - Chama `tabelaFormat()` para ajustar
     * visualmente a tabela.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * **Correção Necessária**: - A cláusula `OR` na consulta pode causar
     * resultados inesperados. Para evitar erros, uma abordagem mais segura
     * seria: ```sql SELECT id_form_ele_semestral AS 'N°', iduser AS 'R.E',
     * nome_prev AS Técnico, tipo_prev AS 'Preventiva', setor_prev AS Setor,
     * nome_equi_set AS Equipamento, cod_equi_set AS 'Código', tempo_dis AS
     * Distribuição FROM form_eletrica_semestral WHERE iduser = ? AND
     * (situacao_prev IS NULL OR situacao_prev = ?) ```
     *
     * Fluxo do Método: 1. Define a situação inicial das preventivas. 2. Prepara
     * e executa a consulta SQL. 3. Obtém os resultados e os exibe na tabela
     * `tblPreEle`. 4. Aplica formatação à tabela para melhor visualização. 5.
     * Trata erros para evitar falhas na execução.
     */
    private void ele_sem() {
        situacao1 = " ";

        String sql = "select id_form_ele_semestral as 'N°', iduser as 'R.E', nome_prev as Técnico, tipo_prev as 'Preventiva', setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', tempo_dis as Distribuição from  form_eletrica_semestral where iduser = ? and situacao_prev is null or iduser = ? and situacao_prev = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, id);
            pst.setString(3, situacao1);
            rs = pst.executeQuery();
            tblPreEle.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `ele_sem_pes`. Este método permite a pesquisa de preventivas
     * elétricas semestrais no banco de dados com base no usuário logado
     * (`iduser`) e no critério de busca inserido pelo usuário (`txtPesqEle`).
     * Ele filtra as preventivas por número de formulário
     * (`id_form_ele_semestral`) ou setor (`setor_prev`) e exibe os resultados
     * na tabela `tblPreEle`.
     *
     * Funcionalidades: - **Consulta SQL com Filtro de Pesquisa**: - Busca
     * registros na tabela `form_eletrica_semestral` onde `situacao_prev` é
     * `null` e `iduser` corresponde ao usuário logado. - Permite busca por
     * `id_form_ele_semestral` ou `setor_prev` usando a cláusula `LIKE` com `%`,
     * garantindo pesquisa parcial. - Retorna informações organizadas com alias
     * para melhor exibição na tabela: - `"N°"` → Número do formulário da
     * preventiva. - `"R.E"` → Registro do usuário responsável. - `"Técnico"` →
     * Nome do técnico responsável. - `"Preventiva"` → Tipo da manutenção
     * preventiva. - `"Setor"` → Setor onde foi realizada a preventiva. -
     * `"Equipamento"` → Nome do equipamento. - `"Código"` → Código de
     * identificação do equipamento. - `"Distribuição"` → Tempo de distribuição
     * da preventiva.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Aplica filtros de pesquisa a partir da entrada do usuário
     * (`txtPesqEle.getText()`). - Atribui os resultados à tabela `tblPreEle`
     * via `DbUtils.resultSetToTableModel(rs)`.
     *
     * - **Formatação da Tabela**: - Chama `tabelaFormat()` para organizar
     * visualmente os dados exibidos na tabela.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * **Correção Necessária**: - A consulta contém um erro na cláusula
     * `WHERE`: está usando `id_form_ele_mensal` quando deveria ser
     * `id_form_ele_semestral`. - Além disso, a estrutura com `OR` pode gerar
     * resultados inesperados. Uma abordagem mais correta seria: ```sql SELECT
     * id_form_ele_semestral AS 'N°', iduser AS 'R.E', nome_prev AS Técnico,
     * tipo_prev AS 'Preventiva', setor_prev AS Setor, nome_equi_set AS
     * Equipamento, cod_equi_set AS 'Código', tempo_dis AS Distribuição FROM
     * form_eletrica_semestral WHERE iduser = ? AND situacao_prev IS NULL AND
     * (id_form_ele_semestral LIKE ? OR setor_prev LIKE ?) ```
     *
     * Fluxo do Método: 1. Obtém o critério de pesquisa inserido pelo usuário.
     * 2. Prepara e executa a consulta SQL segura. 3. Exibe os resultados
     * filtrados na tabela `tblPreEle`. 4. Aplica formatação à tabela para
     * melhor visualização. 5. Trata erros para evitar problemas na execução.
     */
    private void ele_sem_pes() {

        String sql = "select id_form_ele_semestral as 'N°', iduser as 'R.E', nome_prev as Técnico, tipo_prev as 'Preventiva', setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', tempo_dis as Distribuição from  form_eletrica_semestral where iduser = ? and situacao_prev is null and id_form_ele_mensal like ? or iduser = ? and situacao_prev is null and setor_prev like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, txtPesqEle.getText() + "%");
            pst.setString(3, id);
            pst.setString(4, txtPesqEle.getText() + "%");
            rs = pst.executeQuery();
            tblPreEle.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `ele_sem_quantida`. Este método consulta o banco de dados para
     * contar o número de preventivas elétricas semestrais associadas ao usuário
     * logado (`iduser`) que ainda estão pendentes (`situacao_prev IS NULL`). O
     * resultado é exibido no campo `txtQuaEle`, permitindo um rápido
     * acompanhamento da quantidade de tarefas abertas.
     *
     * Funcionalidades: - **Consulta SQL com Contagem de Registros**: - Usa
     * `COUNT(*)` para retornar o número total de preventivas elétricas
     * semestrais pendentes. - Filtra por `iduser`, garantindo que apenas
     * preventivas do usuário logado sejam contabilizadas.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Obtém o resultado e atribui ao campo `txtQuaEle`.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * Fluxo do Método: 1. Prepara e executa a consulta SQL segura. 2. Obtém a
     * contagem de preventivas elétricas semestrais pendentes. 3. Exibe o
     * resultado no campo `txtQuaEle`. 4. Trata erros para evitar problemas na
     * execução.
     */
    private void ele_sem_quantida() {

        String sql = "select count(*) from form_eletrica_semestral where iduser = ?  and situacao_prev is null ";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            rs = pst.executeQuery();
            if (rs.next()) {
                txtQuaEle.setText(rs.getString(1));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `setar_ele`. Este método captura os dados da preventiva elétrica
     * selecionada na tabela `tblPreEle` e os exibe nos campos apropriados da
     * interface. Ele também ativa os botões de alteração e início da
     * preventiva, garantindo que o usuário possa interagir com os dados
     * selecionados.
     *
     * Funcionalidades: - **Obtenção de Dados da Preventiva Selecionada**: -
     * Obtém o índice da linha selecionada com `getSelectedRow()`. - Define
     * `txtIdPrevAlt`, `txtIdPrevIni` e `idprev` com o ID da preventiva. -
     * Define `txtTipPreAlt`, `txtTipPreIni` e `tipo` com o tipo de preventiva.
     *
     * - **Ativação de Botões**: - Chama `botão()` para realizar ações
     * adicionais. - Habilita `btnAltPrev` e `btnIniPrev`, permitindo que o
     * usuário altere ou inicie a preventiva.
     *
     * - **Atualização do Campo de Tipo de Preventiva**: - Define `txtTipPreIni`
     * com a combinação do tipo `"Eletrica"` e a variável `si`.
     *
     * - **Limpeza de Campos de Pesquisa**: - Define `txtPesqEle`, `txtPesqHid`,
     * `txtPesqCiv` e `txtPesqRef` como `null`, garantindo que os campos de
     * pesquisa sejam resetados.
     *
     * Fluxo do Método: 1. Obtém a linha selecionada na tabela. 2. Recupera e
     * exibe os dados da preventiva nos campos correspondentes. 3. Ativa os
     * botões de interação. 4. Atualiza o campo de tipo de preventiva. 5. Limpa
     * os campos de pesquisa.
     */
    private void setar_ele() {
        set = "Eletrica";
        int setar = tblPreEle.getSelectedRow();
        txtIdPrevAlt.setText(tblPreEle.getModel().getValueAt(setar, 0).toString());
        idprev = (tblPreEle.getModel().getValueAt(setar, 0).toString());
        txtIdPrevIni.setText(tblPreEle.getModel().getValueAt(setar, 0).toString());
        txtTipPreAlt.setText(tblPreEle.getModel().getValueAt(setar, 3).toString());
        tipo = (tblPreEle.getModel().getValueAt(setar, 3).toString());
        txtTipPreIni.setText(tblPreEle.getModel().getValueAt(setar, 3).toString());
        botão();
        btnAltPrev.setEnabled(true);
        btnIniPrev.setEnabled(true);

        txtTipPreIni.setText(set + " - " + si);
        txtPesqEle.setText(null);
        txtPesqHid.setText(null);
        txtPesqCiv.setText(null);
        txtPesqRef.setText(null);
    }

    /**
     * Método `hid_men`. Este método consulta o banco de dados para obter
     * preventivas hidráulicas mensais associadas a um usuário específico. Ele
     * recupera informações detalhadas sobre as preventivas e exibe os
     * resultados na tabela `tblPreHid`, formatando a exibição para melhor
     * legibilidade.
     *
     * Funcionalidades: - **Definição da Situação Inicial**: - A variável
     * `situacao1` é inicializada como `" "`, representando uma possível
     * situação de preventiva.
     *
     * - **Consulta SQL**: - Busca preventivas mensais na tabela
     * `form_hidraulica_mensal` com base no `iduser`. - Retorna registros onde
     * `situacao_prev` está `null` ou igual a `" "`. - Formata os nomes das
     * colunas para exibição: - `"N°"` → Identificador da preventiva. - `"R.E"`
     * → ID do usuário responsável. - `"Técnico"` → Nome do técnico responsável.
     * - `"Preventiva"` → Tipo da manutenção preventiva. - `"Setor"` → Setor
     * onde a preventiva foi realizada. - `"Equipamento"` → Nome do equipamento.
     * - `"Código"` → Código de identificação do equipamento. - `"Distribuição"`
     * → Tempo de distribuição da preventiva.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Atribui o resultado à tabela `tblPreHid` via
     * `DbUtils.resultSetToTableModel(rs)`.
     *
     * - **Formatação da Tabela**: - Chama `tabelaFormat1()` para ajustar
     * visualmente a tabela.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * **Correção Necessária**: - A cláusula `OR` na consulta pode causar
     * resultados inesperados. Para evitar erros, uma abordagem mais segura
     * seria: ```sql SELECT id_hidraulica_mensal AS 'N°', iduser AS 'R.E',
     * nome_prev AS Técnico, tipo_prev AS 'Preventiva', setor_prev AS Setor,
     * nome_equi_set AS Equipamento, cod_equi_set AS 'Código', tempo_dis AS
     * Distribuição FROM form_hidraulica_mensal WHERE iduser = ? AND
     * (situacao_prev IS NULL OR situacao_prev = ?) ```
     *
     * Fluxo do Método: 1. Define a situação inicial das preventivas. 2. Prepara
     * e executa a consulta SQL. 3. Obtém os resultados e os exibe na tabela
     * `tblPreHid`. 4. Aplica formatação à tabela para melhor visualização. 5.
     * Trata erros para evitar falhas na execução.
     */
    private void hid_men() {
        situacao1 = " ";

        String sql = "select id_hidraulica_mensal as 'N°', iduser as 'R.E', nome_prev as Técnico, tipo_prev as 'Preventiva', setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', tempo_dis as Distribuição from  form_hidraulica_mensal where iduser = ? and situacao_prev is null or iduser = ? and situacao_prev = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, id);
            pst.setString(3, situacao1);
            rs = pst.executeQuery();
            tblPreHid.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat1();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `hid_men_pes`. Este método permite a pesquisa de preventivas
     * hidráulicas mensais no banco de dados com base no usuário logado
     * (`iduser`) e no critério de busca inserido pelo usuário (`txtPesqHid`).
     * Ele filtra as preventivas por número de formulário
     * (`id_hidraulica_mensal`) ou setor (`setor_prev`) e exibe os resultados na
     * tabela `tblPreHid`.
     *
     * Funcionalidades: - **Consulta SQL com Filtro de Pesquisa**: - Busca
     * registros na tabela `form_hidraulica_mensal` onde `situacao_prev` é
     * `null` e `iduser` corresponde ao usuário logado. - Permite busca por
     * `id_hidraulica_mensal` ou `setor_prev` usando a cláusula `LIKE` com `%`,
     * garantindo pesquisa parcial. - Retorna informações organizadas com alias
     * para melhor exibição na tabela: - `"N°"` → Número do formulário da
     * preventiva. - `"R.E"` → Registro do usuário responsável. - `"Técnico"` →
     * Nome do técnico responsável. - `"Preventiva"` → Tipo da manutenção
     * preventiva. - `"Setor"` → Setor onde foi realizada a preventiva. -
     * `"Equipamento"` → Nome do equipamento. - `"Código"` → Código de
     * identificação do equipamento. - `"Distribuição"` → Tempo de distribuição
     * da preventiva.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Aplica filtros de pesquisa a partir da entrada do usuário
     * (`txtPesqHid.getText()`). - Atribui os resultados à tabela `tblPreHid`
     * via `DbUtils.resultSetToTableModel(rs)`.
     *
     * - **Formatação da Tabela**: - Chama `tabelaFormat1()` para organizar
     * visualmente os dados exibidos na tabela.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * **Correção Necessária**: - A consulta contém um erro na cláusula
     * `WHERE`: a estrutura com `OR` pode gerar resultados inesperados. Uma
     * abordagem mais correta seria: ```sql SELECT id_hidraulica_mensal AS 'N°',
     * iduser AS 'R.E', nome_prev AS Técnico, tipo_prev AS 'Preventiva',
     * setor_prev AS Setor, nome_equi_set AS Equipamento, cod_equi_set AS
     * 'Código', tempo_dis AS Distribuição FROM form_hidraulica_mensal WHERE
     * iduser = ? AND situacao_prev IS NULL AND (id_hidraulica_mensal LIKE ? OR
     * setor_prev LIKE ?) ```
     *
     * Fluxo do Método: 1. Obtém o critério de pesquisa inserido pelo usuário.
     * 2. Prepara e executa a consulta SQL segura. 3. Exibe os resultados
     * filtrados na tabela `tblPreHid`. 4. Aplica formatação à tabela para
     * melhor visualização. 5. Trata erros para evitar problemas na execução.
     */
    private void hid_men_pes() {

        situacao1 = " ";

        String sql = "select id_hidraulica_mensal as 'N°', iduser as 'R.E', nome_prev as Técnico, tipo_prev as 'Preventiva', setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', tempo_dis as Distribuição from  form_hidraulica_mensal where iduser = ? and situacao_prev is null and id_hidraulica_mensal like ? or iduser = ? and situacao_prev is null and setor_prev like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, txtPesqHid.getText() + "%");
            pst.setString(3, id);
            pst.setString(4, txtPesqHid.getText() + "%");
            rs = pst.executeQuery();
            tblPreHid.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat1();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `hid_men_quantida`. Este método consulta o banco de dados para
     * contar o número de preventivas hidráulicas mensais associadas ao usuário
     * logado (`iduser`) que ainda estão pendentes (`situacao_prev IS NULL`). O
     * resultado é exibido no campo `txtQuaHid`, permitindo um rápido
     * acompanhamento da quantidade de tarefas abertas.
     *
     * Funcionalidades: - **Consulta SQL com Contagem de Registros**: - Usa
     * `COUNT(*)` para retornar o número total de preventivas hidráulicas
     * mensais pendentes. - Filtra por `iduser`, garantindo que apenas
     * preventivas do usuário logado sejam contabilizadas.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Obtém o resultado e atribui ao campo `txtQuaHid`.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * Fluxo do Método: 1. Prepara e executa a consulta SQL segura. 2. Obtém a
     * contagem de preventivas hidráulicas mensais pendentes. 3. Exibe o
     * resultado no campo `txtQuaHid`. 4. Trata erros para evitar problemas na
     * execução.
     */
    private void hid_men_quantida() {

        String sql = "select count(*) from form_hidraulica_mensal where iduser = ?  and situacao_prev is null ";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            rs = pst.executeQuery();
            if (rs.next()) {
                txtQuaHid.setText(rs.getString(1));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `hid_tri`. Este método consulta o banco de dados para obter
     * preventivas hidráulicas trimestrais associadas a um usuário específico.
     * Ele recupera informações detalhadas sobre as preventivas e exibe os
     * resultados na tabela `tblPreHid`, formatando a exibição para melhor
     * legibilidade.
     *
     * Funcionalidades: - **Definição da Situação Inicial**: - A variável
     * `situacao1` é inicializada como `" "`, representando uma possível
     * situação de preventiva.
     *
     * - **Consulta SQL**: - Busca preventivas trimestrais na tabela
     * `form_hidraulica_trimestral` com base no `iduser`. - Retorna registros
     * onde `situacao_prev` está `null` ou igual a `" "`. - Formata os nomes das
     * colunas para exibição: - `"N°"` → Identificador da preventiva. - `"R.E"`
     * → ID do usuário responsável. - `"Técnico"` → Nome do técnico responsável.
     * - `"Preventiva"` → Tipo da manutenção preventiva. - `"Setor"` → Setor
     * onde a preventiva foi realizada. - `"Equipamento"` → Nome do equipamento.
     * - `"Código"` → Código de identificação do equipamento. - `"Distribuição"`
     * → Tempo de distribuição da preventiva.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Atribui o resultado à tabela `tblPreHid` via
     * `DbUtils.resultSetToTableModel(rs)`.
     *
     * - **Formatação da Tabela**: - Chama `tabelaFormat1()` para ajustar
     * visualmente a tabela.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * **Correção Necessária**: - A cláusula `OR` na consulta pode causar
     * resultados inesperados. Para evitar erros, uma abordagem mais segura
     * seria: ```sql SELECT id_hidraulica_trimestral AS 'N°', iduser AS 'R.E',
     * nome_prev AS Técnico, tipo_prev AS 'Preventiva', setor_prev AS Setor,
     * nome_equi_set AS Equipamento, cod_equi_set AS 'Código', tempo_dis AS
     * Distribuição FROM form_hidraulica_trimestral WHERE iduser = ? AND
     * (situacao_prev IS NULL OR situacao_prev = ?) ```
     *
     * Fluxo do Método: 1. Define a situação inicial das preventivas. 2. Prepara
     * e executa a consulta SQL. 3. Obtém os resultados e os exibe na tabela
     * `tblPreHid`. 4. Aplica formatação à tabela para melhor visualização. 5.
     * Trata erros para evitar falhas na execução.
     */
    private void hid_tri() {
        situacao1 = " ";

        String sql = "select id_hidraulica_trimestral as 'N°', iduser as 'R.E', nome_prev as Técnico, tipo_prev as 'Preventiva', setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', tempo_dis as Distribuição from  form_hidraulica_trimestral where iduser = ? and situacao_prev is null or iduser = ? and situacao_prev = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, id);
            pst.setString(3, situacao1);
            rs = pst.executeQuery();
            tblPreHid.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat1();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `hid_tri_pes`. Este método permite a pesquisa de preventivas
     * hidráulicas trimestrais no banco de dados com base no usuário logado
     * (`iduser`) e no critério de busca inserido pelo usuário (`txtPesqHid`).
     * Ele filtra as preventivas por número de formulário
     * (`id_hidraulica_trimestral`) ou setor (`setor_prev`) e exibe os
     * resultados na tabela `tblPreHid`.
     *
     * Funcionalidades: - **Consulta SQL com Filtro de Pesquisa**: - Busca
     * registros na tabela `form_hidraulica_trimestral` onde `situacao_prev` é
     * `null` e `iduser` corresponde ao usuário logado. - Permite busca por
     * `id_hidraulica_trimestral` ou `setor_prev` usando a cláusula `LIKE` com
     * `%`, garantindo pesquisa parcial. - Retorna informações organizadas com
     * alias para melhor exibição na tabela: - `"N°"` → Número do formulário da
     * preventiva. - `"R.E"` → Registro do usuário responsável. - `"Técnico"` →
     * Nome do técnico responsável. - `"Preventiva"` → Tipo da manutenção
     * preventiva. - `"Setor"` → Setor onde foi realizada a preventiva. -
     * `"Equipamento"` → Nome do equipamento. - `"Código"` → Código de
     * identificação do equipamento. - `"Distribuição"` → Tempo de distribuição
     * da preventiva.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Aplica filtros de pesquisa a partir da entrada do usuário
     * (`txtPesqHid.getText()`). - Atribui os resultados à tabela `tblPreHid`
     * via `DbUtils.resultSetToTableModel(rs)`.
     *
     * - **Formatação da Tabela**: - Chama `tabelaFormat1()` para organizar
     * visualmente os dados exibidos na tabela.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * **Correção Necessária**: - A consulta contém um erro na cláusula
     * `WHERE`: a estrutura com `OR` pode gerar resultados inesperados. Uma
     * abordagem mais correta seria: ```sql SELECT id_hidraulica_trimestral AS
     * 'N°', iduser AS 'R.E', nome_prev AS Técnico, tipo_prev AS 'Preventiva',
     * setor_prev AS Setor, nome_equi_set AS Equipamento, cod_equi_set AS
     * 'Código', tempo_dis AS Distribuição FROM form_hidraulica_trimestral WHERE
     * iduser = ? AND situacao_prev IS NULL AND (id_hidraulica_trimestral LIKE ?
     * OR setor_prev LIKE ?) ```
     *
     * Fluxo do Método: 1. Obtém o critério de pesquisa inserido pelo usuário.
     * 2. Prepara e executa a consulta SQL segura. 3. Exibe os resultados
     * filtrados na tabela `tblPreHid`. 4. Aplica formatação à tabela para
     * melhor visualização. 5. Trata erros para evitar problemas na execução.
     */
    private void hid_tri_pes() {

        situacao1 = " ";

        String sql = "select id_hidraulica_trimestral as 'N°', iduser as 'R.E', nome_prev as Técnico, tipo_prev as 'Preventiva', setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', tempo_dis as Distribuição from  form_hidraulica_trimestral where iduser = ? and situacao_prev is null and id_hidraulica_mensal like ? or iduser = ? and situacao_prev is null and setor_prev like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, txtPesqHid.getText() + "%");
            pst.setString(3, id);
            pst.setString(4, txtPesqHid.getText() + "%");
            rs = pst.executeQuery();
            tblPreHid.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat1();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `hid_tri_quantida`. Este método consulta o banco de dados para
     * contar o número de preventivas hidráulicas trimestrais associadas ao
     * usuário logado (`iduser`) que ainda estão pendentes (`situacao_prev IS
     * NULL`). O resultado é exibido no campo `txtQuaHid`, permitindo um rápido
     * acompanhamento da quantidade de tarefas abertas.
     *
     * Funcionalidades: - **Consulta SQL com Contagem de Registros**: - Usa
     * `COUNT(*)` para retornar o número total de preventivas hidráulicas
     * trimestrais pendentes. - Filtra por `iduser`, garantindo que apenas
     * preventivas do usuário logado sejam contabilizadas.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Obtém o resultado e atribui ao campo `txtQuaHid`.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * Fluxo do Método: 1. Prepara e executa a consulta SQL segura. 2. Obtém a
     * contagem de preventivas hidráulicas trimestrais pendentes. 3. Exibe o
     * resultado no campo `txtQuaHid`. 4. Trata erros para evitar problemas na
     * execução.
     */
    private void hid_tri_quantida() {

        String sql = "select count(*) from form_hidraulica_trimestral where iduser = ?  and situacao_prev is null ";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            rs = pst.executeQuery();
            if (rs.next()) {
                txtQuaHid.setText(rs.getString(1));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `hid_sem`. Este método consulta o banco de dados para obter
     * preventivas hidráulicas semestrais associadas a um usuário específico.
     * Ele recupera informações detalhadas sobre as preventivas e exibe os
     * resultados na tabela `tblPreHid`, formatando a exibição para melhor
     * legibilidade.
     *
     * Funcionalidades: - **Definição da Situação Inicial**: - A variável
     * `situacao1` é inicializada como `" "`, representando uma possível
     * situação de preventiva.
     *
     * - **Consulta SQL**: - Busca preventivas semestrais na tabela
     * `form_hidraulica_semestral` com base no `iduser`. - Retorna registros
     * onde `situacao_prev` está `null` ou igual a `" "`. - Formata os nomes das
     * colunas para exibição: - `"N°"` → Identificador da preventiva. - `"R.E"`
     * → ID do usuário responsável. - `"Técnico"` → Nome do técnico responsável.
     * - `"Preventiva"` → Tipo da manutenção preventiva. - `"Setor"` → Setor
     * onde a preventiva foi realizada. - `"Equipamento"` → Nome do equipamento.
     * - `"Código"` → Código de identificação do equipamento. - `"Distribuição"`
     * → Tempo de distribuição da preventiva.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Atribui o resultado à tabela `tblPreHid` via
     * `DbUtils.resultSetToTableModel(rs)`.
     *
     * - **Formatação da Tabela**: - Chama `tabelaFormat1()` para ajustar
     * visualmente a tabela.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * **Correção Necessária**: - A cláusula `OR` na consulta pode causar
     * resultados inesperados. Para evitar erros, uma abordagem mais segura
     * seria: ```sql SELECT id_hidraulica_semestral AS 'N°', iduser AS 'R.E',
     * nome_prev AS Técnico, tipo_prev AS 'Preventiva', setor_prev AS Setor,
     * nome_equi_set AS Equipamento, cod_equi_set AS 'Código', tempo_dis AS
     * Distribuição FROM form_hidraulica_semestral WHERE iduser = ? AND
     * (situacao_prev IS NULL OR situacao_prev = ?) ```
     *
     * Fluxo do Método: 1. Define a situação inicial das preventivas. 2. Prepara
     * e executa a consulta SQL. 3. Obtém os resultados e os exibe na tabela
     * `tblPreHid`. 4. Aplica formatação à tabela para melhor visualização. 5.
     * Trata erros para evitar falhas na execução.
     */
    private void hid_sem() {
        situacao1 = " ";;

        String sql = "select id_hidraulica_semestral as 'N°', iduser as 'R.E', nome_prev as Técnico, tipo_prev as 'Preventiva', setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', tempo_dis as Distribuição from  form_hidraulica_semestral where iduser = ? and situacao_prev is null or iduser = ? and situacao_prev = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, id);
            pst.setString(3, situacao1);
            rs = pst.executeQuery();
            tblPreHid.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat1();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `hid_sem_pes`. Este método permite a pesquisa de preventivas
     * hidráulicas semestrais no banco de dados com base no usuário logado
     * (`iduser`) e no critério de busca inserido pelo usuário (`txtPesqHid`).
     * Ele filtra as preventivas por número de formulário
     * (`id_hidraulica_semestral`) ou setor (`setor_prev`) e exibe os resultados
     * na tabela `tblPreHid`.
     *
     * Funcionalidades: - **Consulta SQL com Filtro de Pesquisa**: - Busca
     * registros na tabela `form_hidraulica_semestral` onde `situacao_prev` é
     * `null` e `iduser` corresponde ao usuário logado. - Permite busca por
     * `id_hidraulica_semestral` ou `setor_prev` usando a cláusula `LIKE` com
     * `%`, garantindo pesquisa parcial. - Retorna informações organizadas com
     * alias para melhor exibição na tabela: - `"N°"` → Número do formulário da
     * preventiva. - `"R.E"` → Registro do usuário responsável. - `"Técnico"` →
     * Nome do técnico responsável. - `"Preventiva"` → Tipo da manutenção
     * preventiva. - `"Setor"` → Setor onde foi realizada a preventiva. -
     * `"Equipamento"` → Nome do equipamento. - `"Código"` → Código de
     * identificação do equipamento. - `"Distribuição"` → Tempo de distribuição
     * da preventiva.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Aplica filtros de pesquisa a partir da entrada do usuário
     * (`txtPesqHid.getText()`). - Atribui os resultados à tabela `tblPreHid`
     * via `DbUtils.resultSetToTableModel(rs)`.
     *
     * - **Formatação da Tabela**: - Chama `tabelaFormat1()` para organizar
     * visualmente os dados exibidos na tabela.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * **Correção Necessária**: - A consulta contém um erro na cláusula
     * `WHERE`: está utilizando `id_hidraulica_mensal` quando deveria ser
     * `id_hidraulica_semestral`. - Além disso, a estrutura com `OR` pode gerar
     * resultados inesperados. Uma abordagem mais correta seria: ```sql SELECT
     * id_hidraulica_semestral AS 'N°', iduser AS 'R.E', nome_prev AS Técnico,
     * tipo_prev AS 'Preventiva', setor_prev AS Setor, nome_equi_set AS
     * Equipamento, cod_equi_set AS 'Código', tempo_dis AS Distribuição FROM
     * form_hidraulica_semestral WHERE iduser = ? AND situacao_prev IS NULL AND
     * (id_hidraulica_semestral LIKE ? OR setor_prev LIKE ?) ```
     *
     * Fluxo do Método: 1. Obtém o critério de pesquisa inserido pelo usuário.
     * 2. Prepara e executa a consulta SQL segura. 3. Exibe os resultados
     * filtrados na tabela `tblPreHid`. 4. Aplica formatação à tabela para
     * melhor visualização. 5. Trata erros para evitar problemas na execução.
     */
    private void hid_sem_pes() {

        situacao1 = " ";

        String sql = "select id_hidraulica_semestral as 'N°', iduser as 'R.E', nome_prev as Técnico, tipo_prev as 'Preventiva', setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', tempo_dis as Distribuição from  form_hidraulica_semestral where iduser = ? and situacao_prev is null and id_hidraulica_mensal like ? or iduser = ? and situacao_prev is null and setor_prev like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, txtPesqHid.getText() + "%");
            pst.setString(3, id);
            pst.setString(4, txtPesqHid.getText() + "%");
            rs = pst.executeQuery();
            tblPreHid.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat1();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `hid_sem_quantida`. Este método consulta o banco de dados para
     * contar o número de preventivas hidráulicas semestrais associadas ao
     * usuário logado (`iduser`) que ainda estão pendentes (`situacao_prev IS
     * NULL`). O resultado é exibido no campo `txtQuaHid`, permitindo um rápido
     * acompanhamento da quantidade de tarefas abertas.
     *
     * Funcionalidades: - **Consulta SQL com Contagem de Registros**: - Usa
     * `COUNT(*)` para retornar o número total de preventivas hidráulicas
     * semestrais pendentes. - Filtra por `iduser`, garantindo que apenas
     * preventivas do usuário logado sejam contabilizadas.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Obtém o resultado e atribui ao campo `txtQuaHid`.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * Fluxo do Método: 1. Prepara e executa a consulta SQL segura. 2. Obtém a
     * contagem de preventivas hidráulicas semestrais pendentes. 3. Exibe o
     * resultado no campo `txtQuaHid`. 4. Trata erros para evitar problemas na
     * execução.
     */
    private void hid_sem_quantida() {

        String sql = "select count(*) from form_hidraulica_semestral where iduser = ?  and situacao_prev is null ";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            rs = pst.executeQuery();
            if (rs.next()) {
                txtQuaHid.setText(rs.getString(1));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `setar_hid`. Este método captura os dados da preventiva hidráulica
     * selecionada na tabela `tblPreHid` e os exibe nos campos apropriados da
     * interface. Ele também ativa os botões de alteração e início da
     * preventiva, garantindo que o usuário possa interagir com os dados
     * selecionados.
     *
     * Funcionalidades: - **Obtenção de Dados da Preventiva Selecionada**: -
     * Obtém o índice da linha selecionada com `getSelectedRow()`. - Define
     * `txtIdPrevAlt`, `txtIdPrevIni` e `idprev` com o ID da preventiva. -
     * Define `txtTipPreAlt`, `txtTipPreIni` e `tipo` com o tipo de preventiva.
     *
     * - **Ativação de Botões**: - Chama `botão()` para realizar ações
     * adicionais. - Habilita `btnAltPrev` e `btnIniPrev`, permitindo que o
     * usuário altere ou inicie a preventiva.
     *
     * - **Atualização do Campo de Tipo de Preventiva**: - Define `txtTipPreIni`
     * com a combinação do tipo `"Hidráulica"` e a variável `si`.
     *
     * - **Limpeza de Campos de Pesquisa**: - Define `txtPesqEle`, `txtPesqHid`,
     * `txtPesqCiv` e `txtPesqRef` como `null`, garantindo que os campos de
     * pesquisa sejam resetados.
     *
     * Fluxo do Método: 1. Obtém a linha selecionada na tabela. 2. Recupera e
     * exibe os dados da preventiva nos campos correspondentes. 3. Ativa os
     * botões de interação. 4. Atualiza o campo de tipo de preventiva. 5. Limpa
     * os campos de pesquisa.
     */
    private void setar_hid() {
        set = "Hidráulica";
        int setar = tblPreHid.getSelectedRow();
        txtIdPrevAlt.setText(tblPreHid.getModel().getValueAt(setar, 0).toString());
        idprev = (tblPreHid.getModel().getValueAt(setar, 0).toString());
        txtIdPrevIni.setText(tblPreHid.getModel().getValueAt(setar, 0).toString());
        txtTipPreAlt.setText(tblPreHid.getModel().getValueAt(setar, 3).toString());
        tipo = (tblPreHid.getModel().getValueAt(setar, 3).toString());
        txtTipPreIni.setText(tblPreHid.getModel().getValueAt(setar, 3).toString());
        botão();
        btnAltPrev.setEnabled(true);
        btnIniPrev.setEnabled(true);

        txtTipPreIni.setText(set + " - " + si);
        txtPesqEle.setText(null);
        txtPesqHid.setText(null);
        txtPesqCiv.setText(null);
        txtPesqRef.setText(null);
    }

    /**
     * Método `civ_men`. Este método consulta o banco de dados para obter
     * preventivas civis mensais associadas a um usuário específico. Ele
     * recupera informações detalhadas sobre as preventivas e exibe os
     * resultados na tabela `tblPreCivil`, formatando a exibição para melhor
     * legibilidade.
     *
     * Funcionalidades: - **Definição da Situação Inicial**: - A variável
     * `situacao1` é inicializada como `" "`, representando uma possível
     * situação de preventiva.
     *
     * - **Consulta SQL**: - Busca preventivas mensais na tabela
     * `form_civil_mensal` com base no `iduser`. - Retorna registros onde
     * `situacao_prev` está `null` ou igual a `" "`. - Formata os nomes das
     * colunas para exibição: - `"N°"` → Identificador da preventiva. - `"R.E"`
     * → ID do usuário responsável. - `"Técnico"` → Nome do técnico responsável.
     * - `"Preventiva"` → Tipo da manutenção preventiva. - `"Setor"` → Setor
     * onde a preventiva foi realizada. - `"Equipamento"` → Nome do equipamento.
     * - `"Código"` → Código de identificação do equipamento. - `"Distribuição"`
     * → Tempo de distribuição da preventiva.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Atribui o resultado à tabela `tblPreCivil` via
     * `DbUtils.resultSetToTableModel(rs)`.
     *
     * - **Formatação da Tabela**: - Chama `tabelaFormat2()` para ajustar
     * visualmente a tabela.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * **Correção Necessária**: - A cláusula `OR` na consulta pode causar
     * resultados inesperados. Para evitar erros, uma abordagem mais segura
     * seria: ```sql SELECT id_civil_mensal AS 'N°', iduser AS 'R.E', nome_prev
     * AS Técnico, tipo_prev AS 'Preventiva', setor_prev AS Setor, nome_equi_set
     * AS Equipamento, cod_equi_set AS 'Código', tempo_dis AS Distribuição FROM
     * form_civil_mensal WHERE iduser = ? AND (situacao_prev IS NULL OR
     * situacao_prev = ?) ```
     *
     * Fluxo do Método: 1. Define a situação inicial das preventivas. 2. Prepara
     * e executa a consulta SQL. 3. Obtém os resultados e os exibe na tabela
     * `tblPreCivil`. 4. Aplica formatação à tabela para melhor visualização. 5.
     * Trata erros para evitar falhas na execução.
     */
    private void civ_men() {
        situacao1 = " ";

        String sql = "select id_civil_mensal as 'N°', iduser as 'R.E', nome_prev as Técnico, tipo_prev as 'Preventiva', setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', tempo_dis as Distribuição from  form_civil_mensal where iduser = ? and situacao_prev is null or iduser = ? and situacao_prev = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, id);
            pst.setString(3, situacao1);
            rs = pst.executeQuery();
            tblPreCivil.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat2();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `civ_men_pes`. Este método permite a pesquisa de preventivas civis
     * mensais no banco de dados com base no usuário logado (`iduser`) e no
     * critério de busca inserido pelo usuário (`txtPesqCiv`). Ele filtra as
     * preventivas por número de formulário (`id_civil_mensal`) ou setor
     * (`setor_prev`) e exibe os resultados na tabela `tblPreCivil`.
     *
     * Funcionalidades: - **Consulta SQL com Filtro de Pesquisa**: - Busca
     * registros na tabela `form_civil_mensal` onde `situacao_prev` é `null` e
     * `iduser` corresponde ao usuário logado. - Permite busca por
     * `id_civil_mensal` ou `setor_prev` usando a cláusula `LIKE` com `%`,
     * garantindo pesquisa parcial. - Retorna informações organizadas com alias
     * para melhor exibição na tabela: - `"N°"` → Número do formulário da
     * preventiva. - `"R.E"` → Registro do usuário responsável. - `"Técnico"` →
     * Nome do técnico responsável. - `"Preventiva"` → Tipo da manutenção
     * preventiva. - `"Setor"` → Setor onde foi realizada a preventiva. -
     * `"Equipamento"` → Nome do equipamento. - `"Código"` → Código de
     * identificação do equipamento. - `"Distribuição"` → Tempo de distribuição
     * da preventiva.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Aplica filtros de pesquisa a partir da entrada do usuário
     * (`txtPesqCiv.getText()`). - Atribui os resultados à tabela `tblPreCivil`
     * via `DbUtils.resultSetToTableModel(rs)`.
     *
     * - **Formatação da Tabela**: - Chama `tabelaFormat2()` para organizar
     * visualmente os dados exibidos na tabela.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * **Correção Necessária**: - A consulta contém um erro na cláusula
     * `WHERE`: a estrutura com `OR` pode gerar resultados inesperados. Uma
     * abordagem mais correta seria: ```sql SELECT id_civil_mensal AS 'N°',
     * iduser AS 'R.E', nome_prev AS Técnico, tipo_prev AS 'Preventiva',
     * setor_prev AS Setor, nome_equi_set AS Equipamento, cod_equi_set AS
     * 'Código', tempo_dis AS Distribuição FROM form_civil_mensal WHERE iduser =
     * ? AND situacao_prev IS NULL AND (id_civil_mensal LIKE ? OR setor_prev
     * LIKE ?) ```
     *
     * Fluxo do Método: 1. Obtém o critério de pesquisa inserido pelo usuário.
     * 2. Prepara e executa a consulta SQL segura. 3. Exibe os resultados
     * filtrados na tabela `tblPreCivil`. 4. Aplica formatação à tabela para
     * melhor visualização. 5. Trata erros para evitar problemas na execução.
     */
    private void civ_men_pes() {

        situacao1 = " ";

        String sql = "select id_civil_mensal as 'N°', iduser as 'R.E', nome_prev as Técnico, tipo_prev as 'Preventiva', setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', tempo_dis as Distribuição from  form_civil_mensal where iduser = ? and situacao_prev is null and id_civil_mensal like ? or iduser = ? and situacao_prev is null and setor_prev like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, txtPesqCiv.getText() + "%");
            pst.setString(3, id);
            pst.setString(4, txtPesqCiv.getText() + "%");
            rs = pst.executeQuery();
            tblPreCivil.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat2();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `civ_men_quantida`. Este método consulta o banco de dados para
     * contar o número de preventivas civis mensais associadas ao usuário logado
     * (`iduser`) que ainda estão pendentes (`situacao_prev IS NULL`). O
     * resultado é exibido no campo `txtQuaCiv`, permitindo um rápido
     * acompanhamento da quantidade de tarefas abertas.
     *
     * Funcionalidades: - **Consulta SQL com Contagem de Registros**: - Usa
     * `COUNT(*)` para retornar o número total de preventivas civis mensais
     * pendentes. - Filtra por `iduser`, garantindo que apenas preventivas do
     * usuário logado sejam contabilizadas.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Obtém o resultado e atribui ao campo `txtQuaCiv`.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * Fluxo do Método: 1. Prepara e executa a consulta SQL segura. 2. Obtém a
     * contagem de preventivas civis mensais pendentes. 3. Exibe o resultado no
     * campo `txtQuaCiv`. 4. Trata erros para evitar problemas na execução.
     */
    private void civ_men_quantida() {

        String sql = "select count(*) from form_civil_mensal where iduser = ?  and situacao_prev is null ";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            rs = pst.executeQuery();
            if (rs.next()) {
                txtQuaCiv.setText(rs.getString(1));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `civ_tri`. Este método consulta o banco de dados para obter
     * preventivas civis trimestrais associadas a um usuário específico. Ele
     * recupera informações detalhadas sobre as preventivas e exibe os
     * resultados na tabela `tblPreCivil`, formatando a exibição para melhor
     * legibilidade.
     *
     * Funcionalidades: - **Definição da Situação Inicial**: - A variável
     * `situacao1` é inicializada como `" "`, representando uma possível
     * situação de preventiva.
     *
     * - **Consulta SQL**: - Busca preventivas trimestrais na tabela
     * `form_civil_trimestral` com base no `iduser`. - Retorna registros onde
     * `situacao_prev` está `null` ou igual a `" "`. - Formata os nomes das
     * colunas para exibição: - `"N°"` → Identificador da preventiva. - `"R.E"`
     * → ID do usuário responsável. - `"Técnico"` → Nome do técnico responsável.
     * - `"Preventiva"` → Tipo da manutenção preventiva. - `"Setor"` → Setor
     * onde a preventiva foi realizada. - `"Equipamento"` → Nome do equipamento.
     * - `"Código"` → Código de identificação do equipamento. - `"Distribuição"`
     * → Tempo de distribuição da preventiva.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Atribui o resultado à tabela `tblPreCivil` via
     * `DbUtils.resultSetToTableModel(rs)`.
     *
     * - **Formatação da Tabela**: - Chama `tabelaFormat2()` para ajustar
     * visualmente a tabela.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * **Correção Necessária**: - A cláusula `OR` na consulta pode causar
     * resultados inesperados. Para evitar erros, uma abordagem mais segura
     * seria: ```sql SELECT id_civil_trimestral AS 'N°', iduser AS 'R.E',
     * nome_prev AS Técnico, tipo_prev AS 'Preventiva', setor_prev AS Setor,
     * nome_equi_set AS Equipamento, cod_equi_set AS 'Código', tempo_dis AS
     * Distribuição FROM form_civil_trimestral WHERE iduser = ? AND
     * (situacao_prev IS NULL OR situacao_prev = ?) ```
     *
     * Fluxo do Método: 1. Define a situação inicial das preventivas. 2. Prepara
     * e executa a consulta SQL. 3. Obtém os resultados e os exibe na tabela
     * `tblPreCivil`. 4. Aplica formatação à tabela para melhor visualização. 5.
     * Trata erros para evitar falhas na execução.
     */
    private void civ_tri() {
        situacao1 = " ";

        String sql = "select id_civil_trimestral as 'N°', iduser as 'R.E', nome_prev as Técnico, tipo_prev as 'Preventiva', setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', tempo_dis as Distribuição from  form_civil_trimestral where iduser = ? and situacao_prev is null or iduser = ? and situacao_prev = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, id);
            pst.setString(3, situacao1);
            rs = pst.executeQuery();
            tblPreCivil.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat2();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `civ_tri_pes`. Este método permite a pesquisa de preventivas civis
     * trimestrais no banco de dados com base no usuário logado (`iduser`) e no
     * critério de busca inserido pelo usuário (`txtPesqCiv`). Ele filtra as
     * preventivas por número de formulário (`id_civil_trimestral`) ou setor
     * (`setor_prev`) e exibe os resultados na tabela `tblPreCivil`.
     *
     * Funcionalidades: - **Consulta SQL com Filtro de Pesquisa**: - Busca
     * registros na tabela `form_civil_trimestral` onde `situacao_prev` é `null`
     * e `iduser` corresponde ao usuário logado. - Permite busca por
     * `id_civil_trimestral` ou `setor_prev` usando a cláusula `LIKE` com `%`,
     * garantindo pesquisa parcial. - Retorna informações organizadas com alias
     * para melhor exibição na tabela: - `"N°"` → Número do formulário da
     * preventiva. - `"R.E"` → Registro do usuário responsável. - `"Técnico"` →
     * Nome do técnico responsável. - `"Preventiva"` → Tipo da manutenção
     * preventiva. - `"Setor"` → Setor onde foi realizada a preventiva. -
     * `"Equipamento"` → Nome do equipamento. - `"Código"` → Código de
     * identificação do equipamento. - `"Distribuição"` → Tempo de distribuição
     * da preventiva.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Aplica filtros de pesquisa a partir da entrada do usuário
     * (`txtPesqCiv.getText()`). - Atribui os resultados à tabela `tblPreCivil`
     * via `DbUtils.resultSetToTableModel(rs)`.
     *
     * - **Formatação da Tabela**: - Chama `tabelaFormat2()` para organizar
     * visualmente os dados exibidos na tabela.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * **Correção Necessária**: - A consulta contém um erro na cláusula
     * `WHERE`: está utilizando `id_civil_mensal` quando deveria ser
     * `id_civil_trimestral`. - Além disso, a estrutura com `OR` pode gerar
     * resultados inesperados. Uma abordagem mais correta seria: ```sql SELECT
     * id_civil_trimestral AS 'N°', iduser AS 'R.E', nome_prev AS Técnico,
     * tipo_prev AS 'Preventiva', setor_prev AS Setor, nome_equi_set AS
     * Equipamento, cod_equi_set AS 'Código', tempo_dis AS Distribuição FROM
     * form_civil_trimestral WHERE iduser = ? AND situacao_prev IS NULL AND
     * (id_civil_trimestral LIKE ? OR setor_prev LIKE ?) ```
     *
     * Fluxo do Método: 1. Obtém o critério de pesquisa inserido pelo usuário.
     * 2. Prepara e executa a consulta SQL segura. 3. Exibe os resultados
     * filtrados na tabela `tblPreCivil`. 4. Aplica formatação à tabela para
     * melhor visualização. 5. Trata erros para evitar problemas na execução.
     */
    private void civ_tri_pes() {

        String sql = "select id_civil_trimestral as 'N°', iduser as 'R.E', nome_prev as Técnico, tipo_prev as 'Preventiva', setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', tempo_dis as Distribuição from form_civil_trimestral where iduser = ? and situacao_prev is null and id_civil_mensal like ? or iduser = ? and situacao_prev is null and setor_prev like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, txtPesqCiv.getText() + "%");
            pst.setString(3, id);
            pst.setString(4, txtPesqCiv.getText() + "%");
            rs = pst.executeQuery();
            tblPreCivil.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat2();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `civ_sem`. Este método consulta o banco de dados para obter
     * preventivas civis semestrais associadas a um usuário específico. Ele
     * recupera informações detalhadas sobre as preventivas e exibe os
     * resultados na tabela `tblPreCivil`, formatando a exibição para melhor
     * legibilidade.
     *
     * Funcionalidades: - **Definição da Situação Inicial**: - A variável
     * `situacao1` é inicializada como `" "`, representando uma possível
     * situação de preventiva.
     *
     * - **Consulta SQL**: - Busca preventivas semestrais na tabela
     * `form_civil_semestral` com base no `iduser`. - Retorna registros onde
     * `situacao_prev` está `null` ou igual a `" "`. - Formata os nomes das
     * colunas para exibição: - `"N°"` → Identificador da preventiva. - `"R.E"`
     * → ID do usuário responsável. - `"Técnico"` → Nome do técnico responsável.
     * - `"Preventiva"` → Tipo da manutenção preventiva. - `"Setor"` → Setor
     * onde a preventiva foi realizada. - `"Equipamento"` → Nome do equipamento.
     * - `"Código"` → Código de identificação do equipamento. - `"Distribuição"`
     * → Tempo de distribuição da preventiva.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Atribui o resultado à tabela `tblPreCivil` via
     * `DbUtils.resultSetToTableModel(rs)`.
     *
     * - **Formatação da Tabela**: - Chama `tabelaFormat2()` para ajustar
     * visualmente a tabela.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * **Correção Necessária**: - A cláusula `OR` na consulta pode causar
     * resultados inesperados. Para evitar erros, uma abordagem mais segura
     * seria: ```sql SELECT id_civil_semestral AS 'N°', iduser AS 'R.E',
     * nome_prev AS Técnico, tipo_prev AS 'Preventiva', setor_prev AS Setor,
     * nome_equi_set AS Equipamento, cod_equi_set AS 'Código', tempo_dis AS
     * Distribuição FROM form_civil_semestral WHERE iduser = ? AND
     * (situacao_prev IS NULL OR situacao_prev = ?) ```
     *
     * Fluxo do Método: 1. Define a situação inicial das preventivas. 2. Prepara
     * e executa a consulta SQL. 3. Obtém os resultados e os exibe na tabela
     * `tblPreCivil`. 4. Aplica formatação à tabela para melhor visualização. 5.
     * Trata erros para evitar falhas na execução.
     */
    private void civ_sem() {
        situacao1 = " ";

        String sql = "select id_civil_semestral as 'N°',iduser as 'R.E', nome_prev as Técnico, tipo_prev as 'Preventiva', setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', tempo_dis as Distribuição from  form_civil_semestral where iduser = ? and situacao_prev is null or iduser = ? and situacao_prev = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, id);
            pst.setString(3, situacao1);
            rs = pst.executeQuery();
            tblPreCivil.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat2();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `civ_tri_quantida`. Este método consulta o banco de dados para
     * contar o número de preventivas civis trimestrais associadas ao usuário
     * logado (`iduser`) que ainda estão pendentes (`situacao_prev IS NULL`). O
     * resultado é exibido no campo `txtQuaCiv`, permitindo um rápido
     * acompanhamento da quantidade de tarefas abertas.
     *
     * Funcionalidades: - **Consulta SQL com Contagem de Registros**: - Usa
     * `COUNT(*)` para retornar o número total de preventivas civis trimestrais
     * pendentes. - Filtra por `iduser`, garantindo que apenas preventivas do
     * usuário logado sejam contabilizadas.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Obtém o resultado e atribui ao campo `txtQuaCiv`.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * Fluxo do Método: 1. Prepara e executa a consulta SQL segura. 2. Obtém a
     * contagem de preventivas civis trimestrais pendentes. 3. Exibe o resultado
     * no campo `txtQuaCiv`. 4. Trata erros para evitar problemas na execução.
     */
    private void civ_tri_quantida() {

        String sql = "select count(*) from form_civil_trimestral where iduser = ?  and situacao_prev is null ";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            rs = pst.executeQuery();
            if (rs.next()) {
                txtQuaCiv.setText(rs.getString(1));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `civ_sem_pes`. Este método permite a pesquisa de preventivas civis
     * semestrais no banco de dados com base no usuário logado (`iduser`) e no
     * critério de busca inserido pelo usuário (`txtPesqCiv`). Ele filtra as
     * preventivas por número de formulário (`id_civil_semestral`) ou setor
     * (`setor_prev`) e exibe os resultados na tabela `tblPreCivil`.
     *
     * Funcionalidades: - **Consulta SQL com Filtro de Pesquisa**: - Busca
     * registros na tabela `form_civil_semestral` onde `situacao_prev` é `null`
     * e `iduser` corresponde ao usuário logado. - Permite busca por
     * `id_civil_semestral` ou `setor_prev` usando a cláusula `LIKE` com `%`,
     * garantindo pesquisa parcial. - Retorna informações organizadas com alias
     * para melhor exibição na tabela: - `"N°"` → Número do formulário da
     * preventiva. - `"R.E"` → Registro do usuário responsável. - `"Técnico"` →
     * Nome do técnico responsável. - `"Preventiva"` → Tipo da manutenção
     * preventiva. - `"Setor"` → Setor onde foi realizada a preventiva. -
     * `"Equipamento"` → Nome do equipamento. - `"Código"` → Código de
     * identificação do equipamento. - `"Distribuição"` → Tempo de distribuição
     * da preventiva.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Aplica filtros de pesquisa a partir da entrada do usuário
     * (`txtPesqCiv.getText()`). - Atribui os resultados à tabela `tblPreCivil`
     * via `DbUtils.resultSetToTableModel(rs)`.
     *
     * - **Formatação da Tabela**: - Chama `tabelaFormat2()` para organizar
     * visualmente os dados exibidos na tabela.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * **Correção Necessária**: - A consulta contém um erro na cláusula
     * `WHERE`: está utilizando `id_civil_mensal` quando deveria ser
     * `id_civil_semestral`. - Além disso, a estrutura com `OR` pode gerar
     * resultados inesperados. Uma abordagem mais correta seria: ```sql SELECT
     * id_civil_semestral AS 'N°', iduser AS 'R.E', nome_prev AS Técnico,
     * tipo_prev AS 'Preventiva', setor_prev AS Setor, nome_equi_set AS
     * Equipamento, cod_equi_set AS 'Código', tempo_dis AS Distribuição FROM
     * form_civil_semestral WHERE iduser = ? AND situacao_prev IS NULL AND
     * (id_civil_semestral LIKE ? OR setor_prev LIKE ?) ```
     *
     * Fluxo do Método: 1. Obtém o critério de pesquisa inserido pelo usuário.
     * 2. Prepara e executa a consulta SQL segura. 3. Exibe os resultados
     * filtrados na tabela `tblPreCivil`. 4. Aplica formatação à tabela para
     * melhor visualização. 5. Trata erros para evitar problemas na execução.
     */
    private void civ_sem_pes() {

        String sql = "select id_civil_semestral as 'N°', iduser as 'R.E', nome_prev as Técnico, tipo_prev as 'Preventiva', setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', tempo_dis as Distribuição from  form_civil_semestral where iduser = ? and situacao_prev is null and id_civil_mensal like ? or iduser = ? and situacao_prev is null and setor_prev like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, txtPesqCiv.getText() + "%");
            pst.setString(3, id);
            pst.setString(4, txtPesqCiv.getText() + "%");
            rs = pst.executeQuery();
            tblPreCivil.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat2();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `civ_sem_quantida`. Este método consulta o banco de dados para
     * contar o número de preventivas civis semestrais associadas ao usuário
     * logado (`iduser`) que ainda estão pendentes (`situacao_prev IS NULL`). O
     * resultado é exibido no campo `txtQuaCiv`, permitindo um rápido
     * acompanhamento da quantidade de tarefas abertas.
     *
     * Funcionalidades: - **Consulta SQL com Contagem de Registros**: - Usa
     * `COUNT(*)` para retornar o número total de preventivas civis semestrais
     * pendentes. - Filtra por `iduser`, garantindo que apenas preventivas do
     * usuário logado sejam contabilizadas.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Obtém o resultado e atribui ao campo `txtQuaCiv`.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * Fluxo do Método: 1. Prepara e executa a consulta SQL segura. 2. Obtém a
     * contagem de preventivas civis semestrais pendentes. 3. Exibe o resultado
     * no campo `txtQuaCiv`. 4. Trata erros para evitar problemas na execução.
     */
    private void civ_sem_quantida() {

        String sql = "select count(*) from form_civil_semestral where iduser = ?  and situacao_prev is null ";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            rs = pst.executeQuery();
            if (rs.next()) {
                txtQuaCiv.setText(rs.getString(1));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `setar_civ`. Este método captura os dados da preventiva civil
     * selecionada na tabela `tblPreCivil` e os exibe nos campos apropriados da
     * interface. Ele também ativa os botões de alteração e início da
     * preventiva, garantindo que o usuário possa interagir com os dados
     * selecionados.
     *
     * Funcionalidades: - **Obtenção de Dados da Preventiva Selecionada**: -
     * Obtém o índice da linha selecionada com `getSelectedRow()`. - Define
     * `txtIdPrevAlt`, `txtIdPrevIni` e `idprev` com o ID da preventiva. -
     * Define `txtTipPreAlt`, `txtTipPreIni` e `tipo` com o tipo de preventiva.
     *
     * - **Ativação de Botões**: - Chama `botão()` para realizar ações
     * adicionais. - Habilita `btnAltPrev` e `btnIniPrev`, permitindo que o
     * usuário altere ou inicie a preventiva.
     *
     * - **Atualização do Campo de Tipo de Preventiva**: - Define `txtTipPreIni`
     * com a combinação do tipo `"Civil"` e a variável `si`.
     *
     * - **Limpeza de Campos de Pesquisa**: - Define `txtPesqEle`, `txtPesqHid`,
     * `txtPesqCiv` e `txtPesqRef` como `null`, garantindo que os campos de
     * pesquisa sejam resetados.
     *
     * Fluxo do Método: 1. Obtém a linha selecionada na tabela. 2. Recupera e
     * exibe os dados da preventiva nos campos correspondentes. 3. Ativa os
     * botões de interação. 4. Atualiza o campo de tipo de preventiva. 5. Limpa
     * os campos de pesquisa.
     */
    private void setar_civ() {
        set = "Civil";
        int setar = tblPreCivil.getSelectedRow();
        txtIdPrevAlt.setText(tblPreCivil.getModel().getValueAt(setar, 0).toString());
        idprev = (tblPreCivil.getModel().getValueAt(setar, 0).toString());
        txtIdPrevIni.setText(tblPreCivil.getModel().getValueAt(setar, 0).toString());
        txtTipPreAlt.setText(tblPreCivil.getModel().getValueAt(setar, 3).toString());
        tipo = (tblPreCivil.getModel().getValueAt(setar, 3).toString());
        txtTipPreIni.setText(tblPreCivil.getModel().getValueAt(setar, 3).toString());
        botão();
        btnAltPrev.setEnabled(true);
        btnIniPrev.setEnabled(true);

        txtTipPreIni.setText(set + " - " + si);
        txtPesqEle.setText(null);
        txtPesqHid.setText(null);
        txtPesqCiv.setText(null);
        txtPesqRef.setText(null);
    }

    /**
     * Método `ref_men`. Este método consulta o banco de dados para obter
     * preventivas de refrigeração mensais associadas a um usuário específico.
     * Ele recupera informações detalhadas sobre as preventivas e exibe os
     * resultados na tabela `tblPreRef`, formatando a exibição para melhor
     * legibilidade.
     *
     * Funcionalidades: - **Definição da Situação Inicial**: - A variável
     * `situacao1` é inicializada como `" "`, representando uma possível
     * situação de preventiva.
     *
     * - **Consulta SQL**: - Busca preventivas mensais na tabela
     * `form_refrigeracao_mensal` com base no `iduser`. - Retorna registros onde
     * `situacao_prev` está `null` ou igual a `" "`. - Formata os nomes das
     * colunas para exibição: - `"N°"` → Identificador da preventiva. - `"R.E"`
     * → ID do usuário responsável. - `"Técnico"` → Nome do técnico responsável.
     * - `"Preventiva"` → Tipo da manutenção preventiva. - `"Setor"` → Setor
     * onde a preventiva foi realizada. - `"Equipamento"` → Nome do equipamento.
     * - `"Código"` → Código de identificação do equipamento. - `"Distribuição"`
     * → Tempo de distribuição da preventiva.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Atribui o resultado à tabela `tblPreRef` via
     * `DbUtils.resultSetToTableModel(rs)`.
     *
     * - **Formatação da Tabela**: - Chama `tabelaFormat3()` para ajustar
     * visualmente a tabela.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * **Correção Necessária**: - A cláusula `OR` na consulta pode causar
     * resultados inesperados. Para evitar erros, uma abordagem mais segura
     * seria: ```sql SELECT id_refrigeracao_mensal AS 'N°', iduser AS 'R.E',
     * nome_prev AS Técnico, tipo_prev AS 'Preventiva', setor_prev AS Setor,
     * nome_equi_set AS Equipamento, cod_equi_set AS 'Código', tempo_dis AS
     * Distribuição FROM form_refrigeracao_mensal WHERE iduser = ? AND
     * (situacao_prev IS NULL OR situacao_prev = ?) ```
     *
     * Fluxo do Método: 1. Define a situação inicial das preventivas. 2. Prepara
     * e executa a consulta SQL. 3. Obtém os resultados e os exibe na tabela
     * `tblPreRef`. 4. Aplica formatação à tabela para melhor visualização. 5.
     * Trata erros para evitar falhas na execução.
     */
    private void ref_men() {
        situacao1 = " ";

        String sql = "select id_refrigeracao_mensal as 'N°', iduser as 'R.E', nome_prev as Técnico, tipo_prev as 'Preventiva', setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', tempo_dis as Distribuição from form_refrigeracao_mensal where iduser = ? and situacao_prev is null or iduser = ? and situacao_prev = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, id);
            pst.setString(3, situacao1);
            rs = pst.executeQuery();
            tblPreRef.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat3();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `ref_men_pes`. Este método permite a pesquisa de preventivas de
     * refrigeração mensais no banco de dados com base no usuário logado
     * (`iduser`) e no critério de busca inserido pelo usuário (`txtPesqRef`).
     * Ele filtra as preventivas por número de formulário
     * (`id_refrigeracao_mensal`) ou setor (`setor_prev`) e exibe os resultados
     * na tabela `tblPreRef`.
     *
     * Funcionalidades: - **Consulta SQL com Filtro de Pesquisa**: - Busca
     * registros na tabela `form_refrigeracao_mensal` onde `situacao_prev` é
     * `null` e `iduser` corresponde ao usuário logado. - Permite busca por
     * `id_refrigeracao_mensal` ou `setor_prev` usando a cláusula `LIKE` com
     * `%`, garantindo pesquisa parcial. - Retorna informações organizadas com
     * alias para melhor exibição na tabela: - `"N°"` → Número do formulário da
     * preventiva. - `"R.E"` → Registro do usuário responsável. - `"Técnico"` →
     * Nome do técnico responsável. - `"Preventiva"` → Tipo da manutenção
     * preventiva. - `"Setor"` → Setor onde foi realizada a preventiva. -
     * `"Equipamento"` → Nome do equipamento. - `"Código"` → Código de
     * identificação do equipamento. - `"Distribuição"` → Tempo de distribuição
     * da preventiva.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Aplica filtros de pesquisa a partir da entrada do usuário
     * (`txtPesqRef.getText()`). - Atribui os resultados à tabela `tblPreRef`
     * via `DbUtils.resultSetToTableModel(rs)`.
     *
     * - **Formatação da Tabela**: - Chama `tabelaFormat3()` para organizar
     * visualmente os dados exibidos na tabela.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * **Correção Necessária**: - A consulta contém um erro na cláusula
     * `WHERE`: a estrutura com `OR` pode gerar resultados inesperados. Uma
     * abordagem mais correta seria: ```sql SELECT id_refrigeracao_mensal AS
     * 'N°', iduser AS 'R.E', nome_prev AS Técnico, tipo_prev AS 'Preventiva',
     * setor_prev AS Setor, nome_equi_set AS Equipamento, cod_equi_set AS
     * 'Código', tempo_dis AS Distribuição FROM form_refrigeracao_mensal WHERE
     * iduser = ? AND situacao_prev IS NULL AND (id_refrigeracao_mensal LIKE ?
     * OR setor_prev LIKE ?) ```
     *
     * Fluxo do Método: 1. Obtém o critério de pesquisa inserido pelo usuário.
     * 2. Prepara e executa a consulta SQL segura. 3. Exibe os resultados
     * filtrados na tabela `tblPreRef`. 4. Aplica formatação à tabela para
     * melhor visualização. 5. Trata erros para evitar problemas na execução.
     */
    private void ref_men_pes() {

        String sql = "select id_refrigeracao_mensal as 'N°', iduser as 'R.E', nome_prev as Técnico, tipo_prev as 'Preventiva', setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', tempo_dis as Distribuição from  form_refrigeracao_mensal where iduser = ? and situacao_prev is null and id_refrigeracao_mensal like ? or iduser = ? and situacao_prev is null and setor_prev like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, txtPesqRef.getText() + "%");
            pst.setString(3, id);
            pst.setString(4, txtPesqRef.getText() + "%");
            rs = pst.executeQuery();
            tblPreRef.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat3();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `ref_men_quantida`. Este método consulta o banco de dados para
     * contar o número de preventivas de refrigeração mensais associadas ao
     * usuário logado (`iduser`) que ainda estão pendentes (`situacao_prev IS
     * NULL`). O resultado é exibido no campo `txtQuaRef`, permitindo um rápido
     * acompanhamento da quantidade de tarefas abertas.
     *
     * Funcionalidades: - **Consulta SQL com Contagem de Registros**: - Usa
     * `COUNT(*)` para retornar o número total de preventivas de refrigeração
     * mensais pendentes. - Filtra por `iduser`, garantindo que apenas
     * preventivas do usuário logado sejam contabilizadas.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Obtém o resultado e atribui ao campo `txtQuaRef`.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * Fluxo do Método: 1. Prepara e executa a consulta SQL segura. 2. Obtém a
     * contagem de preventivas de refrigeração mensais pendentes. 3. Exibe o
     * resultado no campo `txtQuaRef`. 4. Trata erros para evitar problemas na
     * execução.
     */
    private void ref_men_quantida() {

        String sql = "select count(*) from form_refrigeracao_mensal where iduser = ?  and situacao_prev is null ";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            rs = pst.executeQuery();
            if (rs.next()) {
                txtQuaRef.setText(rs.getString(1));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `ref_tri`. Este método consulta o banco de dados para obter
     * preventivas de refrigeração trimestrais associadas a um usuário
     * específico. Ele recupera informações detalhadas sobre as preventivas e
     * exibe os resultados na tabela `tblPreRef`, formatando a exibição para
     * melhor legibilidade.
     *
     * Funcionalidades: - **Definição da Situação Inicial**: - A variável
     * `situacao1` é inicializada como `" "`, representando uma possível
     * situação de preventiva.
     *
     * - **Consulta SQL**: - Busca preventivas trimestrais na tabela
     * `form_refrigeracao_trimestral` com base no `iduser`. - Retorna registros
     * onde `situacao_prev` está `null` ou igual a `" "`. - Formata os nomes das
     * colunas para exibição: - `"N°"` → Identificador da preventiva. - `"R.E"`
     * → ID do usuário responsável. - `"Técnico"` → Nome do técnico responsável.
     * - `"Preventiva"` → Tipo da manutenção preventiva. - `"Setor"` → Setor
     * onde a preventiva foi realizada. - `"Equipamento"` → Nome do equipamento.
     * - `"Código"` → Código de identificação do equipamento. - `"Distribuição"`
     * → Tempo de distribuição da preventiva.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Atribui o resultado à tabela `tblPreRef` via
     * `DbUtils.resultSetToTableModel(rs)`.
     *
     * - **Formatação da Tabela**: - Chama `tabelaFormat3()` para ajustar
     * visualmente a tabela.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * **Correção Necessária**: - A cláusula `OR` na consulta pode causar
     * resultados inesperados. Para evitar erros, uma abordagem mais segura
     * seria: ```sql SELECT id_refrigeracao_trimestral AS 'N°', iduser AS 'R.E',
     * nome_prev AS Técnico, tipo_prev AS 'Preventiva', setor_prev AS Setor,
     * nome_equi_set AS Equipamento, cod_equi_set AS 'Código', tempo_dis AS
     * Distribuição FROM form_refrigeracao_trimestral WHERE iduser = ? AND
     * (situacao_prev IS NULL OR situacao_prev = ?) ```
     *
     * Fluxo do Método: 1. Define a situação inicial das preventivas. 2. Prepara
     * e executa a consulta SQL. 3. Obtém os resultados e os exibe na tabela
     * `tblPreRef`. 4. Aplica formatação à tabela para melhor visualização. 5.
     * Trata erros para evitar falhas na execução.
     */
    private void ref_tri() {
        situacao1 = " ";

        String sql = "select id_refrigeracao_trimestral as 'N°', iduser as 'R.E', nome_prev as Técnico, tipo_prev as 'Preventiva', setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', tempo_dis as Distribuição from form_refrigeracao_trimestral where iduser = ? and situacao_prev is null or iduser = ? and situacao_prev = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, id);
            pst.setString(3, situacao1);
            rs = pst.executeQuery();
            tblPreRef.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat3();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `ref_tri_pes`. Este método permite a pesquisa de preventivas de
     * refrigeração trimestrais no banco de dados com base no usuário logado
     * (`iduser`) e no critério de busca inserido pelo usuário (`txtPesqRef`).
     * Ele filtra as preventivas por número de formulário
     * (`id_refrigeracao_trimestral`) ou setor (`setor_prev`) e exibe os
     * resultados na tabela `tblPreRef`.
     *
     * Funcionalidades: - **Consulta SQL com Filtro de Pesquisa**: - Busca
     * registros na tabela `form_refrigeracao_trimestral` onde `situacao_prev` é
     * `null` e `iduser` corresponde ao usuário logado. - Permite busca por
     * `id_refrigeracao_trimestral` ou `setor_prev` usando a cláusula `LIKE` com
     * `%`, garantindo pesquisa parcial. - Retorna informações organizadas com
     * alias para melhor exibição na tabela: - `"N°"` → Número do formulário da
     * preventiva. - `"R.E"` → Registro do usuário responsável. - `"Técnico"` →
     * Nome do técnico responsável. - `"Preventiva"` → Tipo da manutenção
     * preventiva. - `"Setor"` → Setor onde foi realizada a preventiva. -
     * `"Equipamento"` → Nome do equipamento. - `"Código"` → Código de
     * identificação do equipamento. - `"Distribuição"` → Tempo de distribuição
     * da preventiva.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Aplica filtros de pesquisa a partir da entrada do usuário
     * (`txtPesqRef.getText()`). - Atribui os resultados à tabela `tblPreRef`
     * via `DbUtils.resultSetToTableModel(rs)`.
     *
     * - **Formatação da Tabela**: - Chama `tabelaFormat3()` para organizar
     * visualmente os dados exibidos na tabela.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * **Correção Necessária**: - A consulta contém um erro na cláusula
     * `WHERE`: está utilizando `id_refrigeracao_mensal` quando deveria ser
     * `id_refrigeracao_trimestral`. - Além disso, a estrutura com `OR` pode
     * gerar resultados inesperados. Uma abordagem mais correta seria: ```sql
     * SELECT id_refrigeracao_trimestral AS 'N°', iduser AS 'R.E', nome_prev AS
     * Técnico, tipo_prev AS 'Preventiva', setor_prev AS Setor, nome_equi_set AS
     * Equipamento, cod_equi_set AS 'Código', tempo_dis AS Distribuição FROM
     * form_refrigeracao_trimestral WHERE iduser = ? AND situacao_prev IS NULL
     * AND (id_refrigeracao_trimestral LIKE ? OR setor_prev LIKE ?) ```
     *
     * Fluxo do Método: 1. Obtém o critério de pesquisa inserido pelo usuário.
     * 2. Prepara e executa a consulta SQL segura. 3. Exibe os resultados
     * filtrados na tabela `tblPreRef`. 4. Aplica formatação à tabela para
     * melhor visualização. 5. Trata erros para evitar problemas na execução.
     */
    private void ref_tri_pes() {

        String sql = "select id_refrigeracao_trimestral as 'N°', iduser as 'R.E', nome_prev as Técnico, tipo_prev as 'Preventiva', setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', tempo_dis as Distribuição from  form_refrigeracao_trimestral where iduser = ? and situacao_prev is null and id_refrigeracao_mensal like ? or iduser = ? and situacao_prev is null and setor_prev like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, txtPesqRef.getText() + "%");
            pst.setString(3, id);
            pst.setString(4, txtPesqRef.getText() + "%");
            rs = pst.executeQuery();
            tblPreRef.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat3();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `ref_tri_quantida`. Este método consulta o banco de dados para
     * contar o número de preventivas de refrigeração trimestrais associadas ao
     * usuário logado (`iduser`) que ainda estão pendentes (`situacao_prev IS
     * NULL`). O resultado é exibido no campo `txtQuaRef`, permitindo um rápido
     * acompanhamento da quantidade de tarefas abertas.
     *
     * Funcionalidades: - **Consulta SQL com Contagem de Registros**: - Usa
     * `COUNT(*)` para retornar o número total de preventivas de refrigeração
     * trimestrais pendentes. - Filtra por `iduser`, garantindo que apenas
     * preventivas do usuário logado sejam contabilizadas.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Obtém o resultado e atribui ao campo `txtQuaRef`.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * Fluxo do Método: 1. Prepara e executa a consulta SQL segura. 2. Obtém a
     * contagem de preventivas de refrigeração trimestrais pendentes. 3. Exibe o
     * resultado no campo `txtQuaRef`. 4. Trata erros para evitar problemas na
     * execução.
     */
    private void ref_tri_quantida() {

        String sql = "select count(*) from form_refrigeracao_trimestral where iduser = ?  and situacao_prev is null ";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            rs = pst.executeQuery();
            if (rs.next()) {
                txtQuaRef.setText(rs.getString(1));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `ref_sem`. Este método consulta o banco de dados para obter
     * preventivas de refrigeração semestrais associadas a um usuário
     * específico. Ele recupera informações detalhadas sobre as preventivas e
     * exibe os resultados na tabela `tblPreRef`, formatando a exibição para
     * melhor legibilidade.
     *
     * Funcionalidades: - **Definição da Situação Inicial**: - A variável
     * `situacao1` é inicializada como `" "`, representando uma possível
     * situação de preventiva.
     *
     * - **Consulta SQL**: - Busca preventivas semestrais na tabela
     * `form_refrigeracao_semestral` com base no `iduser`. - Retorna registros
     * onde `situacao_prev` está `null` ou igual a `" "`. - Formata os nomes das
     * colunas para exibição: - `"N°"` → Identificador da preventiva. - `"R.E"`
     * → ID do usuário responsável. - `"Técnico"` → Nome do técnico responsável.
     * - `"Preventiva"` → Tipo da manutenção preventiva. - `"Setor"` → Setor
     * onde a preventiva foi realizada. - `"Equipamento"` → Nome do equipamento.
     * - `"Código"` → Código de identificação do equipamento. - `"Distribuição"`
     * → Tempo de distribuição da preventiva.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Atribui o resultado à tabela `tblPreRef` via
     * `DbUtils.resultSetToTableModel(rs)`.
     *
     * - **Formatação da Tabela**: - Chama `tabelaFormat3()` para ajustar
     * visualmente a tabela.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * **Correção Necessária**: - A cláusula `OR` na consulta pode causar
     * resultados inesperados. Para evitar erros, uma abordagem mais segura
     * seria: ```sql SELECT id_refrigeracao_semestral AS 'N°', iduser AS 'R.E',
     * nome_prev AS Técnico, tipo_prev AS 'Preventiva', setor_prev AS Setor,
     * nome_equi_set AS Equipamento, cod_equi_set AS 'Código', tempo_dis AS
     * Distribuição FROM form_refrigeracao_semestral WHERE iduser = ? AND
     * (situacao_prev IS NULL OR situacao_prev = ?) ```
     *
     * Fluxo do Método: 1. Define a situação inicial das preventivas. 2. Prepara
     * e executa a consulta SQL. 3. Obtém os resultados e os exibe na tabela
     * `tblPreRef`. 4. Aplica formatação à tabela para melhor visualização. 5.
     * Trata erros para evitar falhas na execução.
     */
    private void ref_sem() {
        situacao1 = " ";

        String sql = "select id_refrigeracao_semestral as 'N°', iduser as 'R.E', nome_prev as Técnico, tipo_prev as 'Preventiva', setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', tempo_dis as Distribuição from  form_refrigeracao_semestral where iduser = ? and situacao_prev is null or iduser = ? and situacao_prev = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, id);
            pst.setString(3, situacao1);
            rs = pst.executeQuery();
            tblPreRef.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat3();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `ref_sem_pes`. Este método permite a pesquisa de preventivas de
     * refrigeração semestrais no banco de dados com base no usuário logado
     * (`iduser`) e no critério de busca inserido pelo usuário (`txtPesqRef`).
     * Ele filtra as preventivas por número de formulário
     * (`id_refrigeracao_semestral`) ou setor (`setor_prev`) e exibe os
     * resultados na tabela `tblPreRef`.
     *
     * Funcionalidades: - **Consulta SQL com Filtro de Pesquisa**: - Busca
     * registros na tabela `form_refrigeracao_semestral` onde `situacao_prev` é
     * `null` e `iduser` corresponde ao usuário logado. - Permite busca por
     * `id_refrigeracao_semestral` ou `setor_prev` usando a cláusula `LIKE` com
     * `%`, garantindo pesquisa parcial. - Retorna informações organizadas com
     * alias para melhor exibição na tabela: - `"N°"` → Número do formulário da
     * preventiva. - `"R.E"` → Registro do usuário responsável. - `"Técnico"` →
     * Nome do técnico responsável. - `"Preventiva"` → Tipo da manutenção
     * preventiva. - `"Setor"` → Setor onde foi realizada a preventiva. -
     * `"Equipamento"` → Nome do equipamento. - `"Código"` → Código de
     * identificação do equipamento. - `"Distribuição"` → Tempo de distribuição
     * da preventiva.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Aplica filtros de pesquisa a partir da entrada do usuário
     * (`txtPesqRef.getText()`). - Atribui os resultados à tabela `tblPreRef`
     * via `DbUtils.resultSetToTableModel(rs)`.
     *
     * - **Formatação da Tabela**: - Chama `tabelaFormat3()` para organizar
     * visualmente os dados exibidos na tabela.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * **Correção Necessária**: - A consulta contém um erro na cláusula
     * `WHERE`: está utilizando `id_refrigeracao_mensal` quando deveria ser
     * `id_refrigeracao_semestral`. - Além disso, a estrutura com `OR` pode
     * gerar resultados inesperados. Uma abordagem mais correta seria: ```sql
     * SELECT id_refrigeracao_semestral AS 'N°', iduser AS 'R.E', nome_prev AS
     * Técnico, tipo_prev AS 'Preventiva', setor_prev AS Setor, nome_equi_set AS
     * Equipamento, cod_equi_set AS 'Código', tempo_dis AS Distribuição FROM
     * form_refrigeracao_semestral WHERE iduser = ? AND situacao_prev IS NULL
     * AND (id_refrigeracao_semestral LIKE ? OR setor_prev LIKE ?) ```
     *
     * Fluxo do Método: 1. Obtém o critério de pesquisa inserido pelo usuário.
     * 2. Prepara e executa a consulta SQL segura. 3. Exibe os resultados
     * filtrados na tabela `tblPreRef`. 4. Aplica formatação à tabela para
     * melhor visualização. 5. Trata erros para evitar problemas na execução.
     */
    private void ref_sem_pes() {

        String sql = "select id_refrigeracao_semestral as 'N°', iduser as 'R.E', nome_prev as Técnico, tipo_prev as 'Preventiva', setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', tempo_dis as Distribuição from  form_refrigeracao_semestral where iduser = ? and situacao_prev is null and id_refrigeracao_mensal like ? or iduser = ? and situacao_prev is null and setor_prev like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, txtPesqRef.getText() + "%");
            pst.setString(3, id);
            pst.setString(4, txtPesqRef.getText() + "%");
            rs = pst.executeQuery();
            tblPreRef.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat3();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `ref_sem_quantida`. Este método consulta o banco de dados para
     * contar o número de preventivas de refrigeração semestrais associadas ao
     * usuário logado (`iduser`) que ainda estão pendentes (`situacao_prev IS
     * NULL`). O resultado é exibido no campo `txtQuaRef`, permitindo um rápido
     * acompanhamento da quantidade de tarefas abertas.
     *
     * Funcionalidades: - **Consulta SQL com Contagem de Registros**: - Usa
     * `COUNT(*)` para retornar o número total de preventivas de refrigeração
     * semestrais pendentes. - Filtra por `iduser`, garantindo que apenas
     * preventivas do usuário logado sejam contabilizadas.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Obtém o resultado e atribui ao campo `txtQuaRef`.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * Fluxo do Método: 1. Prepara e executa a consulta SQL segura. 2. Obtém a
     * contagem de preventivas de refrigeração semestrais pendentes. 3. Exibe o
     * resultado no campo `txtQuaRef`. 4. Trata erros para evitar problemas na
     * execução.
     */
    private void ref_sem_quantida() {

        String sql = "select count(*) from form_refrigeracao_semestral where iduser = ?  and situacao_prev is null ";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            rs = pst.executeQuery();
            if (rs.next()) {
                txtQuaRef.setText(rs.getString(1));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `setar_ref`. Este método captura os dados da preventiva de
     * refrigeração selecionada na tabela `tblPreRef` e os exibe nos campos
     * apropriados da interface. Ele também ativa os botões de alteração e
     * início da preventiva, garantindo que o usuário possa interagir com os
     * dados selecionados.
     *
     * Funcionalidades: - **Obtenção de Dados da Preventiva Selecionada**: -
     * Obtém o índice da linha selecionada com `getSelectedRow()`. - Define
     * `txtIdPrevAlt`, `txtIdPrevIni` e `idprev` com o ID da preventiva. -
     * Define `txtTipPreAlt`, `txtTipPreIni` e `tipo` com o tipo de preventiva.
     *
     * - **Ativação de Botões**: - Chama `botão()` para realizar ações
     * adicionais. - Habilita `btnAltPrev` e `btnIniPrev`, permitindo que o
     * usuário altere ou inicie a preventiva.
     *
     * - **Atualização do Campo de Tipo de Preventiva**: - Define `txtTipPreIni`
     * com a combinação do tipo `"Refrigeração"` e a variável `si`.
     *
     * - **Limpeza de Campos de Pesquisa**: - Define `txtPesqEle`, `txtPesqHid`,
     * `txtPesqCiv` e `txtPesqRef` como `null`, garantindo que os campos de
     * pesquisa sejam resetados.
     *
     * Fluxo do Método: 1. Obtém a linha selecionada na tabela. 2. Recupera e
     * exibe os dados da preventiva nos campos correspondentes. 3. Ativa os
     * botões de interação. 4. Atualiza o campo de tipo de preventiva. 5. Limpa
     * os campos de pesquisa.
     */
    private void setar_ref() {
        set = "Refrigeração";
        int setar = tblPreRef.getSelectedRow();
        txtIdPrevAlt.setText(tblPreRef.getModel().getValueAt(setar, 0).toString());
        idprev = (tblPreRef.getModel().getValueAt(setar, 0).toString());
        txtIdPrevIni.setText(tblPreRef.getModel().getValueAt(setar, 0).toString());
        txtTipPreAlt.setText(tblPreRef.getModel().getValueAt(setar, 3).toString());
        tipo = (tblPreRef.getModel().getValueAt(setar, 3).toString());
        txtTipPreIni.setText(tblPreRef.getModel().getValueAt(setar, 3).toString());
        botão();
        btnAltPrev.setEnabled(true);
        btnIniPrev.setEnabled(true);

        txtTipPreIni.setText(set + " - " + si);
        txtPesqEle.setText(null);
        txtPesqHid.setText(null);
        txtPesqCiv.setText(null);
        txtPesqRef.setText(null);
    }

    /**
     * Método `select_preventiva`. Este método verifica o tipo de preventiva
     * selecionada (`txtTipPreIni`) e, caso seja `"Eletrica - Mensal"` ou
     * `"Hidráulica - Mensal"`, exibe uma caixa de diálogo de confirmação para
     * iniciar a preventiva. Se o usuário confirmar, ele inicializa a tela
     * correspondente (`TelaEletricaMen` ou `TelaHidraulicaMen1`), define a data
     * de início e executa o procedimento de inicialização.
     *
     * Funcionalidades: - **Verificação do Tipo de Preventiva**: - Obtém o valor
     * de `txtTipPreIni` e verifica se corresponde a `"Eletrica - Mensal"` ou
     * `"Hidráulica - Mensal"`.
     *
     * - **Confirmação do Usuário**: - Exibe uma caixa de diálogo
     * (`JOptionPane.showConfirmDialog`) perguntando se deseja iniciar a
     * preventiva. - Se o usuário confirmar (`JOptionPane.YES_OPTION`),
     * prossegue com a inicialização.
     *
     * - **Registro da Data e Hora**: - Obtém a data e hora atual usando
     * `ZonedDateTime.now()`. - Formata a data e hora para um estilo médio
     * (`FormatStyle.MEDIUM`).
     *
     * - **Inicialização da Tela de Preventiva**: - Cria uma instância da tela
     * correspondente (`TelaEletricaMen` ou `TelaHidraulicaMen1`). - Torna a
     * tela visível (`setVisible(true)`). - Adiciona a tela ao componente pai
     * (`getParent().add(prev_men)`). - Chama o método de seleção da preventiva
     * (`select_eletr_mensal()` ou `select_hidraulica_mensal()`). - Define a
     * data de início (`TelaEletricaMen.txtDatIni.setText(format)` ou
     * `TelaHidraulicaMen1.txtDatIni1.setText(format)`). - Executa o método
     * `prev_ini()` para iniciar a preventiva.
     *
     * - **Encerramento da Janela Atual**: - Chama `dispose()` para fechar a
     * janela atual após a inicialização da preventiva.
     *
     * Fluxo do Método: 1. Obtém o tipo de preventiva selecionada. 2. Exibe uma
     * caixa de diálogo para confirmação do usuário. 3. Se confirmado, registra
     * a data e hora atual. 4. Inicializa a tela de preventiva correspondente.
     * 5. Fecha a janela atual após a inicialização.
     */
    private void select_preventiva() {

        String preventiva;
        preventiva = txtTipPreIni.getText();

        if (preventiva.equals("Eletrica - Mensal")) {
            int confirma = JOptionPane.showConfirmDialog(null, "Deseja Iniciar a Preventiva?", "Atenção", JOptionPane.YES_NO_OPTION);
            if (confirma == JOptionPane.YES_OPTION) {

                DateTimeFormatter data_hora = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
                ZonedDateTime zdtNow = ZonedDateTime.now();
                String format = data_hora.format(zdtNow);

                id_prev = txtIdPrevIni.getText();
                TelaEletricaMen prev_men = new TelaEletricaMen();
                prev_men.setVisible(true);
                getParent().add(prev_men);
                prev_men.select_eletr_mensal();
                TelaEletricaMen.txtDatIni.setText(format);
                prev_men.prev_ini();

                try {
                    prev_men.setMaximum(true);
                } catch (PropertyVetoException e) {
                    JOptionPane.showMessageDialog(null, e);
                }

                dispose();
            }
        }
        if (preventiva.equals("Hidráulica - Mensal")) {
            int confirma = JOptionPane.showConfirmDialog(null, "Deseja Iniciar a Preventiva?", "Atenção", JOptionPane.YES_NO_OPTION);
            if (confirma == JOptionPane.YES_OPTION) {

                DateTimeFormatter data_hora = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
                ZonedDateTime zdtNow = ZonedDateTime.now();
                String format = data_hora.format(zdtNow);

                id_prev1 = txtIdPrevIni.getText();
                TelaHidraulicaMen1 prev_men = new TelaHidraulicaMen1();
                prev_men.setVisible(true);
                getParent().add(prev_men);
                prev_men.select_hidraulica_mensal();
                TelaHidraulicaMen1.txtDatIni1.setText(format);
                prev_men.prev_ini();

                try {
                    prev_men.setMaximum(true);
                } catch (PropertyVetoException e) {
                    JOptionPane.showMessageDialog(null, e);
                }

                dispose();
            }
        }
        if (preventiva.equals("Civil - Mensal")) {
            int confirma = JOptionPane.showConfirmDialog(null, "Deseja Iniciar a Preventiva?", "Atenção", JOptionPane.YES_NO_OPTION);
            if (confirma == JOptionPane.YES_OPTION) {

                DateTimeFormatter data_hora = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
                ZonedDateTime zdtNow = ZonedDateTime.now();
                String format = data_hora.format(zdtNow);

                id_prev2 = txtIdPrevIni.getText();
                TelaCivilMen1 prev_men = new TelaCivilMen1();
                prev_men.setVisible(true);
                getParent().add(prev_men);
                prev_men.select_civil_mensal();
                TelaCivilMen1.txtDatIni2.setText(format);
                prev_men.prev_ini();

                try {
                    prev_men.setMaximum(true);
                } catch (PropertyVetoException e) {
                    JOptionPane.showMessageDialog(null, e);
                }

                dispose();
            }

        }
        if (preventiva.equals("Refrigeração - Mensal")) {
            int confirma = JOptionPane.showConfirmDialog(null, "Deseja Iniciar a Preventiva?", "Atenção", JOptionPane.YES_NO_OPTION);
            if (confirma == JOptionPane.YES_OPTION) {

                DateTimeFormatter data_hora = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
                ZonedDateTime zdtNow = ZonedDateTime.now();
                String format = data_hora.format(zdtNow);

                id_prev3 = txtIdPrevIni.getText();
                TelaRefrigeracaoMen1 prev_men = new TelaRefrigeracaoMen1();
                prev_men.setVisible(true);
                getParent().add(prev_men);
                prev_men.select_refrigeracao_mensal();
                TelaRefrigeracaoMen1.txtDatIni3.setText(format);
                prev_men.prev_ini();

                try {
                    prev_men.setMaximum(true);
                } catch (PropertyVetoException e) {
                    JOptionPane.showMessageDialog(null, e);
                }

                dispose();
            }
        }
        if (preventiva.equals("Eletrica - Trimestral")) {
            int confirma = JOptionPane.showConfirmDialog(null, "Deseja Iniciar a Preventiva?", "Atenção", JOptionPane.YES_NO_OPTION);
            if (confirma == JOptionPane.YES_OPTION) {

                DateTimeFormatter data_hora = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
                ZonedDateTime zdtNow = ZonedDateTime.now();
                String format = data_hora.format(zdtNow);

                id_prev4 = txtIdPrevIni.getText();
                TelaEletricaTrimestral prev_men = new TelaEletricaTrimestral();
                prev_men.setVisible(true);
                getParent().add(prev_men);
                prev_men.select_eletr_trim();
                TelaEletricaTrimestral.txtDatIni4.setText(format);
                prev_men.prev_ini();

                try {
                    prev_men.setMaximum(true);
                } catch (PropertyVetoException e) {
                    JOptionPane.showMessageDialog(null, e);
                }

                dispose();
            }
        }
        if (preventiva.equals("Hidráulica - Trimestral")) {
            int confirma = JOptionPane.showConfirmDialog(null, "Deseja Iniciar a Preventiva?", "Atenção", JOptionPane.YES_NO_OPTION);
            if (confirma == JOptionPane.YES_OPTION) {

                DateTimeFormatter data_hora = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
                ZonedDateTime zdtNow = ZonedDateTime.now();
                String format = data_hora.format(zdtNow);

                id_prev5 = txtIdPrevIni.getText();
                TelaHidraulicaTrimestral prev_men = new TelaHidraulicaTrimestral();
                prev_men.setVisible(true);
                getParent().add(prev_men);
                prev_men.select_hidra_trim();
                TelaHidraulicaTrimestral.txtDatIni5.setText(format);
                prev_men.prev_ini();

                try {
                    prev_men.setMaximum(true);
                } catch (PropertyVetoException e) {
                    JOptionPane.showMessageDialog(null, e);
                }

                dispose();
            }
        }
        if (preventiva.equals("Civil - Trimestral")) {
            int confirma = JOptionPane.showConfirmDialog(null, "Deseja Iniciar a Preventiva?", "Atenção", JOptionPane.YES_NO_OPTION);
            if (confirma == JOptionPane.YES_OPTION) {

                DateTimeFormatter data_hora = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
                ZonedDateTime zdtNow = ZonedDateTime.now();
                String format = data_hora.format(zdtNow);

                id_prev6 = txtIdPrevIni.getText();
                TelaCivilTrimestral prev_men = new TelaCivilTrimestral();
                prev_men.setVisible(true);
                getParent().add(prev_men);
                prev_men.select_civ_tri();
                TelaCivilTrimestral.txtDatIni6.setText(format);
                prev_men.prev_ini();

                try {
                    prev_men.setMaximum(true);
                } catch (PropertyVetoException e) {
                    JOptionPane.showMessageDialog(null, e);
                }

                dispose();
            }

        }
        if (preventiva.equals("Refrigeração - Trimestral")) {
            int confirma = JOptionPane.showConfirmDialog(null, "Deseja Iniciar a Preventiva?", "Atenção", JOptionPane.YES_NO_OPTION);
            if (confirma == JOptionPane.YES_OPTION) {

                DateTimeFormatter data_hora = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
                ZonedDateTime zdtNow = ZonedDateTime.now();
                String format = data_hora.format(zdtNow);

                id_prev7 = txtIdPrevIni.getText();
                TelaRefrigeracaoTrimestral prev_men = new TelaRefrigeracaoTrimestral();
                prev_men.setVisible(true);
                getParent().add(prev_men);
                prev_men.select_refri_trimestral();
                TelaRefrigeracaoTrimestral.txtDatIni7.setText(format);
                prev_men.prev_ini();

                try {
                    prev_men.setMaximum(true);
                } catch (PropertyVetoException e) {
                    JOptionPane.showMessageDialog(null, e);
                }

                dispose();
            }
        }
        if (preventiva.equals("Eletrica - Semestral")) {
            int confirma = JOptionPane.showConfirmDialog(null, "Deseja Iniciar a Preventiva?", "Atenção", JOptionPane.YES_NO_OPTION);
            if (confirma == JOptionPane.YES_OPTION) {

                DateTimeFormatter data_hora = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
                ZonedDateTime zdtNow = ZonedDateTime.now();
                String format = data_hora.format(zdtNow);

                id_prev8 = txtIdPrevIni.getText();
                TelaEletricaSem prev_men = new TelaEletricaSem();
                prev_men.setVisible(true);
                getParent().add(prev_men);
                prev_men.select_eletr_sem();
                TelaEletricaSem.txtDatIni8.setText(format);
                prev_men.prev_ini();

                try {
                    prev_men.setMaximum(true);
                } catch (PropertyVetoException e) {
                    JOptionPane.showMessageDialog(null, e);
                }

                dispose();
            }
        }
        if (preventiva.equals("Hidráulica - Semestral")) {
            int confirma = JOptionPane.showConfirmDialog(null, "Deseja Iniciar a Preventiva?", "Atenção", JOptionPane.YES_NO_OPTION);
            if (confirma == JOptionPane.YES_OPTION) {

                DateTimeFormatter data_hora = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
                ZonedDateTime zdtNow = ZonedDateTime.now();
                String format = data_hora.format(zdtNow);

                id_prev10 = txtIdPrevIni.getText();
                TelaHidraulicaSem prev_men = new TelaHidraulicaSem();
                prev_men.setVisible(true);
                getParent().add(prev_men);
                prev_men.select_hidra_sem();
                TelaHidraulicaSem.txtDatIni10.setText(format);
                prev_men.prev_ini();

                try {
                    prev_men.setMaximum(true);
                } catch (PropertyVetoException e) {
                    JOptionPane.showMessageDialog(null, e);
                }

                dispose();
            }
        }
        if (preventiva.equals("Civil - Semestral")) {
            int confirma = JOptionPane.showConfirmDialog(null, "Deseja Iniciar a Preventiva?", "Atenção", JOptionPane.YES_NO_OPTION);
            if (confirma == JOptionPane.YES_OPTION) {

                DateTimeFormatter data_hora = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
                ZonedDateTime zdtNow = ZonedDateTime.now();
                String format = data_hora.format(zdtNow);

                id_prev9 = txtIdPrevIni.getText();
                TelaCivilSem prev_men = new TelaCivilSem();
                prev_men.setVisible(true);
                getParent().add(prev_men);
                prev_men.select_civil_sem();
                TelaCivilSem.txtDatIni9.setText(format);
                prev_men.prev_ini();

                try {
                    prev_men.setMaximum(true);
                } catch (PropertyVetoException e) {
                    JOptionPane.showMessageDialog(null, e);
                }

                dispose();
            }

        }
        if (preventiva.equals("Refrigeração - Semestral")) {
            int confirma = JOptionPane.showConfirmDialog(null, "Deseja Iniciar a Preventiva?", "Atenção", JOptionPane.YES_NO_OPTION);
            if (confirma == JOptionPane.YES_OPTION) {

                DateTimeFormatter data_hora = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
                ZonedDateTime zdtNow = ZonedDateTime.now();
                String format = data_hora.format(zdtNow);

                id_prev11 = txtIdPrevIni.getText();
                TelaRefrigeracaoSem prev_men = new TelaRefrigeracaoSem();
                prev_men.setVisible(true);
                getParent().add(prev_men);
                prev_men.select_ref_sem();
                TelaRefrigeracaoSem.txtDatIni11.setText(format);
                prev_men.prev_ini();

                try {
                    prev_men.setMaximum(true);
                } catch (PropertyVetoException e) {
                    JOptionPane.showMessageDialog(null, e);
                }

                dispose();
            }
        }

    }

    /**
     * Método `ele_men_ini`. Este método consulta o banco de dados para obter
     * preventivas elétricas mensais associadas ao usuário logado (`iduser`) que
     * estão na situação `"Aberta"`. Os resultados são exibidos na tabela
     * `tblPreEle`, permitindo um acompanhamento visual das tarefas.
     *
     * Funcionalidades: - **Definição da Situação da Preventiva**: - A variável
     * `situacao` é inicializada como `"Aberta"`, indicando que apenas
     * preventivas abertas serão recuperadas.
     *
     * - **Consulta SQL**: - Busca preventivas elétricas mensais na tabela
     * `form_eletrica_mensal` com base no `iduser`. - Retorna registros onde
     * `situacao_prev` é `"Aberta"`. - Formata os nomes das colunas para
     * exibição: - `"N° Preventiva"` → Identificador da preventiva. - `"R.E"` →
     * ID do usuário responsável. - `"Técnico"` → Nome do técnico responsável. -
     * `"Preventiva"` → Tipo da manutenção preventiva. - `"Setor"` → Setor onde
     * a preventiva foi realizada. - `"Equipamento"` → Nome do equipamento. -
     * `"Código"` → Código de identificação do equipamento. - `"Situação"` →
     * Estado atual da preventiva. - `"Distribuição"` → Tempo de distribuição da
     * preventiva.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Atribui os resultados à tabela `tblPreEle` via
     * `DbUtils.resultSetToTableModel(rs)`.
     *
     * - **Formatação da Tabela**: - Chama `tabelaFormat()` para ajustar
     * visualmente a tabela.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * Fluxo do Método: 1. Define a situação da preventiva como `"Aberta"`. 2.
     * Prepara e executa a consulta SQL. 3. Obtém os resultados e os exibe na
     * tabela `tblPreEle`. 4. Aplica formatação à tabela para melhor
     * visualização. 5. Trata erros para evitar falhas na execução.
     */
    private void ele_men_ini() {
        situacao = "Aberta";

        String sql = "select id_form_ele_mensal as 'N° Preventiva', iduser as 'R.E', nome_prev as Técnico, tipo_prev as 'Preventiva', setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', situacao_prev as 'Situação', tempo_dis as Distribuição from  form_eletrica_mensal where iduser = ? and situacao_prev = ? ";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, situacao);
            rs = pst.executeQuery();
            tblPreEle.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `ele_men_pes1`. Este método permite a pesquisa de preventivas
     * elétricas mensais no banco de dados com base no usuário logado (`iduser`)
     * e no critério de busca inserido pelo usuário (`txtPesqEle`). Ele filtra
     * as preventivas por número de formulário (`id_form_ele_mensal`) e exibe os
     * resultados na tabela `tblPreEle`.
     *
     * Funcionalidades: - **Consulta SQL com Filtro de Pesquisa**: - Busca
     * registros na tabela `form_eletrica_mensal` onde `situacao_prev` é
     * `"Aberta"` e `iduser` corresponde ao usuário logado. - Permite busca por
     * `id_form_ele_mensal` usando a cláusula `LIKE` com `%`, garantindo
     * pesquisa parcial. - Retorna informações organizadas com alias para melhor
     * exibição na tabela: - `"N° Preventiva"` → Número do formulário da
     * preventiva. - `"R.E"` → Registro do usuário responsável. - `"Técnico"` →
     * Nome do técnico responsável. - `"Preventiva"` → Tipo da manutenção
     * preventiva. - `"Setor"` → Setor onde foi realizada a preventiva. -
     * `"Equipamento"` → Nome do equipamento. - `"Código"` → Código de
     * identificação do equipamento. - `"Situação"` → Estado atual da
     * preventiva. - `"Distribuição"` → Tempo de distribuição da preventiva.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Aplica filtros de pesquisa a partir da entrada do usuário
     * (`txtPesqEle.getText()`). - Atribui os resultados à tabela `tblPreEle`
     * via `DbUtils.resultSetToTableModel(rs)`.
     *
     * - **Formatação da Tabela**: - Chama `tabelaFormat()` para organizar
     * visualmente os dados exibidos na tabela.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * Fluxo do Método: 1. Obtém o critério de pesquisa inserido pelo usuário.
     * 2. Prepara e executa a consulta SQL segura. 3. Exibe os resultados
     * filtrados na tabela `tblPreEle`. 4. Aplica formatação à tabela para
     * melhor visualização. 5. Trata erros para evitar problemas na execução.
     */
    private void ele_men_pes1() {

        situacao = "Aberta";

        String sql = "select id_form_ele_mensal as 'N° Preventiva', iduser as 'R.E', nome_prev as Técnico, tipo_prev as 'Preventiva', setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', situacao_prev as 'Situação', tempo_dis as Distribuição from form_eletrica_mensal where iduser = ? and situacao_prev = ? and id_form_ele_mensal like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, situacao);
            pst.setString(3, txtPesqEle.getText() + "%");
            rs = pst.executeQuery();
            tblPreEle.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `ele_men_quantida1`. Este método consulta o banco de dados para
     * contar o número de preventivas elétricas mensais associadas ao usuário
     * logado (`iduser`) que ainda estão na situação `"Aberta"`. O resultado é
     * exibido no campo `txtQuaEle`, permitindo um rápido acompanhamento da
     * quantidade de tarefas abertas.
     *
     * Funcionalidades: - **Consulta SQL com Contagem de Registros**: - Usa
     * `COUNT(*)` para retornar o número total de preventivas elétricas mensais
     * pendentes. - Filtra por `iduser`, garantindo que apenas preventivas do
     * usuário logado sejam contabilizadas. - Considera apenas preventivas com
     * `situacao_prev = "Aberta"`.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Obtém o resultado e atribui ao campo `txtQuaEle`.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * Fluxo do Método: 1. Define a situação da preventiva como `"Aberta"`. 2.
     * Prepara e executa a consulta SQL segura. 3. Obtém a contagem de
     * preventivas elétricas mensais pendentes. 4. Exibe o resultado no campo
     * `txtQuaEle`. 5. Trata erros para evitar problemas na execução.
     */
    private void ele_men_quantida1() {
        situacao = "Aberta";

        String sql = "select count(*) from form_eletrica_mensal where iduser = ?  and situacao_prev = ? ";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, situacao);
            rs = pst.executeQuery();
            if (rs.next()) {
                txtQuaEle.setText(rs.getString(1));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `ele_tri_ini`. Este método consulta o banco de dados para obter
     * preventivas elétricas trimestrais associadas ao usuário logado (`iduser`)
     * que estão na situação `"Aberta"`. Os resultados são exibidos na tabela
     * `tblPreEle`, permitindo um acompanhamento visual das tarefas.
     *
     * Funcionalidades: - **Definição da Situação da Preventiva**: - A variável
     * `situacao` é inicializada como `"Aberta"`, indicando que apenas
     * preventivas abertas serão recuperadas.
     *
     * - **Consulta SQL**: - Busca preventivas elétricas trimestrais na tabela
     * `form_eletrica_trimestral` com base no `iduser`. - Retorna registros onde
     * `situacao_prev` é `"Aberta"`. - Formata os nomes das colunas para
     * exibição: - `"N°"` → Identificador da preventiva. - `"R.E"` → ID do
     * usuário responsável. - `"Técnico"` → Nome do técnico responsável. -
     * `"Preventiva"` → Tipo da manutenção preventiva. - `"Setor"` → Setor onde
     * a preventiva foi realizada. - `"Equipamento"` → Nome do equipamento. -
     * `"Código"` → Código de identificação do equipamento. - `"Situação"` →
     * Estado atual da preventiva. - `"Distribuição"` → Tempo de distribuição da
     * preventiva.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Atribui os resultados à tabela `tblPreEle` via
     * `DbUtils.resultSetToTableModel(rs)`.
     *
     * - **Formatação da Tabela**: - Chama `tabelaFormat()` para ajustar
     * visualmente a tabela.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * Fluxo do Método: 1. Define a situação da preventiva como `"Aberta"`. 2.
     * Prepara e executa a consulta SQL. 3. Obtém os resultados e os exibe na
     * tabela `tblPreEle`. 4. Aplica formatação à tabela para melhor
     * visualização. 5. Trata erros para evitar falhas na execução.
     */
    private void ele_tri_ini() {
        situacao = "Aberta";

        String sql = "select id_form_ele_trimestral as 'N°', iduser as 'R.E', nome_prev as Técnico, tipo_prev as 'Preventiva', setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', situacao_prev as 'Situação', tempo_dis as Distribuição from  form_eletrica_trimestral where iduser = ? and situacao_prev = ? ";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, situacao);
            rs = pst.executeQuery();
            tblPreEle.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `ele_tri_pes1`. Este método permite a pesquisa de preventivas
     * elétricas trimestrais no banco de dados com base no usuário logado
     * (`iduser`) e no critério de busca inserido pelo usuário (`txtPesqEle`).
     * Ele filtra as preventivas por número de formulário
     * (`id_form_ele_trimestral`) e exibe os resultados na tabela `tblPreEle`.
     *
     * Funcionalidades: - **Consulta SQL com Filtro de Pesquisa**: - Busca
     * registros na tabela `form_eletrica_trimestral` onde `situacao_prev` é
     * `"Aberta"` e `iduser` corresponde ao usuário logado. - Permite busca por
     * `id_form_ele_trimestral` usando a cláusula `LIKE` com `%`, garantindo
     * pesquisa parcial. - Retorna informações organizadas com alias para melhor
     * exibição na tabela: - `"N°"` → Número do formulário da preventiva. -
     * `"R.E"` → Registro do usuário responsável. - `"Técnico"` → Nome do
     * técnico responsável. - `"Preventiva"` → Tipo da manutenção preventiva. -
     * `"Setor"` → Setor onde foi realizada a preventiva. - `"Equipamento"` →
     * Nome do equipamento. - `"Código"` → Código de identificação do
     * equipamento. - `"Situação"` → Estado atual da preventiva. -
     * `"Distribuição"` → Tempo de distribuição da preventiva.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Aplica filtros de pesquisa a partir da entrada do usuário
     * (`txtPesqEle.getText()`). - Atribui os resultados à tabela `tblPreEle`
     * via `DbUtils.resultSetToTableModel(rs)`.
     *
     * - **Formatação da Tabela**: - Chama `tabelaFormat()` para organizar
     * visualmente os dados exibidos na tabela.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * Fluxo do Método: 1. Obtém o critério de pesquisa inserido pelo usuário.
     * 2. Prepara e executa a consulta SQL segura. 3. Exibe os resultados
     * filtrados na tabela `tblPreEle`. 4. Aplica formatação à tabela para
     * melhor visualização. 5. Trata erros para evitar problemas na execução.
     */
    private void ele_tri_pes1() {
        situacao = "Aberta";

        String sql = "select id_form_ele_trimestral as 'N°', iduser as 'R.E', nome_prev as Técnico, tipo_prev as 'Preventiva', setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', situacao_prev as 'Situação', tempo_dis as Distribuição from form_eletrica_trimestral where iduser = ? and situacao_prev = ? and id_form_ele_trimestral like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, situacao);
            pst.setString(3, txtPesqEle.getText() + "%");
            rs = pst.executeQuery();
            tblPreEle.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `ele_tri_quantida1`. Este método consulta o banco de dados para
     * contar o número de preventivas elétricas trimestrais associadas ao
     * usuário logado (`iduser`) que ainda estão na situação `"Aberta"`. O
     * resultado é exibido no campo `txtQuaEle`, permitindo um rápido
     * acompanhamento da quantidade de tarefas abertas.
     *
     * Funcionalidades: - **Consulta SQL com Contagem de Registros**: - Usa
     * `COUNT(*)` para retornar o número total de preventivas elétricas
     * trimestrais pendentes. - Filtra por `iduser`, garantindo que apenas
     * preventivas do usuário logado sejam contabilizadas. - Considera apenas
     * preventivas com `situacao_prev = "Aberta"`.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Obtém o resultado e atribui ao campo `txtQuaEle`.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * Fluxo do Método: 1. Define a situação da preventiva como `"Aberta"`. 2.
     * Prepara e executa a consulta SQL segura. 3. Obtém a contagem de
     * preventivas elétricas trimestrais pendentes. 4. Exibe o resultado no
     * campo `txtQuaEle`. 5. Trata erros para evitar problemas na execução.
     */
    private void ele_tri_quantida1() {
        situacao = "Aberta";

        String sql = "select count(*) from form_eletrica_trimestral where iduser = ?  and situacao_prev = ? ";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, situacao);
            rs = pst.executeQuery();
            if (rs.next()) {
                txtQuaEle.setText(rs.getString(1));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `ele_sem_ini`. Este método consulta o banco de dados para obter
     * preventivas elétricas semestrais associadas ao usuário logado (`iduser`)
     * que estão na situação `"Aberta"`. Os resultados são exibidos na tabela
     * `tblPreEle`, permitindo um acompanhamento visual das tarefas.
     *
     * Funcionalidades: - **Definição da Situação da Preventiva**: - A variável
     * `situacao` é inicializada como `"Aberta"`, indicando que apenas
     * preventivas abertas serão recuperadas.
     *
     * - **Consulta SQL**: - Busca preventivas elétricas semestrais na tabela
     * `form_eletrica_semestral` com base no `iduser`. - Retorna registros onde
     * `situacao_prev` é `"Aberta"`. - Formata os nomes das colunas para
     * exibição: - `"N°"` → Identificador da preventiva. - `"R.E"` → ID do
     * usuário responsável. - `"Técnico"` → Nome do técnico responsável. -
     * `"Preventiva"` → Tipo da manutenção preventiva. - `"Setor"` → Setor onde
     * a preventiva foi realizada. - `"Equipamento"` → Nome do equipamento. -
     * `"Código"` → Código de identificação do equipamento. - `"Situação"` →
     * Estado atual da preventiva. - `"Distribuição"` → Tempo de distribuição da
     * preventiva.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Atribui os resultados à tabela `tblPreEle` via
     * `DbUtils.resultSetToTableModel(rs)`.
     *
     * - **Formatação da Tabela**: - Chama `tabelaFormat()` para ajustar
     * visualmente a tabela.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * Fluxo do Método: 1. Define a situação da preventiva como `"Aberta"`. 2.
     * Prepara e executa a consulta SQL. 3. Obtém os resultados e os exibe na
     * tabela `tblPreEle`. 4. Aplica formatação à tabela para melhor
     * visualização. 5. Trata erros para evitar falhas na execução.
     */
    private void ele_sem_ini() {
        situacao = "Aberta";

        String sql = "select id_form_ele_semestral as 'N°', iduser as 'R.E', nome_prev as Técnico, tipo_prev as 'Preventiva', setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', situacao_prev as 'Situação', tempo_dis as Distribuição from  form_eletrica_semestral where iduser = ? and situacao_prev = ? ";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, situacao);
            rs = pst.executeQuery();
            tblPreEle.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `ele_sem_pes1`. Este método permite a pesquisa de preventivas
     * elétricas semestrais no banco de dados com base no usuário logado
     * (`iduser`) e no critério de busca inserido pelo usuário (`txtPesqEle`).
     * Ele filtra as preventivas por número de formulário
     * (`id_form_ele_semestral`) e exibe os resultados na tabela `tblPreEle`.
     *
     * Funcionalidades: - **Consulta SQL com Filtro de Pesquisa**: - Busca
     * registros na tabela `form_eletrica_semestral` onde `situacao_prev` é
     * `"Aberta"` e `iduser` corresponde ao usuário logado. - Permite busca por
     * `id_form_ele_semestral` usando a cláusula `LIKE` com `%`, garantindo
     * pesquisa parcial. - Retorna informações organizadas com alias para melhor
     * exibição na tabela: - `"N°"` → Número do formulário da preventiva. -
     * `"R.E"` → Registro do usuário responsável. - `"Técnico"` → Nome do
     * técnico responsável. - `"Preventiva"` → Tipo da manutenção preventiva. -
     * `"Setor"` → Setor onde foi realizada a preventiva. - `"Equipamento"` →
     * Nome do equipamento. - `"Código"` → Código de identificação do
     * equipamento. - `"Situação"` → Estado atual da preventiva. -
     * `"Distribuição"` → Tempo de distribuição da preventiva.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Aplica filtros de pesquisa a partir da entrada do usuário
     * (`txtPesqEle.getText()`). - Atribui os resultados à tabela `tblPreEle`
     * via `DbUtils.resultSetToTableModel(rs)`.
     *
     * - **Formatação da Tabela**: - Chama `tabelaFormat()` para organizar
     * visualmente os dados exibidos na tabela.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * Fluxo do Método: 1. Obtém o critério de pesquisa inserido pelo usuário.
     * 2. Prepara e executa a consulta SQL segura. 3. Exibe os resultados
     * filtrados na tabela `tblPreEle`. 4. Aplica formatação à tabela para
     * melhor visualização. 5. Trata erros para evitar problemas na execução.
     */
    private void ele_sem_pes1() {

        situacao = "Aberta";

        String sql = "select id_form_ele_semestral as 'N°', iduser as 'R.E', nome_prev as Técnico, tipo_prev as 'Preventiva', setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', situacao_prev as 'Situação', tempo_dis as Distribuição from form_eletrica_semestral where iduser = ? and situacao_prev = ? and id_form_ele_semestral like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, situacao);
            pst.setString(3, txtPesqEle.getText() + "%");
            rs = pst.executeQuery();
            tblPreEle.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `ele_sem_quantida1`. Este método consulta o banco de dados para
     * contar o número de preventivas elétricas semestrais associadas ao usuário
     * logado (`iduser`) que ainda estão na situação `"Aberta"`. O resultado é
     * exibido no campo `txtQuaEle`, permitindo um rápido acompanhamento da
     * quantidade de tarefas abertas.
     *
     * Funcionalidades: - **Consulta SQL com Contagem de Registros**: - Usa
     * `COUNT(*)` para retornar o número total de preventivas elétricas
     * semestrais pendentes. - Filtra por `iduser`, garantindo que apenas
     * preventivas do usuário logado sejam contabilizadas. - Considera apenas
     * preventivas com `situacao_prev = "Aberta"`.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Obtém o resultado e atribui ao campo `txtQuaEle`.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * Fluxo do Método: 1. Define a situação da preventiva como `"Aberta"`. 2.
     * Prepara e executa a consulta SQL segura. 3. Obtém a contagem de
     * preventivas elétricas semestrais pendentes. 4. Exibe o resultado no campo
     * `txtQuaEle`. 5. Trata erros para evitar problemas na execução.
     */
    private void ele_sem_quantida1() {
        situacao = "Aberta";

        String sql = "select count(*) from form_eletrica_semestral where iduser = ?  and situacao_prev = ? ";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, situacao);
            rs = pst.executeQuery();
            if (rs.next()) {
                txtQuaEle.setText(rs.getString(1));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `hid_men_ini`. Este método consulta o banco de dados para obter
     * preventivas hidráulicas mensais associadas ao usuário logado (`iduser`)
     * que estão na situação `"Aberta"`. Os resultados são exibidos na tabela
     * `tblPreHid`, permitindo um acompanhamento visual das tarefas.
     *
     * Funcionalidades: - **Definição da Situação da Preventiva**: - A variável
     * `situacao` é inicializada como `"Aberta"`, indicando que apenas
     * preventivas abertas serão recuperadas.
     *
     * - **Consulta SQL**: - Busca preventivas hidráulicas mensais na tabela
     * `form_hidraulica_mensal` com base no `iduser`. - Retorna registros onde
     * `situacao_prev` é `"Aberta"`. - Formata os nomes das colunas para
     * exibição: - `"N°"` → Identificador da preventiva. - `"R.E"` → ID do
     * usuário responsável. - `"Técnico"` → Nome do técnico responsável. -
     * `"Preventiva"` → Tipo da manutenção preventiva. - `"Setor"` → Setor onde
     * a preventiva foi realizada. - `"Equipamento"` → Nome do equipamento. -
     * `"Código"` → Código de identificação do equipamento. - `"Situação"` →
     * Estado atual da preventiva. - `"Distribuição"` → Tempo de distribuição da
     * preventiva.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Atribui os resultados à tabela `tblPreHid` via
     * `DbUtils.resultSetToTableModel(rs)`.
     *
     * - **Formatação da Tabela**: - Chama `tabelaFormat1()` para ajustar
     * visualmente a tabela.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * Fluxo do Método: 1. Define a situação da preventiva como `"Aberta"`. 2.
     * Prepara e executa a consulta SQL. 3. Obtém os resultados e os exibe na
     * tabela `tblPreHid`. 4. Aplica formatação à tabela para melhor
     * visualização. 5. Trata erros para evitar falhas na execução.
     */
    private void hid_men_ini() {
        situacao = "Aberta";

        String sql = "select id_hidraulica_mensal as 'N°', iduser as 'R.E', nome_prev as Técnico, tipo_prev as 'Preventiva', setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', situacao_prev as 'Situação', tempo_dis as Distribuição from  form_hidraulica_mensal where iduser = ? and situacao_prev = ? ";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, situacao);
            rs = pst.executeQuery();
            tblPreHid.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat1();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `hid_men_pes1`. Este método permite a pesquisa de preventivas
     * hidráulicas mensais no banco de dados com base no usuário logado
     * (`iduser`) e no critério de busca inserido pelo usuário (`txtPesqHid`).
     * Ele filtra as preventivas por número de formulário
     * (`id_hidraulica_mensal`) e exibe os resultados na tabela `tblPreHid`.
     *
     * Funcionalidades: - **Consulta SQL com Filtro de Pesquisa**: - Busca
     * registros na tabela `form_hidraulica_mensal` onde `situacao_prev` é
     * `"Aberta"` e `iduser` corresponde ao usuário logado. - Permite busca por
     * `id_hidraulica_mensal` usando a cláusula `LIKE` com `%`, garantindo
     * pesquisa parcial. - Retorna informações organizadas com alias para melhor
     * exibição na tabela: - `"N°"` → Número do formulário da preventiva. -
     * `"R.E"` → Registro do usuário responsável. - `"Técnico"` → Nome do
     * técnico responsável. - `"Preventiva"` → Tipo da manutenção preventiva. -
     * `"Setor"` → Setor onde foi realizada a preventiva. - `"Equipamento"` →
     * Nome do equipamento. - `"Código"` → Código de identificação do
     * equipamento. - `"Situação"` → Estado atual da preventiva. -
     * `"Distribuição"` → Tempo de distribuição da preventiva.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Aplica filtros de pesquisa a partir da entrada do usuário
     * (`txtPesqHid.getText()`). - Atribui os resultados à tabela `tblPreHid`
     * via `DbUtils.resultSetToTableModel(rs)`.
     *
     * - **Formatação da Tabela**: - Chama `tabelaFormat1()` para organizar
     * visualmente os dados exibidos na tabela.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * Fluxo do Método: 1. Obtém o critério de pesquisa inserido pelo usuário.
     * 2. Prepara e executa a consulta SQL segura. 3. Exibe os resultados
     * filtrados na tabela `tblPreHid`. 4. Aplica formatação à tabela para
     * melhor visualização. 5. Trata erros para evitar problemas na execução.
     */
    private void hid_men_pes1() {

        situacao = "Aberta";

        String sql = "select id_hidraulica_mensal as 'N°', iduser as 'R.E', nome_prev as Técnico, tipo_prev as 'Preventiva', setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', situacao_prev as 'Situação', tempo_dis as Distribuição from form_hidraulica_mensal where iduser = ? and situacao_prev = ? and id_hidraulica_mensal like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, situacao);
            pst.setString(3, txtPesqHid.getText() + "%");
            rs = pst.executeQuery();
            tblPreHid.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat1();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `hid_men_quantida1`. Este método consulta o banco de dados para
     * contar o número de preventivas hidráulicas mensais associadas ao usuário
     * logado (`iduser`) que ainda estão na situação `"Aberta"`. O resultado é
     * exibido no campo `txtQuaHid`, permitindo um rápido acompanhamento da
     * quantidade de tarefas abertas.
     *
     * Funcionalidades: - **Consulta SQL com Contagem de Registros**: - Usa
     * `COUNT(*)` para retornar o número total de preventivas hidráulicas
     * mensais pendentes. - Filtra por `iduser`, garantindo que apenas
     * preventivas do usuário logado sejam contabilizadas. - Considera apenas
     * preventivas com `situacao_prev = "Aberta"`.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Obtém o resultado e atribui ao campo `txtQuaHid`.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * Fluxo do Método: 1. Define a situação da preventiva como `"Aberta"`. 2.
     * Prepara e executa a consulta SQL segura. 3. Obtém a contagem de
     * preventivas hidráulicas mensais pendentes. 4. Exibe o resultado no campo
     * `txtQuaHid`. 5. Trata erros para evitar problemas na execução.
     */
    private void hid_men_quantida1() {
        situacao = "Aberta";

        String sql = "select count(*) from form_hidraulica_mensal where iduser = ?  and situacao_prev = ? ";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, situacao);
            rs = pst.executeQuery();
            if (rs.next()) {
                txtQuaHid.setText(rs.getString(1));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `hid_tri_ini`. Este método consulta o banco de dados para obter
     * preventivas hidráulicas trimestrais associadas ao usuário logado
     * (`iduser`) que estão na situação `"Aberta"`. Os resultados são exibidos
     * na tabela `tblPreHid`, permitindo um acompanhamento visual das tarefas.
     *
     * Funcionalidades: - **Definição da Situação da Preventiva**: - A variável
     * `situacao` é inicializada como `"Aberta"`, indicando que apenas
     * preventivas abertas serão recuperadas.
     *
     * - **Consulta SQL**: - Busca preventivas hidráulicas trimestrais na tabela
     * `form_hidraulica_trimestral` com base no `iduser`. - Retorna registros
     * onde `situacao_prev` é `"Aberta"`. - Formata os nomes das colunas para
     * exibição: - `"N°"` → Identificador da preventiva. - `"R.E"` → ID do
     * usuário responsável. - `"Técnico"` → Nome do técnico responsável. -
     * `"Preventiva"` → Tipo da manutenção preventiva. - `"Setor"` → Setor onde
     * a preventiva foi realizada. - `"Equipamento"` → Nome do equipamento. -
     * `"Código"` → Código de identificação do equipamento. - `"Situação"` →
     * Estado atual da preventiva. - `"Distribuição"` → Tempo de distribuição da
     * preventiva.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Atribui os resultados à tabela `tblPreHid` via
     * `DbUtils.resultSetToTableModel(rs)`.
     *
     * - **Formatação da Tabela**: - Chama `tabelaFormat1()` para ajustar
     * visualmente a tabela.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * Fluxo do Método: 1. Define a situação da preventiva como `"Aberta"`. 2.
     * Prepara e executa a consulta SQL. 3. Obtém os resultados e os exibe na
     * tabela `tblPreHid`. 4. Aplica formatação à tabela para melhor
     * visualização. 5. Trata erros para evitar falhas na execução.
     */
    private void hid_tri_ini() {
        situacao = "Aberta";

        String sql = "select id_hidraulica_trimestral as 'N°', iduser as 'R.E', nome_prev as Técnico, tipo_prev as 'Preventiva', setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', situacao_prev as 'Situação', tempo_dis as Distribuição from  form_hidraulica_trimestral where iduser = ? and situacao_prev = ? ";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, situacao);
            rs = pst.executeQuery();
            tblPreHid.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat1();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `hid_tri_pes1`. Este método permite a pesquisa de preventivas
     * hidráulicas trimestrais no banco de dados com base no usuário logado
     * (`iduser`) e no critério de busca inserido pelo usuário (`txtPesqHid`).
     * Ele filtra as preventivas por número de formulário
     * (`id_hidraulica_trimestral`) e exibe os resultados na tabela `tblPreHid`.
     *
     * Funcionalidades: - **Consulta SQL com Filtro de Pesquisa**: - Busca
     * registros na tabela `form_hidraulica_trimestral` onde `situacao_prev` é
     * `"Aberta"` e `iduser` corresponde ao usuário logado. - Permite busca por
     * `id_hidraulica_trimestral` usando a cláusula `LIKE` com `%`, garantindo
     * pesquisa parcial. - Retorna informações organizadas com alias para melhor
     * exibição na tabela: - `"N°"` → Número do formulário da preventiva. -
     * `"R.E"` → Registro do usuário responsável. - `"Técnico"` → Nome do
     * técnico responsável. - `"Preventiva"` → Tipo da manutenção preventiva. -
     * `"Setor"` → Setor onde foi realizada a preventiva. - `"Equipamento"` →
     * Nome do equipamento. - `"Código"` → Código de identificação do
     * equipamento. - `"Situação"` → Estado atual da preventiva. -
     * `"Distribuição"` → Tempo de distribuição da preventiva.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Aplica filtros de pesquisa a partir da entrada do usuário
     * (`txtPesqHid.getText()`). - Atribui os resultados à tabela `tblPreHid`
     * via `DbUtils.resultSetToTableModel(rs)`.
     *
     * - **Formatação da Tabela**: - Chama `tabelaFormat1()` para organizar
     * visualmente os dados exibidos na tabela.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * Fluxo do Método: 1. Obtém o critério de pesquisa inserido pelo usuário.
     * 2. Prepara e executa a consulta SQL segura. 3. Exibe os resultados
     * filtrados na tabela `tblPreHid`. 4. Aplica formatação à tabela para
     * melhor visualização. 5. Trata erros para evitar problemas na execução.
     */
    private void hid_tri_pes1() {

        situacao = "Aberta";

        String sql = "select id_hidraulica_trimestral as 'N°', iduser as 'R.E', nome_prev as Técnico, tipo_prev as 'Preventiva', setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', situacao_prev as 'Situação', tempo_dis as Distribuição from form_hidraulica_trimestral where iduser = ? and situacao_prev = ? and id_hidraulica_trimestral like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, situacao);
            pst.setString(3, txtPesqHid.getText() + "%");
            rs = pst.executeQuery();
            tblPreHid.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat1();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `hid_tri_quantida1`. Este método consulta o banco de dados para
     * contar o número de preventivas hidráulicas trimestrais associadas ao
     * usuário logado (`iduser`) que ainda estão na situação `"Aberta"`. O
     * resultado é exibido no campo `txtQuaHid`, permitindo um rápido
     * acompanhamento da quantidade de tarefas abertas.
     *
     * Funcionalidades: - **Consulta SQL com Contagem de Registros**: - Usa
     * `COUNT(*)` para retornar o número total de preventivas hidráulicas
     * trimestrais pendentes. - Filtra por `iduser`, garantindo que apenas
     * preventivas do usuário logado sejam contabilizadas. - Considera apenas
     * preventivas com `situacao_prev = "Aberta"`.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Obtém o resultado e atribui ao campo `txtQuaHid`.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * Fluxo do Método: 1. Define a situação da preventiva como `"Aberta"`. 2.
     * Prepara e executa a consulta SQL segura. 3. Obtém a contagem de
     * preventivas hidráulicas trimestrais pendentes. 4. Exibe o resultado no
     * campo `txtQuaHid`. 5. Trata erros para evitar problemas na execução.
     */
    private void hid_tri_quantida1() {
        situacao = "Aberta";

        String sql = "select count(*) from form_hidraulica_trimestral where iduser = ?  and situacao_prev = ? ";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, situacao);
            rs = pst.executeQuery();
            if (rs.next()) {
                txtQuaHid.setText(rs.getString(1));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `hid_sem_ini`. Este método consulta o banco de dados para obter
     * preventivas hidráulicas semestrais associadas ao usuário logado
     * (`iduser`) que estão na situação `"Aberta"`. Os resultados são exibidos
     * na tabela `tblPreHid`, permitindo um acompanhamento visual das tarefas.
     *
     * Funcionalidades: - **Definição da Situação da Preventiva**: - A variável
     * `situacao` é inicializada como `"Aberta"`, indicando que apenas
     * preventivas abertas serão recuperadas.
     *
     * - **Consulta SQL**: - Busca preventivas hidráulicas semestrais na tabela
     * `form_hidraulica_semestral` com base no `iduser`. - Retorna registros
     * onde `situacao_prev` é `"Aberta"`. - Formata os nomes das colunas para
     * exibição: - `"N°"` → Identificador da preventiva. - `"R.E"` → ID do
     * usuário responsável. - `"Técnico"` → Nome do técnico responsável. -
     * `"Preventiva"` → Tipo da manutenção preventiva. - `"Setor"` → Setor onde
     * a preventiva foi realizada. - `"Equipamento"` → Nome do equipamento. -
     * `"Código"` → Código de identificação do equipamento. - `"Situação"` →
     * Estado atual da preventiva. - `"Distribuição"` → Tempo de distribuição da
     * preventiva.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Atribui os resultados à tabela `tblPreHid` via
     * `DbUtils.resultSetToTableModel(rs)`.
     *
     * - **Formatação da Tabela**: - Chama `tabelaFormat1()` para ajustar
     * visualmente a tabela.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * Fluxo do Método: 1. Define a situação da preventiva como `"Aberta"`. 2.
     * Prepara e executa a consulta SQL. 3. Obtém os resultados e os exibe na
     * tabela `tblPreHid`. 4. Aplica formatação à tabela para melhor
     * visualização. 5. Trata erros para evitar falhas na execução.
     */
    private void hid_sem_ini() {
        situacao = "Aberta";

        String sql = "select id_hidraulica_semestral as 'N°', iduser as 'R.E', nome_prev as Técnico, tipo_prev as 'Preventiva', setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', situacao_prev as 'Situação', tempo_dis as Distribuição from  form_hidraulica_semestral where iduser = ? and situacao_prev = ? ";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, situacao);
            rs = pst.executeQuery();
            tblPreHid.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat1();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `hid_sem_pes1`. Este método permite a pesquisa de preventivas
     * hidráulicas semestrais no banco de dados com base no usuário logado
     * (`iduser`) e no critério de busca inserido pelo usuário (`txtPesqHid`).
     * Ele filtra as preventivas por número de formulário
     * (`id_hidraulica_semestral`) e exibe os resultados na tabela `tblPreHid`.
     *
     * Funcionalidades: - **Consulta SQL com Filtro de Pesquisa**: - Busca
     * registros na tabela `form_hidraulica_semestral` onde `situacao_prev` é
     * `"Aberta"` e `iduser` corresponde ao usuário logado. - Permite busca por
     * `id_hidraulica_semestral` usando a cláusula `LIKE` com `%`, garantindo
     * pesquisa parcial. - Retorna informações organizadas com alias para melhor
     * exibição na tabela: - `"N°"` → Número do formulário da preventiva. -
     * `"R.E"` → Registro do usuário responsável. - `"Técnico"` → Nome do
     * técnico responsável. - `"Preventiva"` → Tipo da manutenção preventiva. -
     * `"Setor"` → Setor onde foi realizada a preventiva. - `"Equipamento"` →
     * Nome do equipamento. - `"Código"` → Código de identificação do
     * equipamento. - `"Situação"` → Estado atual da preventiva. -
     * `"Distribuição"` → Tempo de distribuição da preventiva.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Aplica filtros de pesquisa a partir da entrada do usuário
     * (`txtPesqHid.getText()`). - Atribui os resultados à tabela `tblPreHid`
     * via `DbUtils.resultSetToTableModel(rs)`.
     *
     * - **Formatação da Tabela**: - Chama `tabelaFormat1()` para organizar
     * visualmente os dados exibidos na tabela.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * Fluxo do Método: 1. Obtém o critério de pesquisa inserido pelo usuário.
     * 2. Prepara e executa a consulta SQL segura. 3. Exibe os resultados
     * filtrados na tabela `tblPreHid`. 4. Aplica formatação à tabela para
     * melhor visualização. 5. Trata erros para evitar problemas na execução.
     */
    private void hid_sem_pes1() {

        situacao = "Aberta";

        String sql = "select id_hidraulica_semestral as 'N°', iduser as 'R.E', nome_prev as Técnico, tipo_prev as 'Preventiva', setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', situacao_prev as 'Situação', tempo_dis as Distribuição from form_hidraulica_semestral where iduser = ? and situacao_prev = ? and id_hidraulica_semestral like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, situacao);
            pst.setString(3, txtPesqHid.getText() + "%");
            rs = pst.executeQuery();
            tblPreHid.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat1();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `hid_sem_quantida1`. Este método consulta o banco de dados para
     * contar o número de preventivas hidráulicas semestrais associadas ao
     * usuário logado (`iduser`) que ainda estão na situação `"Aberta"`. O
     * resultado é exibido no campo `txtQuaHid`, permitindo um rápido
     * acompanhamento da quantidade de tarefas abertas.
     *
     * Funcionalidades: - **Consulta SQL com Contagem de Registros**: - Usa
     * `COUNT(*)` para retornar o número total de preventivas hidráulicas
     * semestrais pendentes. - Filtra por `iduser`, garantindo que apenas
     * preventivas do usuário logado sejam contabilizadas. - Considera apenas
     * preventivas com `situacao_prev = "Aberta"`.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Obtém o resultado e atribui ao campo `txtQuaHid`.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * Fluxo do Método: 1. Define a situação da preventiva como `"Aberta"`. 2.
     * Prepara e executa a consulta SQL segura. 3. Obtém a contagem de
     * preventivas hidráulicas semestrais pendentes. 4. Exibe o resultado no
     * campo `txtQuaHid`. 5. Trata erros para evitar problemas na execução.
     */
    private void hid_sem_quantida1() {
        situacao = "Aberta";

        String sql = "select count(*) from form_hidraulica_semestral where iduser = ?  and situacao_prev = ? ";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, situacao);
            rs = pst.executeQuery();
            if (rs.next()) {
                txtQuaHid.setText(rs.getString(1));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `ref_men_ini`. Este método consulta o banco de dados para obter
     * preventivas de refrigeração mensais associadas ao usuário logado
     * (`iduser`) que estão na situação `"Aberta"`. Os resultados são exibidos
     * na tabela `tblPreRef`, permitindo um acompanhamento visual das tarefas.
     *
     * Funcionalidades: - **Definição da Situação da Preventiva**: - A variável
     * `situacao` é inicializada como `"Aberta"`, indicando que apenas
     * preventivas abertas serão recuperadas.
     *
     * - **Consulta SQL**: - Busca preventivas de refrigeração mensais na tabela
     * `form_refrigeracao_mensal` com base no `iduser`. - Retorna registros onde
     * `situacao_prev` é `"Aberta"`. - Formata os nomes das colunas para
     * exibição: - `"N°"` → Identificador da preventiva. - `"R.E"` → ID do
     * usuário responsável. - `"Técnico"` → Nome do técnico responsável. -
     * `"Preventiva"` → Tipo da manutenção preventiva. - `"Setor"` → Setor onde
     * a preventiva foi realizada. - `"Equipamento"` → Nome do equipamento. -
     * `"Código"` → Código de identificação do equipamento. - `"Situação"` →
     * Estado atual da preventiva. - `"Distribuição"` → Tempo de distribuição da
     * preventiva.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Atribui os resultados à tabela `tblPreRef` via
     * `DbUtils.resultSetToTableModel(rs)`.
     *
     * - **Formatação da Tabela**: - Chama `tabelaFormat3()` para ajustar
     * visualmente a tabela.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * Fluxo do Método: 1. Define a situação da preventiva como `"Aberta"`. 2.
     * Prepara e executa a consulta SQL. 3. Obtém os resultados e os exibe na
     * tabela `tblPreRef`. 4. Aplica formatação à tabela para melhor
     * visualização. 5. Trata erros para evitar falhas na execução.
     */
    private void ref_men_ini() {
        situacao = "Aberta";

        String sql = "select id_refrigeracao_mensal as 'N°', iduser as 'R.E', nome_prev as Técnico, tipo_prev as 'Preventiva', setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', situacao_prev as 'Situação', tempo_dis as Distribuição from  form_refrigeracao_mensal where iduser = ? and situacao_prev = ? ";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, situacao);
            rs = pst.executeQuery();
            tblPreRef.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat3();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `ref_men_pes1`. Este método permite a pesquisa de preventivas de
     * refrigeração mensais no banco de dados com base no usuário logado
     * (`iduser`) e no critério de busca inserido pelo usuário (`txtPesqRef`).
     * Ele filtra as preventivas por número de formulário
     * (`id_refrigeracao_mensal`) e exibe os resultados na tabela `tblPreRef`.
     *
     * Funcionalidades: - **Consulta SQL com Filtro de Pesquisa**: - Busca
     * registros na tabela `form_refrigeracao_mensal` onde `situacao_prev` é
     * `"Aberta"` e `iduser` corresponde ao usuário logado. - Permite busca por
     * `id_refrigeracao_mensal` usando a cláusula `LIKE` com `%`, garantindo
     * pesquisa parcial. - Retorna informações organizadas com alias para melhor
     * exibição na tabela: - `"N°"` → Número do formulário da preventiva. -
     * `"R.E"` → Registro do usuário responsável. - `"Técnico"` → Nome do
     * técnico responsável. - `"Preventiva"` → Tipo da manutenção preventiva. -
     * `"Setor"` → Setor onde foi realizada a preventiva. - `"Equipamento"` →
     * Nome do equipamento. - `"Código"` → Código de identificação do
     * equipamento. - `"Situação"` → Estado atual da preventiva. -
     * `"Distribuição"` → Tempo de distribuição da preventiva.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Aplica filtros de pesquisa a partir da entrada do usuário
     * (`txtPesqRef.getText()`). - Atribui os resultados à tabela `tblPreRef`
     * via `DbUtils.resultSetToTableModel(rs)`.
     *
     * - **Formatação da Tabela**: - Chama `tabelaFormat3()` para organizar
     * visualmente os dados exibidos na tabela.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * Fluxo do Método: 1. Obtém o critério de pesquisa inserido pelo usuário.
     * 2. Prepara e executa a consulta SQL segura. 3. Exibe os resultados
     * filtrados na tabela `tblPreRef`. 4. Aplica formatação à tabela para
     * melhor visualização. 5. Trata erros para evitar problemas na execução.
     */
    private void ref_men_pes1() {

        situacao = "Aberta";

        String sql = "select id_refrigeracao_mensal as 'N°', iduser as 'R.E', nome_prev as Técnico, tipo_prev as 'Preventiva', setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', situacao_prev as 'Situação', tempo_dis as Distribuição from form_refrigeracao_mensal where iduser = ? and situacao_prev = ? and id_refrigeracao_mensal like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, situacao);
            pst.setString(3, txtPesqRef.getText() + "%");
            rs = pst.executeQuery();
            tblPreRef.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat3();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `ref_men_quantida1`. Este método consulta o banco de dados para
     * contar o número de preventivas de refrigeração mensais associadas ao
     * usuário logado (`iduser`) que ainda estão na situação `"Aberta"`. O
     * resultado é exibido no campo `txtQuaRef`, permitindo um rápido
     * acompanhamento da quantidade de tarefas abertas.
     *
     * Funcionalidades: - **Consulta SQL com Contagem de Registros**: - Usa
     * `COUNT(*)` para retornar o número total de preventivas de refrigeração
     * mensais pendentes. - Filtra por `iduser`, garantindo que apenas
     * preventivas do usuário logado sejam contabilizadas. - Considera apenas
     * preventivas com `situacao_prev = "Aberta"`.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Obtém o resultado e atribui ao campo `txtQuaRef`.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * Fluxo do Método: 1. Define a situação da preventiva como `"Aberta"`. 2.
     * Prepara e executa a consulta SQL segura. 3. Obtém a contagem de
     * preventivas de refrigeração mensais pendentes. 4. Exibe o resultado no
     * campo `txtQuaRef`. 5. Trata erros para evitar problemas na execução.
     */
    private void ref_men_quantida1() {
        situacao = "Aberta";

        String sql = "select count(*) from form_refrigeracao_mensal where iduser = ?  and situacao_prev = ? ";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, situacao);
            rs = pst.executeQuery();
            if (rs.next()) {
                txtQuaRef.setText(rs.getString(1));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `ref_tri_ini`. Este método consulta o banco de dados para obter
     * preventivas de refrigeração trimestrais associadas ao usuário logado
     * (`iduser`) que estão na situação `"Aberta"`. Os resultados são exibidos
     * na tabela `tblPreRef`, permitindo um acompanhamento visual das tarefas.
     *
     * Funcionalidades: - **Definição da Situação da Preventiva**: - A variável
     * `situacao` é inicializada como `"Aberta"`, indicando que apenas
     * preventivas abertas serão recuperadas.
     *
     * - **Consulta SQL**: - Busca preventivas de refrigeração trimestrais na
     * tabela `form_refrigeracao_trimestral` com base no `iduser`. - Retorna
     * registros onde `situacao_prev` é `"Aberta"`. - Formata os nomes das
     * colunas para exibição: - `"N°"` → Identificador da preventiva. - `"R.E"`
     * → ID do usuário responsável. - `"Técnico"` → Nome do técnico responsável.
     * - `"Preventiva"` → Tipo da manutenção preventiva. - `"Setor"` → Setor
     * onde a preventiva foi realizada. - `"Equipamento"` → Nome do equipamento.
     * - `"Código"` → Código de identificação do equipamento. - `"Situação"` →
     * Estado atual da preventiva. - `"Distribuição"` → Tempo de distribuição da
     * preventiva.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Atribui os resultados à tabela `tblPreRef` via
     * `DbUtils.resultSetToTableModel(rs)`.
     *
     * - **Formatação da Tabela**: - Chama `tabelaFormat3()` para ajustar
     * visualmente a tabela.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * Fluxo do Método: 1. Define a situação da preventiva como `"Aberta"`. 2.
     * Prepara e executa a consulta SQL. 3. Obtém os resultados e os exibe na
     * tabela `tblPreRef`. 4. Aplica formatação à tabela para melhor
     * visualização. 5. Trata erros para evitar falhas na execução.
     */
    private void ref_tri_ini() {
        situacao = "Aberta";

        String sql = "select id_refrigeracao_trimestral as 'N°', iduser as 'R.E', nome_prev as Técnico, tipo_prev as 'Preventiva', setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'CCódigo', situacao_prev as 'Situação', tempo_dis as Distribuição from  form_refrigeracao_trimestral where iduser = ? and situacao_prev = ? ";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, situacao);
            rs = pst.executeQuery();
            tblPreRef.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat3();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `ref_tri_quantida1`. Este método consulta o banco de dados para
     * contar o número de preventivas de refrigeração trimestrais associadas ao
     * usuário logado (`iduser`) que ainda estão na situação `"Aberta"`. O
     * resultado é exibido no campo `txtQuaRef`, permitindo um rápido
     * acompanhamento da quantidade de tarefas abertas.
     *
     * Funcionalidades: - **Consulta SQL com Contagem de Registros**: - Usa
     * `COUNT(*)` para retornar o número total de preventivas de refrigeração
     * trimestrais pendentes. - Filtra por `iduser`, garantindo que apenas
     * preventivas do usuário logado sejam contabilizadas. - Considera apenas
     * preventivas com `situacao_prev = "Aberta"`.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Obtém o resultado e atribui ao campo `txtQuaRef`.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * Fluxo do Método: 1. Define a situação da preventiva como `"Aberta"`. 2.
     * Prepara e executa a consulta SQL segura. 3. Obtém a contagem de
     * preventivas de refrigeração trimestrais pendentes. 4. Exibe o resultado
     * no campo `txtQuaRef`. 5. Trata erros para evitar problemas na execução.
     */
    private void ref_tri_quantida1() {
        situacao = "Aberta";

        String sql = "select count(*) from form_refrigeracao_trimestral where iduser = ?  and situacao_prev = ? ";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, situacao);
            rs = pst.executeQuery();
            if (rs.next()) {
                txtQuaRef.setText(rs.getString(1));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `ref_tri_pes1`. Este método permite a pesquisa de preventivas de
     * refrigeração trimestrais no banco de dados com base no usuário logado
     * (`iduser`) e no critério de busca inserido pelo usuário (`txtPesqRef`).
     * Ele filtra as preventivas por número de formulário
     * (`id_refrigeracao_trimestral`) e exibe os resultados na tabela
     * `tblPreRef`.
     *
     * Funcionalidades: - **Consulta SQL com Filtro de Pesquisa**: - Busca
     * registros na tabela `form_refrigeracao_trimestral` onde `situacao_prev` é
     * `"Aberta"` e `iduser` corresponde ao usuário logado. - Permite busca por
     * `id_refrigeracao_trimestral` usando a cláusula `LIKE` com `%`, garantindo
     * pesquisa parcial. - Retorna informações organizadas com alias para melhor
     * exibição na tabela: - `"N°"` → Número do formulário da preventiva. -
     * `"R.E"` → Registro do usuário responsável. - `"Técnico"` → Nome do
     * técnico responsável. - `"Preventiva"` → Tipo da manutenção preventiva. -
     * `"Setor"` → Setor onde foi realizada a preventiva. - `"Equipamento"` →
     * Nome do equipamento. - `"Código"` → Código de identificação do
     * equipamento. - `"Situação"` → Estado atual da preventiva. -
     * `"Distribuição"` → Tempo de distribuição da preventiva.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Aplica filtros de pesquisa a partir da entrada do usuário
     * (`txtPesqRef.getText()`). - Atribui os resultados à tabela `tblPreRef`
     * via `DbUtils.resultSetToTableModel(rs)`.
     *
     * - **Formatação da Tabela**: - Chama `tabelaFormat3()` para organizar
     * visualmente os dados exibidos na tabela.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * Fluxo do Método: 1. Obtém o critério de pesquisa inserido pelo usuário.
     * 2. Prepara e executa a consulta SQL segura. 3. Exibe os resultados
     * filtrados na tabela `tblPreRef`. 4. Aplica formatação à tabela para
     * melhor visualização. 5. Trata erros para evitar problemas na execução.
     */
    private void ref_tri_pes1() {

        situacao = "Aberta";

        String sql = "select id_refrigeracao_trimestral as 'N°', iduser as 'R.E', nome_prev as Técnico, tipo_prev as 'Preventiva', setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', situacao_prev as 'Situação', tempo_dis as Distribuição from form_refrigeracao_trimestral where iduser = ? and situacao_prev = ? and id_refrigeracao_trimestral like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, situacao);
            pst.setString(3, txtPesqRef.getText() + "%");
            rs = pst.executeQuery();
            tblPreRef.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat3();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `ref_sem_quantida1`. Este método consulta o banco de dados para
     * contar o número de preventivas de refrigeração semestrais associadas ao
     * usuário logado (`iduser`) que ainda estão na situação `"Aberta"`. O
     * resultado é exibido no campo `txtQuaRef`, permitindo um rápido
     * acompanhamento da quantidade de tarefas abertas.
     *
     * Funcionalidades: - **Consulta SQL com Contagem de Registros**: - Usa
     * `COUNT(*)` para retornar o número total de preventivas de refrigeração
     * semestrais pendentes. - Filtra por `iduser`, garantindo que apenas
     * preventivas do usuário logado sejam contabilizadas. - Considera apenas
     * preventivas com `situacao_prev = "Aberta"`.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Obtém o resultado e atribui ao campo `txtQuaRef`.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * Fluxo do Método: 1. Define a situação da preventiva como `"Aberta"`. 2.
     * Prepara e executa a consulta SQL segura. 3. Obtém a contagem de
     * preventivas de refrigeração semestrais pendentes. 4. Exibe o resultado no
     * campo `txtQuaRef`. 5. Trata erros para evitar problemas na execução.
     */
    private void ref_sem_quantida1() {
        situacao = "Aberta";

        String sql = "select count(*) from form_refrigeracao_semestral where iduser = ?  and situacao_prev = ? ";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, situacao);
            rs = pst.executeQuery();
            if (rs.next()) {
                txtQuaRef.setText(rs.getString(1));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `ref_sem_ini`. Este método consulta o banco de dados para obter
     * preventivas de refrigeração semestrais associadas ao usuário logado
     * (`iduser`) que estão na situação `"Aberta"`. Os resultados são exibidos
     * na tabela `tblPreRef`, permitindo um acompanhamento visual das tarefas.
     *
     * Funcionalidades: - **Definição da Situação da Preventiva**: - A variável
     * `situacao` é inicializada como `"Aberta"`, indicando que apenas
     * preventivas abertas serão recuperadas.
     *
     * - **Consulta SQL**: - Busca preventivas de refrigeração semestrais na
     * tabela `form_refrigeracao_semestral` com base no `iduser`. - Retorna
     * registros onde `situacao_prev` é `"Aberta"`. - Formata os nomes das
     * colunas para exibição: - `"N°"` → Identificador da preventiva. - `"R.E"`
     * → ID do usuário responsável. - `"Técnico"` → Nome do técnico responsável.
     * - `"Preventiva"` → Tipo da manutenção preventiva. - `"Setor"` → Setor
     * onde a preventiva foi realizada. - `"Equipamento"` → Nome do equipamento.
     * - `"Código"` → Código de identificação do equipamento. - `"Situação"` →
     * Estado atual da preventiva. - `"Distribuição"` → Tempo de distribuição da
     * preventiva.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Atribui os resultados à tabela `tblPreRef` via
     * `DbUtils.resultSetToTableModel(rs)`.
     *
     * - **Formatação da Tabela**: - Chama `tabelaFormat3()` para ajustar
     * visualmente a tabela.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * Fluxo do Método: 1. Define a situação da preventiva como `"Aberta"`. 2.
     * Prepara e executa a consulta SQL. 3. Obtém os resultados e os exibe na
     * tabela `tblPreRef`. 4. Aplica formatação à tabela para melhor
     * visualização. 5. Trata erros para evitar falhas na execução.
     */
    private void ref_sem_ini() {
        situacao = "Aberta";

        String sql = "select id_refrigeracao_semestral as 'N°', iduser as 'R.E', nome_prev as Técnico, tipo_prev as 'Preventiva', setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', situacao_prev as 'Situação', tempo_dis as Distribuição from  form_refrigeracao_semestral where iduser = ? and situacao_prev = ? ";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, situacao);
            rs = pst.executeQuery();
            tblPreRef.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat3();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `ref_sem_pes1`. Este método permite a pesquisa de preventivas de
     * refrigeração semestrais no banco de dados com base no usuário logado
     * (`iduser`) e no critério de busca inserido pelo usuário (`txtPesqRef`).
     * Ele filtra as preventivas por número de formulário
     * (`id_refrigeracao_semestral`) e exibe os resultados na tabela
     * `tblPreRef`.
     *
     * Funcionalidades: - **Consulta SQL com Filtro de Pesquisa**: - Busca
     * registros na tabela `form_refrigeracao_semestral` onde `situacao_prev` é
     * `"Aberta"` e `iduser` corresponde ao usuário logado. - Permite busca por
     * `id_refrigeracao_semestral` usando a cláusula `LIKE` com `%`, garantindo
     * pesquisa parcial. - Retorna informações organizadas com alias para melhor
     * exibição na tabela: - `"N°"` → Número do formulário da preventiva. -
     * `"R.E"` → Registro do usuário responsável. - `"Técnico"` → Nome do
     * técnico responsável. - `"Preventiva"` → Tipo da manutenção preventiva. -
     * `"Setor"` → Setor onde foi realizada a preventiva. - `"Equipamento"` →
     * Nome do equipamento. - `"Código"` → Código de identificação do
     * equipamento. - `"Situação"` → Estado atual da preventiva. -
     * `"Distribuição"` → Tempo de distribuição da preventiva.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Aplica filtros de pesquisa a partir da entrada do usuário
     * (`txtPesqRef.getText()`). - Atribui os resultados à tabela `tblPreRef`
     * via `DbUtils.resultSetToTableModel(rs)`.
     *
     * - **Formatação da Tabela**: - Chama `tabelaFormat3()` para organizar
     * visualmente os dados exibidos na tabela.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * Fluxo do Método: 1. Obtém o critério de pesquisa inserido pelo usuário.
     * 2. Prepara e executa a consulta SQL segura. 3. Exibe os resultados
     * filtrados na tabela `tblPreRef`. 4. Aplica formatação à tabela para
     * melhor visualização. 5. Trata erros para evitar problemas na execução.
     */
    private void ref_sem_pes1() {

        situacao = "Aberta";

        String sql = "select id_refrigeracao_semestral as 'N°', iduser as 'R.E', nome_prev as Técnico, tipo_prev as 'Preventiva', setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', situacao_prev as 'Situação', tempo_dis as Distribuição from form_refrigeracao_semestral where iduser = ? and situacao_prev = ? and id_refrigeracao_semestral like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, situacao);
            pst.setString(3, txtPesqRef.getText() + "%");
            rs = pst.executeQuery();
            tblPreRef.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat3();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `civ_men_ini`. Este método consulta o banco de dados para obter
     * preventivas civis mensais associadas ao usuário logado (`iduser`) que
     * estão na situação `"Aberta"`. Os resultados são exibidos na tabela
     * `tblPreCivil`, permitindo um acompanhamento visual das tarefas.
     *
     * Funcionalidades: - **Definição da Situação da Preventiva**: - A variável
     * `situacao` é inicializada como `"Aberta"`, indicando que apenas
     * preventivas abertas serão recuperadas.
     *
     * - **Consulta SQL**: - Busca preventivas civis mensais na tabela
     * `form_civil_mensal` com base no `iduser`. - Retorna registros onde
     * `situacao_prev` é `"Aberta"`. - Formata os nomes das colunas para
     * exibição: - `"N°"` → Identificador da preventiva. - `"R.E"` → ID do
     * usuário responsável. - `"Técnico"` → Nome do técnico responsável. -
     * `"Preventiva"` → Tipo da manutenção preventiva. - `"Setor"` → Setor onde
     * a preventiva foi realizada. - `"Equipamento"` → Nome do equipamento. -
     * `"Código"` → Código de identificação do equipamento. - `"Situação"` →
     * Estado atual da preventiva. - `"Distribuição"` → Tempo de distribuição da
     * preventiva.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Atribui os resultados à tabela `tblPreCivil` via
     * `DbUtils.resultSetToTableModel(rs)`.
     *
     * - **Formatação da Tabela**: - Chama `tabelaFormat2()` para ajustar
     * visualmente a tabela.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * Fluxo do Método: 1. Define a situação da preventiva como `"Aberta"`. 2.
     * Prepara e executa a consulta SQL. 3. Obtém os resultados e os exibe na
     * tabela `tblPreCivil`. 4. Aplica formatação à tabela para melhor
     * visualização. 5. Trata erros para evitar falhas na execução.
     */
    private void civ_men_ini() {
        situacao = "Aberta";

        String sql = "select id_civil_mensal as 'N°', iduser as 'R.E', nome_prev as Técnico, tipo_prev as 'Preventiva', setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', situacao_prev as 'Situação', tempo_dis as Distribuição from  form_civil_mensal where iduser = ? and situacao_prev = ? ";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, situacao);
            rs = pst.executeQuery();
            tblPreCivil.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat2();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `civ_men_pes1`. Este método permite a pesquisa de preventivas
     * civis mensais no banco de dados com base no usuário logado (`iduser`) e
     * no critério de busca inserido pelo usuário (`txtPesqCiv`). Ele filtra as
     * preventivas por número de formulário (`id_civil_mensal`) e exibe os
     * resultados na tabela `tblPreCivil`.
     *
     * Funcionalidades: - **Consulta SQL com Filtro de Pesquisa**: - Busca
     * registros na tabela `form_civil_mensal` onde `situacao_prev` é `"Aberta"`
     * e `iduser` corresponde ao usuário logado. - Permite busca por
     * `id_civil_mensal` usando a cláusula `LIKE` com `%`, garantindo pesquisa
     * parcial. - Retorna informações organizadas com alias para melhor exibição
     * na tabela: - `"N°"` → Número do formulário da preventiva. - `"R.E"` →
     * Registro do usuário responsável. - `"Técnico"` → Nome do técnico
     * responsável. - `"Preventiva"` → Tipo da manutenção preventiva. -
     * `"Setor"` → Setor onde foi realizada a preventiva. - `"Equipamento"` →
     * Nome do equipamento. - `"Código"` → Código de identificação do
     * equipamento. - `"Situação"` → Estado atual da preventiva. -
     * `"Distribuição"` → Tempo de distribuição da preventiva.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Aplica filtros de pesquisa a partir da entrada do usuário
     * (`txtPesqCiv.getText()`). - Atribui os resultados à tabela `tblPreCivil`
     * via `DbUtils.resultSetToTableModel(rs)`.
     *
     * - **Formatação da Tabela**: - Chama `tabelaFormat2()` para organizar
     * visualmente os dados exibidos na tabela.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * Fluxo do Método: 1. Obtém o critério de pesquisa inserido pelo usuário.
     * 2. Prepara e executa a consulta SQL segura. 3. Exibe os resultados
     * filtrados na tabela `tblPreCivil`. 4. Aplica formatação à tabela para
     * melhor visualização. 5. Trata erros para evitar problemas na execução.
     */
    private void civ_men_pes1() {

        situacao = "Aberta";

        String sql = "select id_civil_mensal as 'N°', iduser as 'R.E', nome_prev as Técnico, tipo_prev as 'Preventiva', setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', situacao_prev as 'Situação', tempo_dis as Distribuição from form_civil_mensal where iduser = ? and situacao_prev = ? and id_civil_mensal like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, situacao);
            pst.setString(3, txtPesqCiv.getText() + "%");
            rs = pst.executeQuery();
            tblPreCivil.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat2();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `civ_men_quantida1`. Este método consulta o banco de dados para
     * contar o número de preventivas civis mensais associadas ao usuário logado
     * (`iduser`) que ainda estão na situação `"Aberta"`. O resultado é exibido
     * no campo `txtQuaCiv`, permitindo um rápido acompanhamento da quantidade
     * de tarefas abertas.
     *
     * Funcionalidades: - **Consulta SQL com Contagem de Registros**: - Usa
     * `COUNT(*)` para retornar o número total de preventivas civis mensais
     * pendentes. - Filtra por `iduser`, garantindo que apenas preventivas do
     * usuário logado sejam contabilizadas. - Considera apenas preventivas com
     * `situacao_prev = "Aberta"`.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Obtém o resultado e atribui ao campo `txtQuaCiv`.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * Fluxo do Método: 1. Define a situação da preventiva como `"Aberta"`. 2.
     * Prepara e executa a consulta SQL segura. 3. Obtém a contagem de
     * preventivas civis mensais pendentes. 4. Exibe o resultado no campo
     * `txtQuaCiv`. 5. Trata erros para evitar problemas na execução.
     */
    private void civ_men_quantida1() {
        situacao = "Aberta";

        String sql = "select count(*) from form_civil_mensal where iduser = ?  and situacao_prev = ? ";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, situacao);
            rs = pst.executeQuery();
            if (rs.next()) {
                txtQuaCiv.setText(rs.getString(1));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `civ_tri_ini`. Este método consulta o banco de dados para obter
     * preventivas civis trimestrais associadas ao usuário logado (`iduser`) que
     * estão na situação `"Aberta"`. Os resultados são exibidos na tabela
     * `tblPreCivil`, permitindo um acompanhamento visual das tarefas.
     *
     * Funcionalidades: - **Definição da Situação da Preventiva**: - A variável
     * `situacao` é inicializada como `"Aberta"`, indicando que apenas
     * preventivas abertas serão recuperadas.
     *
     * - **Consulta SQL**: - Busca preventivas civis trimestrais na tabela
     * `form_civil_trimestral` com base no `iduser`. - Retorna registros onde
     * `situacao_prev` é `"Aberta"`. - Formata os nomes das colunas para
     * exibição: - `"N°"` → Identificador da preventiva. - `"R.E"` → ID do
     * usuário responsável. - `"Técnico"` → Nome do técnico responsável. -
     * `"Preventiva"` → Tipo da manutenção preventiva. - `"Setor"` → Setor onde
     * a preventiva foi realizada. - `"Equipamento"` → Nome do equipamento. -
     * `"Código"` → Código de identificação do equipamento. - `"Situação"` →
     * Estado atual da preventiva. - `"Distribuição"` → Tempo de distribuição da
     * preventiva.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Atribui os resultados à tabela `tblPreCivil` via
     * `DbUtils.resultSetToTableModel(rs)`.
     *
     * - **Formatação da Tabela**: - Chama `tabelaFormat2()` para ajustar
     * visualmente a tabela.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * Fluxo do Método: 1. Define a situação da preventiva como `"Aberta"`. 2.
     * Prepara e executa a consulta SQL. 3. Obtém os resultados e os exibe na
     * tabela `tblPreCivil`. 4. Aplica formatação à tabela para melhor
     * visualização. 5. Trata erros para evitar falhas na execução.
     */
    private void civ_tri_ini() {
        situacao = "Aberta";

        String sql = "select id_civil_trimestral as 'N°', iduser as 'R.E', nome_prev as Técnico, tipo_prev as 'Preventiva', setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', situacao_prev as 'Situação', tempo_dis as Distribuição from  form_civil_trimestral where iduser = ? and situacao_prev = ? ";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, situacao);
            rs = pst.executeQuery();
            tblPreCivil.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat2();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `civ_tri_pes1`. Este método permite a pesquisa de preventivas
     * civis trimestrais no banco de dados com base no usuário logado (`iduser`)
     * e no critério de busca inserido pelo usuário (`txtPesqCiv`). Ele filtra
     * as preventivas por número de formulário (`id_civil_trimestral`) e exibe
     * os resultados na tabela `tblPreCivil`.
     *
     * Funcionalidades: - **Consulta SQL com Filtro de Pesquisa**: - Busca
     * registros na tabela `form_civil_trimestral` onde `situacao_prev` é
     * `"Aberta"` e `iduser` corresponde ao usuário logado. - Permite busca por
     * `id_civil_trimestral` usando a cláusula `LIKE` com `%`, garantindo
     * pesquisa parcial. - Retorna informações organizadas com alias para melhor
     * exibição na tabela: - `"N°"` → Número do formulário da preventiva. -
     * `"R.E"` → Registro do usuário responsável. - `"Técnico"` → Nome do
     * técnico responsável. - `"Preventiva"` → Tipo da manutenção preventiva. -
     * `"Setor"` → Setor onde foi realizada a preventiva. - `"Equipamento"` →
     * Nome do equipamento. - `"Código"` → Código de identificação do
     * equipamento. - `"Situação"` → Estado atual da preventiva. -
     * `"Distribuição"` → Tempo de distribuição da preventiva.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Aplica filtros de pesquisa a partir da entrada do usuário
     * (`txtPesqCiv.getText()`). - Atribui os resultados à tabela `tblPreCivil`
     * via `DbUtils.resultSetToTableModel(rs)`.
     *
     * - **Formatação da Tabela**: - Chama `tabelaFormat2()` para organizar
     * visualmente os dados exibidos na tabela.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * Fluxo do Método: 1. Obtém o critério de pesquisa inserido pelo usuário.
     * 2. Prepara e executa a consulta SQL segura. 3. Exibe os resultados
     * filtrados na tabela `tblPreCivil`. 4. Aplica formatação à tabela para
     * melhor visualização. 5. Trata erros para evitar problemas na execução.
     */
    private void civ_tri_pes1() {

        situacao = "Aberta";

        String sql = "select id_civil_trimestral as 'N°', iduser as 'R.E', nome_prev as Técnico, tipo_prev as 'Preventiva', setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', situacao_prev as 'Situação', tempo_dis as Distribuição from form_civil_trimestral where iduser = ? and situacao_prev = ? and id_civil_trimestral like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, situacao);
            pst.setString(3, txtPesqCiv.getText() + "%");
            rs = pst.executeQuery();
            tblPreCivil.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat2();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `civ_tri_pes1`. Este método permite a pesquisa de preventivas
     * civis trimestrais no banco de dados com base no usuário logado (`iduser`)
     * e no critério de busca inserido pelo usuário (`txtPesqCiv`). Ele filtra
     * as preventivas por número de formulário (`id_civil_trimestral`) e exibe
     * os resultados na tabela `tblPreCivil`.
     *
     * Funcionalidades: - **Consulta SQL com Filtro de Pesquisa**: - Busca
     * registros na tabela `form_civil_trimestral` onde `situacao_prev` é
     * `"Aberta"` e `iduser` corresponde ao usuário logado. - Permite busca por
     * `id_civil_trimestral` usando a cláusula `LIKE` com `%`, garantindo
     * pesquisa parcial. - Retorna informações organizadas com alias para melhor
     * exibição na tabela: - `"N°"` → Número do formulário da preventiva. -
     * `"R.E"` → Registro do usuário responsável. - `"Técnico"` → Nome do
     * técnico responsável. - `"Preventiva"` → Tipo da manutenção preventiva. -
     * `"Setor"` → Setor onde foi realizada a preventiva. - `"Equipamento"` →
     * Nome do equipamento. - `"Código"` → Código de identificação do
     * equipamento. - `"Situação"` → Estado atual da preventiva. -
     * `"Distribuição"` → Tempo de distribuição da preventiva.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Aplica filtros de pesquisa a partir da entrada do usuário
     * (`txtPesqCiv.getText()`). - Atribui os resultados à tabela `tblPreCivil`
     * via `DbUtils.resultSetToTableModel(rs)`.
     *
     * - **Formatação da Tabela**: - Chama `tabelaFormat2()` para organizar
     * visualmente os dados exibidos na tabela.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * Fluxo do Método: 1. Obtém o critério de pesquisa inserido pelo usuário.
     * 2. Prepara e executa a consulta SQL segura. 3. Exibe os resultados
     * filtrados na tabela `tblPreCivil`. 4. Aplica formatação à tabela para
     * melhor visualização. 5. Trata erros para evitar problemas na execução.
     */
    private void civ_tri_quantida1() {
        situacao = "Aberta";

        String sql = "select count(*) from form_civil_trimestral where iduser = ?  and situacao_prev = ? ";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, situacao);
            rs = pst.executeQuery();
            if (rs.next()) {
                txtQuaCiv.setText(rs.getString(1));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `civ_sem_ini`. Este método consulta o banco de dados para obter
     * preventivas civis semestrais associadas ao usuário logado (`iduser`) que
     * estão na situação `"Aberta"`. Os resultados são exibidos na tabela
     * `tblPreCivil`, permitindo um acompanhamento visual das tarefas.
     *
     * Funcionalidades: - **Definição da Situação da Preventiva**: - A variável
     * `situacao` é inicializada como `"Aberta"`, indicando que apenas
     * preventivas abertas serão recuperadas.
     *
     * - **Consulta SQL**: - Busca preventivas civis semestrais na tabela
     * `form_civil_semestral` com base no `iduser`. - Retorna registros onde
     * `situacao_prev` é `"Aberta"`. - Formata os nomes das colunas para
     * exibição: - `"N°"` → Identificador da preventiva. - `"R.E"` → ID do
     * usuário responsável. - `"Técnico"` → Nome do técnico responsável. -
     * `"Preventiva"` → Tipo da manutenção preventiva. - `"Setor"` → Setor onde
     * a preventiva foi realizada. - `"Equipamento"` → Nome do equipamento. -
     * `"Código"` → Código de identificação do equipamento. - `"Situação"` →
     * Estado atual da preventiva. - `"Distribuição"` → Tempo de distribuição da
     * preventiva.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Atribui os resultados à tabela `tblPreCivil` via
     * `DbUtils.resultSetToTableModel(rs)`.
     *
     * - **Formatação da Tabela**: - Chama `tabelaFormat2()` para ajustar
     * visualmente a tabela.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * Fluxo do Método: 1. Define a situação da preventiva como `"Aberta"`. 2.
     * Prepara e executa a consulta SQL. 3. Obtém os resultados e os exibe na
     * tabela `tblPreCivil`. 4. Aplica formatação à tabela para melhor
     * visualização. 5. Trata erros para evitar falhas na execução.
     */
    private void civ_sem_ini() {
        situacao = "Aberta";

        String sql = "select id_civil_semestral as 'N°', iduser as 'R.E', nome_prev as Técnico, tipo_prev as 'Preventiva', setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', situacao_prev as 'Situação', tempo_dis as Distribuição from  form_civil_semestral where iduser = ? and situacao_prev = ? ";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, situacao);
            rs = pst.executeQuery();
            tblPreCivil.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat2();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `civ_sem_pes1`. Este método permite a pesquisa de preventivas
     * civis semestrais no banco de dados com base no usuário logado (`iduser`)
     * e no critério de busca inserido pelo usuário (`txtPesqCiv`). Ele filtra
     * as preventivas por número de formulário (`id_civil_semestral`) e exibe os
     * resultados na tabela `tblPreCivil`.
     *
     * Funcionalidades: - **Consulta SQL com Filtro de Pesquisa**: - Busca
     * registros na tabela `form_civil_semestral` onde `situacao_prev` é
     * `"Aberta"` e `iduser` corresponde ao usuário logado. - Permite busca por
     * `id_civil_semestral` usando a cláusula `LIKE` com `%`, garantindo
     * pesquisa parcial. - Retorna informações organizadas com alias para melhor
     * exibição na tabela: - `"N°"` → Número do formulário da preventiva. -
     * `"R.E"` → Registro do usuário responsável. - `"Técnico"` → Nome do
     * técnico responsável. - `"Preventiva"` → Tipo da manutenção preventiva. -
     * `"Setor"` → Setor onde foi realizada a preventiva. - `"Equipamento"` →
     * Nome do equipamento. - `"Código"` → Código de identificação do
     * equipamento. - `"Situação"` → Estado atual da preventiva. -
     * `"Distribuição"` → Tempo de distribuição da preventiva.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Aplica filtros de pesquisa a partir da entrada do usuário
     * (`txtPesqCiv.getText()`). - Atribui os resultados à tabela `tblPreCivil`
     * via `DbUtils.resultSetToTableModel(rs)`.
     *
     * - **Formatação da Tabela**: - Chama `tabelaFormat2()` para organizar
     * visualmente os dados exibidos na tabela.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * Fluxo do Método: 1. Obtém o critério de pesquisa inserido pelo usuário.
     * 2. Prepara e executa a consulta SQL segura. 3. Exibe os resultados
     * filtrados na tabela `tblPreCivil`. 4. Aplica formatação à tabela para
     * melhor visualização. 5. Trata erros para evitar problemas na execução.
     */
    private void civ_sem_pes1() {

        situacao = "Aberta";

        String sql = "select id_civil_semestral as 'N°', iduser as 'R.E', nome_prev as Técnico, tipo_prev as 'Preventiva', setor_prev as Setor, nome_equi_set as Equipamento, cod_equi_set as 'Código', situacao_prev as 'Situação', tempo_dis as Distribuição from form_civil_semestral where iduser = ? and situacao_prev = ? and id_civil_semestral like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, situacao);
            pst.setString(3, txtPesqCiv.getText() + "%");
            rs = pst.executeQuery();
            tblPreCivil.setModel(DbUtils.resultSetToTableModel(rs));
            tabelaFormat2();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `civ_sem_quantida1`. Este método consulta o banco de dados para
     * contar o número de preventivas civis semestrais associadas ao usuário
     * logado (`iduser`) que ainda estão na situação `"Aberta"`. O resultado é
     * exibido no campo `txtQuaCiv`, permitindo um rápido acompanhamento da
     * quantidade de tarefas abertas.
     *
     * Funcionalidades: - **Consulta SQL com Contagem de Registros**: - Usa
     * `COUNT(*)` para retornar o número total de preventivas civis semestrais
     * pendentes. - Filtra por `iduser`, garantindo que apenas preventivas do
     * usuário logado sejam contabilizadas. - Considera apenas preventivas com
     * `situacao_prev = "Aberta"`.
     *
     * - **Execução da Query**: - Usa `PreparedStatement` para segurança contra
     * injeção SQL. - Obtém o resultado e atribui ao campo `txtQuaCiv`.
     *
     * - **Tratamento de Erros**: - Captura exceções `SQLException` e exibe
     * mensagens de erro para evitar falhas silenciosas.
     *
     * Fluxo do Método: 1. Define a situação da preventiva como `"Aberta"`. 2.
     * Prepara e executa a consulta SQL segura. 3. Obtém a contagem de
     * preventivas civis semestrais pendentes. 4. Exibe o resultado no campo
     * `txtQuaCiv`. 5. Trata erros para evitar problemas na execução.
     */
    private void civ_sem_quantida1() {
        situacao = "Aberta";

        String sql = "select count(*) from form_civil_semestral where iduser = ?  and situacao_prev = ? ";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, situacao);
            rs = pst.executeQuery();
            if (rs.next()) {
                txtQuaCiv.setText(rs.getString(1));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método `select_preventiva1`. Este método verifica o tipo de preventiva
     * selecionada (`txtTipPreIni`) e, com base nisso, exibe a tela
     * correspondente para iniciar a manutenção preventiva. Antes de abrir a
     * tela, o usuário recebe uma confirmação via `JOptionPane`.
     *
     * Funcionalidades: - **Identificação do Tipo de Preventiva**: - Obtém o
     * tipo de preventiva a partir do campo `txtTipPreIni`. - Compara o valor
     * com diferentes opções de preventivas mensais e trimestrais.
     *
     * - **Confirmação do Usuário**: - Exibe um `JOptionPane` perguntando se o
     * usuário deseja iniciar a preventiva. - Se o usuário confirmar
     * (`YES_OPTION`), a tela correspondente é aberta.
     *
     * - **Abertura da Tela de Preventiva**: - Cria uma instância da tela
     * correspondente (`TelaEletricaMen`, `TelaHidraulicaMen1`, etc.). - Define
     * a tela como visível (`setVisible(true)`). - Adiciona a tela ao
     * `getParent()`. - Chama o método específico para carregar os dados da
     * preventiva (`select_eletr_mensal1()`, etc.). - Fecha a tela atual
     * (`dispose()`).
     *
     * Fluxo do Método: 1. Obtém o tipo de preventiva. 2. Exibe uma caixa de
     * diálogo de confirmação. 3. Se confirmado, abre a tela correspondente e
     * carrega os dados. 4. Fecha a tela atual.
     */
    private void select_preventiva1() {

        String preventiva = null;
        preventiva = txtTipPreIni.getText();

        if (preventiva.equals("Eletrica - Mensal")) {
            int confirma = JOptionPane.showConfirmDialog(null, "Deseja Iniciar a Preventiva?", "Atenção", JOptionPane.YES_NO_OPTION);
            if (confirma == JOptionPane.YES_OPTION) {

                id_prev = txtIdPrevIni.getText();
                TelaEletricaMen prev_men = new TelaEletricaMen();
                prev_men.setVisible(true);
                getParent().add(prev_men);
                prev_men.select_eletr_mensal1();

                try {
                    prev_men.setMaximum(true);
                } catch (PropertyVetoException e) {
                    JOptionPane.showMessageDialog(null, e);
                }

                dispose();
            }
        }
        if (preventiva.equals("Hidráulica - Mensal")) {
            int confirma = JOptionPane.showConfirmDialog(null, "Deseja Iniciar a Preventiva?", "Atenção", JOptionPane.YES_NO_OPTION);
            if (confirma == JOptionPane.YES_OPTION) {

                id_prev1 = txtIdPrevIni.getText();
                TelaHidraulicaMen1 prev_men = new TelaHidraulicaMen1();
                prev_men.setVisible(true);
                getParent().add(prev_men);
                prev_men.select_hidra_mensal1();

                try {
                    prev_men.setMaximum(true);
                } catch (PropertyVetoException e) {
                    JOptionPane.showMessageDialog(null, e);
                }

                dispose();
            }
        }
        if (preventiva.equals("Civil - Mensal")) {
            int confirma = JOptionPane.showConfirmDialog(null, "Deseja Iniciar a Preventiva?", "Atenção", JOptionPane.YES_NO_OPTION);
            if (confirma == JOptionPane.YES_OPTION) {

                id_prev2 = txtIdPrevIni.getText();
                TelaCivilMen1 prev_men = new TelaCivilMen1();
                prev_men.setVisible(true);
                getParent().add(prev_men);
                prev_men.select_civil_mensal1();

                try {
                    prev_men.setMaximum(true);
                } catch (PropertyVetoException e) {
                    JOptionPane.showMessageDialog(null, e);
                }

                dispose();
            }

        }
        if (preventiva.equals("Refrigeração - Mensal")) {
            int confirma = JOptionPane.showConfirmDialog(null, "Deseja Iniciar a Preventiva?", "Atenção", JOptionPane.YES_NO_OPTION);
            if (confirma == JOptionPane.YES_OPTION) {

                id_prev3 = txtIdPrevIni.getText();
                TelaRefrigeracaoMen1 prev_men = new TelaRefrigeracaoMen1();
                prev_men.setVisible(true);
                getParent().add(prev_men);
                prev_men.select_refri_mensal1();

                try {
                    prev_men.setMaximum(true);
                } catch (PropertyVetoException e) {
                    JOptionPane.showMessageDialog(null, e);
                }

                dispose();
            }
        }
        if (preventiva.equals("Eletrica - Trimestral")) {
            int confirma = JOptionPane.showConfirmDialog(null, "Deseja Iniciar a Preventiva?", "Atenção", JOptionPane.YES_NO_OPTION);
            if (confirma == JOptionPane.YES_OPTION) {

                id_prev4 = txtIdPrevIni.getText();
                TelaEletricaTrimestral prev_men = new TelaEletricaTrimestral();
                prev_men.setVisible(true);
                getParent().add(prev_men);
                prev_men.select_eletr_tri1();

                try {
                    prev_men.setMaximum(true);
                } catch (PropertyVetoException e) {
                    JOptionPane.showMessageDialog(null, e);
                }

                dispose();
            }
        }
        if (preventiva.equals("Hidráulica - Trimestral")) {
            int confirma = JOptionPane.showConfirmDialog(null, "Deseja Iniciar a Preventiva?", "Atenção", JOptionPane.YES_NO_OPTION);
            if (confirma == JOptionPane.YES_OPTION) {

                id_prev5 = txtIdPrevIni.getText();
                TelaHidraulicaTrimestral prev_men = new TelaHidraulicaTrimestral();
                prev_men.setVisible(true);
                getParent().add(prev_men);
                prev_men.select_hidr_trim1();

                try {
                    prev_men.setMaximum(true);
                } catch (PropertyVetoException e) {
                    JOptionPane.showMessageDialog(null, e);
                }

                dispose();
            }
        }
        if (preventiva.equals("Civil - Trimestral")) {
            int confirma = JOptionPane.showConfirmDialog(null, "Deseja Iniciar a Preventiva?", "Atenção", JOptionPane.YES_NO_OPTION);
            if (confirma == JOptionPane.YES_OPTION) {

                id_prev6 = txtIdPrevIni.getText();
                TelaCivilTrimestral prev_men = new TelaCivilTrimestral();
                prev_men.setVisible(true);
                getParent().add(prev_men);
                prev_men.select_civil_tri1();

                try {
                    prev_men.setMaximum(true);
                } catch (PropertyVetoException e) {
                    JOptionPane.showMessageDialog(null, e);
                }

                dispose();
            }
        }
        if (preventiva.equals("Refrigeração - Trimestral")) {
            int confirma = JOptionPane.showConfirmDialog(null, "Deseja Iniciar a Preventiva?", "Atenção", JOptionPane.YES_NO_OPTION);
            if (confirma == JOptionPane.YES_OPTION) {

                id_prev7 = txtIdPrevIni.getText();
                TelaRefrigeracaoTrimestral prev_men = new TelaRefrigeracaoTrimestral();
                prev_men.setVisible(true);
                getParent().add(prev_men);
                prev_men.select_refri_trimestral();

                try {
                    prev_men.setMaximum(true);
                } catch (PropertyVetoException e) {
                    JOptionPane.showMessageDialog(null, e);
                }

                dispose();
            }
        }
        if (preventiva.equals("Eletrica - Semestral")) {
            int confirma = JOptionPane.showConfirmDialog(null, "Deseja Iniciar a Preventiva?", "Atenção", JOptionPane.YES_NO_OPTION);
            if (confirma == JOptionPane.YES_OPTION) {

                id_prev8 = txtIdPrevIni.getText();
                TelaEletricaSem prev_men = new TelaEletricaSem();
                prev_men.setVisible(true);
                getParent().add(prev_men);
                prev_men.select_eletr_sem1();

                try {
                    prev_men.setMaximum(true);
                } catch (PropertyVetoException e) {
                    JOptionPane.showMessageDialog(null, e);
                }

                dispose();
            }
        }
        if (preventiva.equals("Hidráulica - Semestral")) {
            int confirma = JOptionPane.showConfirmDialog(null, "Deseja Iniciar a Preventiva?", "Atenção", JOptionPane.YES_NO_OPTION);
            if (confirma == JOptionPane.YES_OPTION) {

                id_prev10 = txtIdPrevIni.getText();
                TelaHidraulicaSem prev_men = new TelaHidraulicaSem();
                prev_men.setVisible(true);
                getParent().add(prev_men);
                prev_men.select_hidra_sem2();

                try {
                    prev_men.setMaximum(true);
                } catch (PropertyVetoException e) {
                    JOptionPane.showMessageDialog(null, e);
                }

                dispose();
            }
        }
        if (preventiva.equals("Civil - Semestral")) {
            int confirma = JOptionPane.showConfirmDialog(null, "Deseja Iniciar a Preventiva?", "Atenção", JOptionPane.YES_NO_OPTION);
            if (confirma == JOptionPane.YES_OPTION) {

                id_prev9 = txtIdPrevIni.getText();
                TelaCivilSem prev_men = new TelaCivilSem();
                prev_men.setVisible(true);
                getParent().add(prev_men);
                prev_men.select_civ_sem1();

                try {
                    prev_men.setMaximum(true);
                } catch (PropertyVetoException e) {
                    JOptionPane.showMessageDialog(null, e);
                }

                dispose();
            }

        }
        if (preventiva.equals("Refrigeração - Semestral")) {
            int confirma = JOptionPane.showConfirmDialog(null, "Deseja Iniciar a Preventiva?", "Atenção", JOptionPane.YES_NO_OPTION);
            if (confirma == JOptionPane.YES_OPTION) {

                id_prev11 = txtIdPrevIni.getText();
                TelaRefrigeracaoSem prev_men = new TelaRefrigeracaoSem();
                prev_men.setVisible(true);
                getParent().add(prev_men);
                prev_men.select_ref_sem1();

                try {
                    prev_men.setMaximum(true);
                } catch (PropertyVetoException e) {
                    JOptionPane.showMessageDialog(null, e);
                }

                dispose();
            }
        }

    }

    /**
     * Método `botão`. Este método ajusta a configuração de um botão
     * (`btnAltPrev`) com base no nível de acesso do usuário (`tip`). Dependendo
     * do tipo de usuário, o botão pode ser ativado ou os campos de texto podem
     * ser limpos.
     *
     * Funcionalidades: - **Verificação do Tipo de Usuário**: - Se `tip` for
     * `"Superadmin"` ou `"Admin"`, o botão `btnAltPrev` é ativado
     * (`setEnabled(true)`). - Se `tip` for `"User"`, os campos `txtIdPrevAlt` e
     * `txtTipPreAlt` são limpos (`setText(null)`).
     *
     * - **Controle de Interface**: - Garante que apenas usuários com permissões
     * adequadas possam interagir com `btnAltPrev`. - Reseta os campos de
     * entrada para usuários sem permissão de alteração.
     *
     * Fluxo do Método: 1. Verifica o tipo de usuário (`tip`). 2. Se for
     * `"Superadmin"` ou `"Admin"`, ativa o botão `btnAltPrev`. 3. Se for
     * `"User"`, limpa os campos `txtIdPrevAlt` e `txtTipPreAlt`.
     */
    private void botão() {

        if (tip.equals("Superadmin")) {
            btnAltPrev.setEnabled(true);
        }
        if (tip.equals("Admin")) {
            btnAltPrev.setEnabled(true);
        }
        if (tip.equals("User")) {
            txtIdPrevAlt.setText(null);
            txtTipPreAlt.setText(null);

        }
    }

    /**
     * Método `limpar`. Este método redefine os campos de entrada e desativa
     * botões na interface gráfica, garantindo que os dados anteriores sejam
     * apagados. Ele é útil para limpar a tela antes de uma nova operação ou
     * após o cancelamento de uma ação.
     *
     * Funcionalidades: - **Limpeza de Campos de Texto**: - Define os campos de
     * pesquisa (`txtPesqEle`, `txtPesqHid`, `txtPesqCiv`, `txtPesqRef`) como
     * `null`, removendo qualquer entrada anterior. - Limpa os identificadores e
     * tipos de preventiva (`txtIdPrevIni`, `txtIdPrevAlt`, `txtTipPreIni`,
     * `txtTipPreAlt`).
     *
     * - **Desativação de Botões**: - Desativa os botões `btnAltPrev` e
     * `btnIniPrev` (`setEnabled(false)`), impedindo interações até que novos
     * dados sejam inseridos.
     *
     * Fluxo do Método: 1. Define todos os campos de entrada como `null`. 2.
     * Desativa os botões de ação.
     */
    private void limpar() {
        txtPesqEle.setText(null);
        txtPesqHid.setText(null);
        txtPesqCiv.setText(null);
        txtPesqRef.setText(null);
        txtIdPrevIni.setText(null);
        txtIdPrevAlt.setText(null);
        txtTipPreIni.setText(null);
        txtIdPrevAlt.setText(null);
        txtTipPreAlt.setText(null);
        btnAltPrev.setEnabled(false);
        btnIniPrev.setEnabled(false);

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
        btnAltPrev = new javax.swing.JButton();
        btnIniPrev = new javax.swing.JButton();
        txtIdPrevAlt = new javax.swing.JTextField();
        txtIdPrevIni = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtTipPreIni = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtTipPreAlt = new javax.swing.JTextField();
        rbnPreMen = new javax.swing.JRadioButton();
        rbnPreTri = new javax.swing.JRadioButton();
        rbnPreSem = new javax.swing.JRadioButton();
        jLabel10 = new javax.swing.JLabel();
        rbnPreLis = new javax.swing.JRadioButton();
        rbnPreAbe = new javax.swing.JRadioButton();
        jLabel11 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txtQuaRef = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        txtQuaEle = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        txtQuaCiv = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        txtQuaHid = new javax.swing.JTextField();
        lblPreEnc = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Preventivas");
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

        btnAltPrev.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/48757_document_edit_edit_document.png"))); // NOI18N
        btnAltPrev.setToolTipText("Alterar Preventiva");
        btnAltPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAltPrevActionPerformed(evt);
            }
        });
        getContentPane().add(btnAltPrev);
        btnAltPrev.setBounds(550, 530, 64, 64);

        btnIniPrev.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/48755_add_document_add_document.png"))); // NOI18N
        btnIniPrev.setToolTipText("Iniciar Preventiva");
        btnIniPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIniPrevActionPerformed(evt);
            }
        });
        getContentPane().add(btnIniPrev);
        btnIniPrev.setBounds(740, 530, 64, 64);

        txtIdPrevAlt.setEditable(false);
        txtIdPrevAlt.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(txtIdPrevAlt);
        txtIdPrevAlt.setBounds(640, 530, 70, 28);

        txtIdPrevIni.setEditable(false);
        txtIdPrevIni.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(txtIdPrevIni);
        txtIdPrevIni.setBounds(830, 530, 70, 28);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 51));
        jLabel6.setText("N° Preventiva");
        getContentPane().add(jLabel6);
        jLabel6.setBounds(826, 514, 80, 20);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 51));
        jLabel7.setText("N°");
        getContentPane().add(jLabel7);
        jLabel7.setBounds(46, 468, 16, 20);

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 51));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Tipo Preventiva");
        getContentPane().add(jLabel8);
        jLabel8.setBounds(820, 560, 90, 14);

        txtTipPreIni.setEditable(false);
        txtTipPreIni.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        txtTipPreIni.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(txtTipPreIni);
        txtTipPreIni.setBounds(808, 574, 120, 28);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 51));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Tipo Preventiva");
        getContentPane().add(jLabel9);
        jLabel9.setBounds(630, 560, 90, 14);

        txtTipPreAlt.setEditable(false);
        txtTipPreAlt.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        txtTipPreAlt.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(txtTipPreAlt);
        txtTipPreAlt.setBounds(616, 574, 120, 28);

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

        buttonGroup2.add(rbnPreLis);
        rbnPreLis.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        rbnPreLis.setText("Lista de Preventivas");
        rbnPreLis.setOpaque(false);
        rbnPreLis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbnPreLisActionPerformed(evt);
            }
        });
        getContentPane().add(rbnPreLis);
        rbnPreLis.setBounds(52, 108, 140, 21);

        buttonGroup2.add(rbnPreAbe);
        rbnPreAbe.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        rbnPreAbe.setText("Preventivas em Aberto");
        rbnPreAbe.setOpaque(false);
        rbnPreAbe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbnPreAbeActionPerformed(evt);
            }
        });
        getContentPane().add(rbnPreAbe);
        rbnPreAbe.setBounds(52, 132, 142, 21);

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 0, 51));
        jLabel11.setText("N° Preventiva");
        getContentPane().add(jLabel11);
        jLabel11.setBounds(634, 514, 78, 20);

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 0, 51));
        jLabel13.setText("N°");
        getContentPane().add(jLabel13);
        jLabel13.setBounds(46, 301, 16, 20);

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 0, 51));
        jLabel14.setText("N°");
        getContentPane().add(jLabel14);
        jLabel14.setBounds(46, 385, 16, 20);

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(0, 0, 51));
        jLabel15.setText("N°");
        getContentPane().add(jLabel15);
        jLabel15.setBounds(46, 220, 16, 20);

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(0, 0, 51));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("Refrigeração");
        getContentPane().add(jLabel16);
        jLabel16.setBounds(120, 564, 120, 22);

        txtQuaRef.setEditable(false);
        txtQuaRef.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtQuaRef.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(txtQuaRef);
        txtQuaRef.setBounds(190, 590, 60, 26);

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(0, 0, 51));
        jLabel17.setText("Quantidade");
        getContentPane().add(jLabel17);
        jLabel17.setBounds(108, 590, 82, 26);

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(0, 0, 51));
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("Eletrica");
        getContentPane().add(jLabel18);
        jLabel18.setBounds(140, 506, 80, 22);

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(0, 0, 51));
        jLabel19.setText("Quantidade");
        getContentPane().add(jLabel19);
        jLabel19.setBounds(108, 532, 82, 26);

        txtQuaEle.setEditable(false);
        txtQuaEle.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtQuaEle.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(txtQuaEle);
        txtQuaEle.setBounds(190, 532, 60, 26);

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(0, 0, 51));
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("Civil");
        getContentPane().add(jLabel20);
        jLabel20.setBounds(330, 564, 100, 22);

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(0, 0, 51));
        jLabel21.setText("Quantidade");
        getContentPane().add(jLabel21);
        jLabel21.setBounds(310, 590, 82, 26);

        txtQuaCiv.setEditable(false);
        txtQuaCiv.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtQuaCiv.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(txtQuaCiv);
        txtQuaCiv.setBounds(390, 590, 60, 26);

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(0, 0, 51));
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("Hidráulica");
        getContentPane().add(jLabel22);
        jLabel22.setBounds(330, 506, 100, 22);

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(0, 0, 51));
        jLabel23.setText("Quantidade");
        getContentPane().add(jLabel23);
        jLabel23.setBounds(308, 532, 82, 26);

        txtQuaHid.setEditable(false);
        txtQuaHid.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtQuaHid.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(txtQuaHid);
        txtQuaHid.setBounds(390, 532, 60, 26);

        lblPreEnc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/telas/imagens_fundo/lista de preventivas.png"))); // NOI18N
        getContentPane().add(lblPreEnc);
        lblPreEnc.setBounds(-10, -8, 1008, 672);

        setBounds(0, 0, 1007, 672);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Método `btnAltPrevActionPerformed`. Este método é acionado quando o botão
     * `btnAltPrev` é pressionado. Ele abre a tela `TelaDisPreventiva`,
     * adiciona-a ao contêiner pai e executa a pesquisa de preventivas. Caso
     * ocorra um erro ao maximizar a janela, uma mensagem de erro é exibida ao
     * usuário.
     *
     * Funcionalidades: - **Criação e Exibição da Tela**: - Instancia
     * `TelaDisPreventiva` e define sua visibilidade (`setVisible(true)`). -
     * Adiciona a tela ao contêiner pai (`getParent().add(dis)`).
     *
     * - **Execução de Pesquisa**: - Chama `pesquisar_pre()` para carregar os
     * dados necessários na tela.
     *
     * - **Maximização da Janela**: - Tenta definir a tela como maximizada
     * (`setMaximum(true)`). - Caso ocorra um erro (`PropertyVetoException`),
     * exibe uma mensagem de erro via `JOptionPane`.
     *
     * - **Fechamento da Tela Atual**: - Chama `dispose()` para fechar a tela
     * atual após abrir `TelaDisPreventiva`.
     *
     * Fluxo do Método: 1. Cria e exibe a tela `TelaDisPreventiva`. 2. Adiciona
     * a tela ao contêiner pai. 3. Executa a pesquisa de preventivas. 4. Tenta
     * maximizar a tela e trata possíveis erros. 5. Fecha a tela atual.
     */
    private void btnAltPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAltPrevActionPerformed

        TelaDisPreventiva dis = new TelaDisPreventiva();
        dis.setVisible(true);
        getParent().add(dis);
        dis.pesquisar_pre();
        try {
            dis.setMaximum(true);
        } catch (PropertyVetoException e) {
            JOptionPane.showMessageDialog(null, e);
        }
        dispose();
    }//GEN-LAST:event_btnAltPrevActionPerformed

    /**
     * Método `btnIniPrevActionPerformed`. Este método é acionado quando o botão
     * `btnIniPrev` é pressionado. Ele verifica o valor da variável `t` e
     * executa a ação correspondente, chamando métodos específicos para lidar
     * com preventivas listadas ou iniciadas.
     *
     * Funcionalidades: - **Verificação do Estado da Preventiva**: - Se `t` for
     * `"Listas"`, chama `select_preventiva()`, que provavelmente lida com
     * preventivas disponíveis para seleção. - Se `t` for `"Iniciadas"`, chama
     * `select_preventiva1()`, que pode lidar com preventivas já em andamento.
     *
     * Fluxo do Método: 1. Verifica o valor da variável `t`. 2. Se for
     * `"Listas"`, executa `select_preventiva()`. 3. Se for `"Iniciadas"`,
     * executa `select_preventiva1()`.
     */
    private void btnIniPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIniPrevActionPerformed

        if (t.equals("Listas")) {
            select_preventiva();
        }
        if (t.equals("Iniciadas")) {
            select_preventiva1();
        }
    }//GEN-LAST:event_btnIniPrevActionPerformed

    /**
     * Método `tblPreRefMouseClicked`. Este método é acionado quando o usuário
     * clica em uma linha da tabela `tblPreRef`. Ele chama o método
     * `setar_ref()`, que provavelmente preenche campos da interface com os
     * dados da linha selecionada.
     *
     * Funcionalidades: - **Detecção de Clique do Usuário**: - O evento
     * `MouseEvent` (`evt`) captura o clique do usuário na tabela `tblPreRef`.
     *
     * - **Chamada do Método `setar_ref()`**: - Executa `setar_ref()`, que pode
     * recuperar os dados da linha clicada e exibi-los em campos de entrada.
     *
     * Fluxo do Método: 1. O usuário clica em uma linha da tabela `tblPreRef`.
     * 2. O evento `MouseEvent` é acionado. 3. O método `setar_ref()` é chamado
     * para processar os dados da linha selecionada.
     */
    private void tblPreRefMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPreRefMouseClicked

        setar_ref();
    }//GEN-LAST:event_tblPreRefMouseClicked

    /**
     * Método `tblPreEleMouseClicked`. Este método é acionado quando o usuário
     * clica em uma linha da tabela `tblPreEle`. Ele chama o método
     * `setar_ele()`, que provavelmente preenche campos da interface com os
     * dados da linha selecionada.
     *
     * Funcionalidades: - **Detecção de Clique do Usuário**: - O evento
     * `MouseEvent` (`evt`) captura o clique do usuário na tabela `tblPreEle`.
     *
     * - **Chamada do Método `setar_ele()`**: - Executa `setar_ele()`, que pode
     * recuperar os dados da linha clicada e exibi-los em campos de entrada.
     *
     * Fluxo do Método: 1. O usuário clica em uma linha da tabela `tblPreEle`.
     * 2. O evento `MouseEvent` é acionado. 3. O método `setar_ele()` é chamado
     * para processar os dados da linha selecionada.
     */
    private void tblPreEleMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPreEleMouseClicked

        setar_ele();
    }//GEN-LAST:event_tblPreEleMouseClicked

    /**
     * Método `tblPreCivilMouseClicked`. Este método é acionado quando o usuário
     * clica em uma linha da tabela `tblPreCivil`. Ele chama o método
     * `setar_civ()`, que provavelmente preenche campos da interface com os
     * dados da linha selecionada.
     *
     * Funcionalidades: - **Detecção de Clique do Usuário**: - O evento
     * `MouseEvent` (`evt`) captura o clique do usuário na tabela `tblPreCivil`.
     *
     * - **Chamada do Método `setar_civ()`**: - Executa `setar_civ()`, que pode
     * recuperar os dados da linha clicada e exibi-los em campos de entrada.
     *
     * Fluxo do Método: 1. O usuário clica em uma linha da tabela `tblPreCivil`.
     * 2. O evento `MouseEvent` é acionado. 3. O método `setar_civ()` é chamado
     * para processar os dados da linha selecionada.
     */
    private void tblPreCivilMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPreCivilMouseClicked

        setar_civ();
    }//GEN-LAST:event_tblPreCivilMouseClicked

    /**
     * Método `tblPreHidMouseClicked`. Este método é acionado quando o usuário
     * clica em uma linha da tabela `tblPreHid`. Ele chama o método
     * `setar_hid()`, que provavelmente preenche campos da interface com os
     * dados da linha selecionada.
     *
     * Funcionalidades: - **Detecção de Clique do Usuário**: - O evento
     * `MouseEvent` (`evt`) captura o clique do usuário na tabela `tblPreHid`.
     *
     * - **Chamada do Método `setar_hid()`**: - Executa `setar_hid()`, que pode
     * recuperar os dados da linha clicada e exibi-los em campos de entrada.
     *
     * Fluxo do Método: 1. O usuário clica em uma linha da tabela `tblPreHid`.
     * 2. O evento `MouseEvent` é acionado. 3. O método `setar_hid()` é chamado
     * para processar os dados da linha selecionada.
     */
    private void tblPreHidMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPreHidMouseClicked

        setar_hid();
    }//GEN-LAST:event_tblPreHidMouseClicked

    /**
     * Método `rbnPreMenActionPerformed`. Este método é acionado quando o botão
     * de opção referente à preventiva **Mensal** (`rbnPreMen`) é selecionado.
     * Ele define a variável `si` como `"Mensal"` e, com base no valor da
     * variável `t`, executa ações específicas para carregar preventivas
     * encerradas ou iniciadas.
     *
     * Funcionalidades: - **Definição do Tipo de Preventiva**: - A variável `si`
     * é definida como `"Mensal"`, indicando o tipo de preventiva selecionada.
     *
     * - **Verificação do Estado da Preventiva (`t`)**: - Se `t` for `"Listas"`,
     * chama o método `selec_encerrad()`, que provavelmente carrega preventivas
     * encerradas. - Se `t` for `"Iniciadas"`, chama `selec_aberta()`, que deve
     * carregar preventivas em andamento.
     *
     * Fluxo do Método: 1. Define o tipo de preventiva como `"Mensal"`. 2.
     * Verifica o valor da variável `t`. 3. Executa o método correspondente ao
     * estado da preventiva.
     */
    private void rbnPreMenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbnPreMenActionPerformed

        si = "Mensal";
        if (t.equals("Listas")) {
            selec_encerrad();
        }
        if (t.equals("Iniciadas")) {
            selec_aberta();
        }

    }//GEN-LAST:event_rbnPreMenActionPerformed

    /**
     * Método `rbnPreTriActionPerformed`. Este método é acionado quando o botão
     * de opção referente à preventiva **Trimestral** (`rbnPreTri`) é
     * selecionado. Ele define a variável `si` como `"Trimestral"` e, com base
     * no valor da variável `t`, executa ações específicas para carregar
     * preventivas encerradas ou iniciadas.
     *
     * Funcionalidades: - **Definição do Tipo de Preventiva**: - A variável `si`
     * é definida como `"Trimestral"`, indicando o tipo de preventiva
     * selecionada.
     *
     * - **Verificação do Estado da Preventiva (`t`)**: - Se `t` for `"Listas"`,
     * chama o método `selec_encerrad()`, que provavelmente carrega preventivas
     * encerradas. - Se `t` for `"Iniciadas"`, chama `selec_aberta()`, que deve
     * carregar preventivas em andamento.
     *
     * Fluxo do Método: 1. Define o tipo de preventiva como `"Trimestral"`. 2.
     * Verifica o valor da variável `t`. 3. Executa o método correspondente ao
     * estado da preventiva.
     */
    private void rbnPreTriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbnPreTriActionPerformed

        si = "Trimestral";
        if (t.equals("Listas")) {
            selec_encerrad();
        }
        if (t.equals("Iniciadas")) {
            selec_aberta();
        }
    }//GEN-LAST:event_rbnPreTriActionPerformed

    /**
     * Método `rbnPreSemActionPerformed`. Este método é acionado quando o botão
     * de opção referente à preventiva **Semestral** (`rbnPreSem`) é
     * selecionado. Ele define a variável `si` como `"Semestral"` e, com base no
     * valor da variável `t`, executa ações específicas para carregar
     * preventivas encerradas ou iniciadas.
     *
     * Funcionalidades: - **Definição do Tipo de Preventiva**: - A variável `si`
     * é definida como `"Semestral"`, indicando o tipo de preventiva
     * selecionada.
     *
     * - **Verificação do Estado da Preventiva (`t`)**: - Se `t` for `"Listas"`,
     * chama o método `selec_encerrad()`, que provavelmente carrega preventivas
     * encerradas. - Se `t` for `"Iniciadas"`, chama `selec_aberta()`, que deve
     * carregar preventivas em andamento.
     *
     * Fluxo do Método: 1. Define o tipo de preventiva como `"Semestral"`. 2.
     * Verifica o valor da variável `t`. 3. Executa o método correspondente ao
     * estado da preventiva.
     */
    private void rbnPreSemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbnPreSemActionPerformed

        si = "Semestral";
        if (t.equals("Listas")) {
            selec_encerrad();
        }
        if (t.equals("Iniciadas")) {
            selec_aberta();
        }
    }//GEN-LAST:event_rbnPreSemActionPerformed

    /**
     * Método `formInternalFrameOpened`. Este método é executado automaticamente
     * quando o `JInternalFrame` é aberto. Ele inicializa o estado da interface,
     * definindo seleções padrão e desabilitando botões de ação até que uma
     * interação do usuário ocorra.
     *
     * Funcionalidades: - **Seleção de Opções Padrão**: - Marca os botões de
     * opção `rbnPreMen` (Mensal) e `rbnPreLis` (Listas) como selecionados. -
     * Define as variáveis `si` e `t` com os valores correspondentes: `"Mensal"`
     * e `"Listas"`.
     *
     * - **Carregamento Inicial de Dados**: - Chama o método `selec_encerrad()`,
     * que provavelmente carrega as preventivas encerradas do tipo mensal.
     *
     * - **Desabilitação de Botões**: - Desativa os botões `btnAltPrev` e
     * `btnIniPrev`, impedindo ações até que uma seleção válida seja feita.
     *
     * Fluxo do Método: 1. Define os botões de opção padrão como selecionados.
     * 2. Inicializa as variáveis de controle de tipo e estado. 3. Carrega os
     * dados iniciais com `selec_encerrad()`. 4. Desativa os botões de ação para
     * evitar interações prematuras.
     */
    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened

        rbnPreMen.setSelected(true);
        rbnPreLis.setSelected(true);
        si = "Mensal";
        t = "Listas";
        selec_encerrad();
        btnAltPrev.setEnabled(false);
        btnIniPrev.setEnabled(false);
        tblPreEle.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblPreHid.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblPreCivil.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblPreRef.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }//GEN-LAST:event_formInternalFrameOpened

    /**
     * Método `txtPesqEleKeyReleased`. Este método é acionado sempre que uma
     * tecla é liberada no campo de texto `txtPesqEle`. Ele verifica o conteúdo
     * digitado e, com base no tipo (`si`) e estado (`t`) da preventiva, executa
     * o método apropriado para listar ou pesquisar preventivas elétricas.
     *
     * Funcionalidades: - **Captura da Entrada do Usuário**: - Obtém o texto
     * digitado no campo `txtPesqEle` e armazena na variável `v`.
     *
     * - **Verificação de Estado (`t`)**: - Se `t` for `"Listas"`, o sistema
     * está exibindo preventivas encerradas. - Se `t` for `"Iniciadas"`, o
     * sistema está exibindo preventivas em andamento.
     *
     * - **Verificação de Tipo (`si`)**: - Pode ser `"Mensal"`, `"Trimestral"`
     * ou `"Semestral"`. - Para cada tipo, há dois métodos: um para listar todas
     * (`ele_men()`, `ele_tri()`, etc.) e outro para pesquisar (`ele_men_pes()`,
     * `ele_tri_pes()`, etc.).
     *
     * - **Lógica de Pesquisa**: - Se o campo estiver vazio (`v.equals("")`),
     * exibe todas as preventivas do tipo e estado. - Caso contrário, executa a
     * pesquisa com base no texto digitado.
     *
     * - **Tratamento de Erros**: - Envolve toda a lógica em um `try-catch` para
     * capturar e exibir exceções inesperadas.
     *
     * Fluxo do Método: 1. Captura o texto digitado. 2. Verifica o estado (`t`)
     * e o tipo (`si`) da preventiva. 3. Executa o método correspondente para
     * listar ou pesquisar. 4. Trata possíveis exceções.
     */
    private void txtPesqEleKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesqEleKeyReleased

        String v;
        v = txtPesqEle.getText();
        try {
            if (t.equals("Listas")) {
                if (si.equals("Mensal")) {
                    if (v.equals("")) {
                        ele_men();
                    } else {
                        ele_men_pes();
                    }
                }
                if (si.equals("Trimestral")) {
                    if (v.equals("")) {
                        ele_tri();
                    } else {
                        ele_tri_pes();
                    }
                }
                if (si.equals("Semestral")) {
                    if (v.equals("")) {
                        ele_sem();
                    } else {
                        ele_sem_pes();
                    }
                }
            }
            if (t.equals("Iniciadas")) {
                if (si.equals("Mensal")) {
                    if (v.equals("")) {
                        ele_men_ini();
                    } else {
                        ele_men_pes1();
                    }
                }
                if (si.equals("Trimestral")) {
                    if (v.equals("")) {
                        ele_tri_ini();
                    } else {
                        ele_tri_pes1();
                    }
                }
                if (si.equals("Semestral")) {
                    if (v.equals("")) {
                        ele_sem_ini();
                    } else {
                        ele_sem_pes1();
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_txtPesqEleKeyReleased

    /**
     * Método `txtPesqHidKeyReleased`. Este método é acionado sempre que uma
     * tecla é liberada no campo de texto `txtPesqHid`. Ele verifica o conteúdo
     * digitado e, com base no tipo (`si`) e estado (`t`) da preventiva
     * hidráulica, executa o método apropriado para listar ou pesquisar os
     * registros.
     *
     * Funcionalidades: - **Captura da Entrada do Usuário**: - Armazena o texto
     * digitado no campo `txtPesqHid` na variável `v`.
     *
     * - **Verificação de Estado (`t`)**: - `"Listas"`: indica que o usuário
     * está visualizando preventivas encerradas. - `"Iniciadas"`: indica que o
     * usuário está visualizando preventivas em andamento.
     *
     * - **Verificação de Tipo (`si`)**: - Pode ser `"Mensal"`, `"Trimestral"`
     * ou `"Semestral"`. - Para cada tipo e estado, há dois métodos: - Um para
     * listar todos os registros (`hid_men()`, `hid_tri()`, `hid_sem()` etc.). -
     * Outro para realizar pesquisa com base no texto digitado (`hid_men_pes()`,
     * `hid_tri_pes()` etc.).
     *
     * - **Lógica de Pesquisa**: - Se o campo estiver vazio, exibe todos os
     * registros do tipo e estado atual. - Caso contrário, executa a pesquisa
     * com base no texto digitado.
     *
     * - **Tratamento de Erros**: - Envolve toda a lógica em um `try-catch` para
     * capturar e exibir exceções inesperadas.
     *
     * Fluxo do Método: 1. Captura o texto digitado. 2. Verifica o estado (`t`)
     * e o tipo (`si`) da preventiva. 3. Executa o método correspondente para
     * listar ou pesquisar. 4. Trata possíveis exceções.
     */
    private void txtPesqHidKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesqHidKeyReleased

        String v;
        v = txtPesqHid.getText();

        try {
            if (t.equals("Listas")) {
                if (si.equals("Mensal")) {
                    if (v.equals("")) {
                        hid_men();
                    } else {
                        hid_men_pes();
                    }
                }
                if (si.equals("Trimestral")) {
                    if (v.equals("")) {
                        hid_tri();
                    } else {
                        hid_tri_pes();
                    }
                }
                if (si.equals("Semestral")) {
                    if (v.equals("")) {
                        hid_sem();
                    } else {
                        hid_sem_pes();
                    }
                }
            }
            if (t.equals("Iniciadas")) {
                if (si.equals("Mensal")) {
                    if (v.equals("")) {
                        hid_men_ini();
                    } else {
                        hid_men_pes1();
                    }
                }
                if (si.equals("Trimestral")) {
                    if (v.equals("")) {
                        hid_tri_ini();
                    } else {
                        hid_tri_pes1();
                    }
                }
                if (si.equals("Semestral")) {
                    if (v.equals("")) {
                        hid_sem_ini();
                    } else {
                        hid_sem_pes1();
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_txtPesqHidKeyReleased

    /**
     * Método `txtPesqRefKeyReleased`. Este método é acionado sempre que uma
     * tecla é liberada no campo de texto `txtPesqRef`, utilizado para pesquisar
     * preventivas de refrigeração. Ele verifica o conteúdo digitado e, com base
     * no tipo (`si`) e estado (`t`) da preventiva, executa o método apropriado
     * para listar ou filtrar os registros.
     *
     * Funcionalidades: - **Captura da Entrada do Usuário**: - Armazena o texto
     * digitado no campo `txtPesqRef` na variável `v`.
     *
     * - **Verificação de Estado (`t`)**: - `"Listas"`: indica que o usuário
     * está visualizando preventivas encerradas. - `"Iniciadas"`: indica que o
     * usuário está visualizando preventivas em andamento.
     *
     * - **Verificação de Tipo (`si`)**: - Pode ser `"Mensal"`, `"Trimestral"`
     * ou `"Semestral"`. - Para cada tipo e estado, há dois métodos: - Um para
     * listar todos os registros (`ref_men()`, `ref_tri()`, `ref_sem()` etc.). -
     * Outro para realizar pesquisa com base no texto digitado (`ref_men_pes()`,
     * `ref_tri_pes()` etc.).
     *
     * - **Lógica de Pesquisa**: - Se o campo estiver vazio, exibe todos os
     * registros do tipo e estado atual. - Caso contrário, executa a pesquisa
     * com base no texto digitado.
     *
     * - **Tratamento de Erros**: - Envolve toda a lógica em um `try-catch` para
     * capturar e exibir exceções inesperadas.
     *
     * Fluxo do Método: 1. Captura o texto digitado. 2. Verifica o estado (`t`)
     * e o tipo (`si`) da preventiva. 3. Executa o método correspondente para
     * listar ou pesquisar. 4. Trata possíveis exceções.
     */
    private void txtPesqRefKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesqRefKeyReleased

        String v;
        v = txtPesqRef.getText();
        try {
            if (t.equals("Listas")) {
                if (si.equals("Mensal")) {
                    if (v.equals("")) {
                        ref_men();
                    } else {
                        ref_men_pes();
                    }
                }
                if (si.equals("Trimestral")) {
                    if (v.equals("")) {
                        ref_tri();
                    } else {
                        ref_tri_pes();
                    }
                }
                if (si.equals("Semestral")) {
                    if (v.equals("")) {
                        ref_sem();
                    } else {
                        ref_sem_pes();
                    }
                }
            }
            if (t.equals("Iniciadas")) {
                if (si.equals("Mensal")) {
                    if (v.equals("")) {
                        ref_men_ini();
                    } else {
                        ref_men_pes1();
                    }
                }
                if (si.equals("Trimestral")) {
                    if (v.equals("")) {
                        ref_tri_ini();
                    } else {
                        ref_tri_pes1();
                    }
                }
                if (si.equals("Semestral")) {
                    if (v.equals("")) {
                        ref_sem_ini();
                    } else {
                        ref_sem_pes1();
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_txtPesqRefKeyReleased

    /**
     * Método `txtPesqCivKeyReleased`. Acionado ao liberar uma tecla no campo de
     * pesquisa `txtPesqCiv`, este método determina qual função de listagem ou
     * pesquisa deve ser executada com base no tipo (`si`) e estado (`t`) da
     * preventiva civil.
     *
     * Funcionalidades: - Captura o texto digitado no campo `txtPesqCiv`. -
     * Verifica se o campo está vazio: - Se estiver, chama o método de listagem
     * completo. - Caso contrário, chama o método de pesquisa correspondente. -
     * Considera os três tipos de preventiva: Mensal, Trimestral e Semestral. -
     * Considera os dois estados: "Listas" (encerradas) e "Iniciadas". - Envolve
     * a lógica em um bloco `try-catch` para capturar exceções.
     */
    private void txtPesqCivKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesqCivKeyReleased

        String v;
        v = txtPesqCiv.getText();
        try {
            if (t.equals("Listas")) {
                if (si.equals("Mensal")) {
                    if (v.equals("")) {
                        civ_men();
                    } else {
                        civ_men_pes();
                    }
                }
                if (si.equals("Trimestral")) {
                    if (v.equals("")) {
                        civ_tri();
                    } else {
                        civ_tri_pes();
                    }
                }
                if (si.equals("Semestral")) {
                    if (v.equals("")) {
                        civ_sem();
                    } else {
                        civ_sem_pes();
                    }
                }
            }
            if (t.equals("Iniciadas")) {
                if (si.equals("Mensal")) {
                    if (v.equals("")) {
                        civ_men_ini();
                    } else {
                        civ_men_pes1();
                    }
                }
                if (si.equals("Trimestral")) {
                    if (v.equals("")) {
                        civ_tri_ini();
                    } else {
                        civ_tri_pes1();
                    }
                }
                if (si.equals("Semestral")) {
                    if (v.equals("")) {
                        civ_sem_ini();
                    } else {
                        civ_sem_pes1();
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_txtPesqCivKeyReleased

    /**
     * Método `rbnPreLisActionPerformed`. Acionado ao selecionar o botão de
     * opção "Listas", este método configura a interface para exibir preventivas
     * encerradas do tipo mensal.
     *
     * Funcionalidades: - Define `si` como `"Mensal"` e `t` como `"Listas"`. -
     * Marca o botão de opção `rbnPreMen` como selecionado. - Chama o método
     * `inicializacao()` para preparar a interface. - Executa `selec_encerrad()`
     * para carregar as preventivas encerradas.
     */
    private void rbnPreLisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbnPreLisActionPerformed

        si = "Mensal";
        t = "Listas";
        rbnPreMen.setSelected(true);
        inicializacao();

        if (t.equals("Listas")) {
            selec_encerrad();
        }
        if (t.equals("Iniciadas")) {
            selec_aberta();
        }
    }//GEN-LAST:event_rbnPreLisActionPerformed

    /**
     * Método `rbnPreAbeActionPerformed`. Acionado ao selecionar o botão de
     * opção "Iniciadas", este método configura a interface para exibir
     * preventivas em andamento do tipo mensal.
     *
     * Funcionalidades: - Define `si` como `"Mensal"` e `t` como `"Iniciadas"`.
     * - Marca o botão de opção `rbnPreMen` como selecionado. - Chama o método
     * `inicializacao()` para preparar a interface. - Executa `selec_aberta()`
     * para carregar as preventivas em andamento.
     */
    private void rbnPreAbeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbnPreAbeActionPerformed

        si = "Mensal";
        t = "Iniciadas";
        rbnPreMen.setSelected(true);
        inicializacao();
        if (t.equals("Listas")) {
            selec_encerrad();
        }
        if (t.equals("Iniciadas")) {
            selec_aberta();
        }
    }//GEN-LAST:event_rbnPreAbeActionPerformed

    /**
     * Método `txtPesqCivKeyReleased`. Acionado ao liberar uma tecla no campo de
     * pesquisa `txtPesqCiv`, este método determina qual função de listagem ou
     * pesquisa deve ser executada com base no tipo (`si`) e estado (`t`) da
     * preventiva civil.
     *
     * Funcionalidades: - Captura o texto digitado no campo `txtPesqCiv`. -
     * Verifica se o campo está vazio: - Se estiver, chama o método de listagem
     * completo. - Caso contrário, chama o método de pesquisa correspondente. -
     * Considera os três tipos de preventiva: Mensal, Trimestral e Semestral. -
     * Considera os dois estados: "Listas" (encerradas) e "Iniciadas". - Envolve
     * a lógica em um bloco `try-catch` para capturar exceções.
     */
    private void txtPesqEleKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesqEleKeyTyped
        // filtro que só permite a inclusão de letras minusculas e numeros e
        //caracteres especiais na caixa de texto:
        String caracter = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789 ";
        if (!caracter.contains(evt.getKeyChar() + "")) {
            evt.consume();
//            Toolkit.getDefaultToolkit().beep();
        }
    }//GEN-LAST:event_txtPesqEleKeyTyped

    private void txtPesqHidKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesqHidKeyTyped

        // filtro que só permite a inclusão de letras minusculas e numeros e
        //caracteres especiais na caixa de texto:
        String caracter = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789 ";
        if (!caracter.contains(evt.getKeyChar() + "")) {
            evt.consume();
//            Toolkit.getDefaultToolkit().beep();
        }
    }//GEN-LAST:event_txtPesqHidKeyTyped

    private void txtPesqRefKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesqRefKeyTyped

        // filtro que só permite a inclusão de letras minusculas e numeros e
        //caracteres especiais na caixa de texto:
        String caracter = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789 ";
        if (!caracter.contains(evt.getKeyChar() + "")) {
            evt.consume();
//            Toolkit.getDefaultToolkit().beep();
        }
    }//GEN-LAST:event_txtPesqRefKeyTyped

    /**
     * Método `txtPesqCivKeyTyped`. Este método é acionado sempre que uma tecla
     * é digitada no campo de texto `txtPesqCiv`. Ele atua como um filtro de
     * entrada, permitindo apenas letras (maiúsculas e minúsculas), números e
     * espaço. Qualquer outro caractere digitado é ignorado (consumido),
     * impedindo sua inserção no campo.
     *
     * Funcionalidades: - **Definição de Caracteres Permitidos**: - A string
     * `caracter` contém todos os caracteres aceitos: letras de A a Z
     * (maiúsculas e minúsculas), números de 0 a 9 e espaço.
     *
     * - **Validação do Caractere Digitado**: - O método `getKeyChar()` obtém o
     * caractere digitado. - Se o caractere não estiver contido na string
     * `caracter`, o evento é consumido com `evt.consume()`, impedindo sua
     * propagação.
     *
     * - **Comportamento Opcional (Comentado)**: - Há uma linha comentada com
     * `Toolkit.getDefaultToolkit().beep();` que, se ativada, emitiria um som ao
     * tentar digitar um caractere inválido.
     *
     * Fluxo do Método: 1. Define os caracteres válidos. 2. Verifica se o
     * caractere digitado está na lista. 3. Se não estiver, consome o evento
     * para impedir a entrada.
     */
    private void txtPesqCivKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesqCivKeyTyped

        // filtro que só permite a inclusão de letras minusculas e numeros e
        //caracteres especiais na caixa de texto:
        String caracter = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789 ";
        if (!caracter.contains(evt.getKeyChar() + "")) {
            evt.consume();
//            Toolkit.getDefaultToolkit().beep();
        }
    }//GEN-LAST:event_txtPesqCivKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAltPrev;
    private javax.swing.JButton btnIniPrev;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
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
    private javax.swing.JRadioButton rbnPreAbe;
    private javax.swing.JRadioButton rbnPreLis;
    private javax.swing.JRadioButton rbnPreMen;
    private javax.swing.JRadioButton rbnPreSem;
    private javax.swing.JRadioButton rbnPreTri;
    public static javax.swing.JTable tblPreCivil;
    public static javax.swing.JTable tblPreEle;
    public static javax.swing.JTable tblPreHid;
    public static javax.swing.JTable tblPreRef;
    private javax.swing.JTextField txtIdPrevAlt;
    private javax.swing.JTextField txtIdPrevIni;
    private javax.swing.JTextField txtPesqCiv;
    private javax.swing.JTextField txtPesqEle;
    private javax.swing.JTextField txtPesqHid;
    private javax.swing.JTextField txtPesqRef;
    private javax.swing.JTextField txtQuaCiv;
    private javax.swing.JTextField txtQuaEle;
    private javax.swing.JTextField txtQuaHid;
    private javax.swing.JTextField txtQuaRef;
    private javax.swing.JTextField txtTipPreAlt;
    private javax.swing.JTextField txtTipPreIni;
    // End of variables declaration//GEN-END:variables
}
