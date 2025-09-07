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
import static com.prjmanutencao.telas.TelaWebCam1.nome_foto;
import java.sql.*;
import static com.prjmanutencao.telas.TelaWebCam1.tipo_prev;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

/**
 * Mostra a preventiva de Hidráulica Mensal e a opções de anotação e fotos.
 *
 * @author Fábio S. Oliveira
 * @version 1.1
 */
public class TelaHidraulicaMen1 extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    String situacao = null;
    String situacao1 = null;
    String situacao2 = null;
    String situacao3 = null;
    String situacao4 = null;
    String situacao5 = null;
    String situacao6 = null;
    String situacao7 = null;
    String situacao8 = null;
    String situacao9 = null;
    String situacao10 = null;
    String situacao11 = null;
    String situacao12 = null;
    String situacao13 = null;
    String situacao14 = null;
    String situacao15 = null;
    String situacao16 = null;
    String situacao17 = null;
    String situacao18 = null;
    String situacao19 = null;
    String situacao20 = null;
    String situacao21 = null;
    String situacao22 = null;
    String situacao23 = null;
    String situacao24 = null;
    String situacao25 = null;
    String situacao26 = null;

    public static String nome1 = null;
    public static String id_prev1 = null;
    public static String foto_ele1 = null;

    public static String acesso = ("127.0.0.1");
    String log = "servidor";
    String sen = "12345";
    String dire = "hidra_mensal";
    String data = null;

    /**
     * Construtor da classe TelaHidraulicaMen1
     *
     * Inicializa os componentes da interface gráfica e configura o estado
     * inicial da tela. - Estabelece a conexão com o banco de dados através de
     * ModuloConexao.conector(). - Oculta todos os campos de anotação (txpAAnot1
     * a txpAAnot27) ao iniciar. - Oculta o painel jPanel1, utilizado para
     * exibição de imagens. - Define o valor do campo txtIdPreEle com o conteúdo
     * da variável id_prev1.
     */
    public TelaHidraulicaMen1() {
        initComponents();
        conexao = ModuloConexao.conector();
        txpAAnot1.setVisible(false);
        txpAAnot2.setVisible(false);
        txpAAnot3.setVisible(false);
        txpAAnot4.setVisible(false);
        txpAAnot5.setVisible(false);
        txpAAnot5.setVisible(false);
        txpAAnot6.setVisible(false);
        txpAAnot7.setVisible(false);
        txpAAnot8.setVisible(false);
        txpAAnot9.setVisible(false);
        txpAAnot10.setVisible(false);
        txpAAnot11.setVisible(false);
        txpAAnot12.setVisible(false);
        txpAAnot13.setVisible(false);
        txpAAnot14.setVisible(false);
        txpAAnot15.setVisible(false);
        txpAAnot16.setVisible(false);
        txpAAnot17.setVisible(false);
        txpAAnot18.setVisible(false);
        txpAAnot19.setVisible(false);
        txpAAnot20.setVisible(false);
        txpAAnot21.setVisible(false);
        txpAAnot22.setVisible(false);
        txpAAnot23.setVisible(false);
        txpAAnot24.setVisible(false);
        txpAAnot25.setVisible(false);
        txpAAnot26.setVisible(false);
        txpAAnot27.setVisible(false);

        jPanel1.setVisible(false);
        txtIdPreEle1.setText(id_prev1);
        jScrollPane1.getVerticalScrollBar().setUnitIncrement(25);
    }

    /**
     * Método select_hidraulica_mensal
     *
     * Responsável por buscar os dados da preventiva hidráulica mensal no banco
     * de dados com base no identificador id_prev1. Os dados recuperados são
     * utilizados para preencher os campos da interface gráfica com as
     * informações correspondentes.
     *
     * Campos preenchidos: - txtDatIni1: Data/hora de início da preventiva -
     * txtDatFin: Data/hora de término da preventiva - txtTemPre: Tempo total da
     * preventiva - txtDatHor: Tempo disponível - txtNomTec: Nome do técnico
     * responsável - txtSetPre: Setor da preventiva - txtEquPr: Nome do
     * equipamento - txtCodEquPre: Código do equipamento - tipo_prev: Tipo da
     * preventiva (armazenado em variável)
     *
     * Em caso de erro na execução da consulta, exibe uma mensagem de erro via
     * JOptionPane.
     */
    public void select_hidraulica_mensal() {

        String sql = "select id_hidraulica_mensal, tempo_ini_prev, tempo_fin_prev, tempo_total_prev, tempo_dis, nome_prev, setor_prev, nome_equi_set, cod_equi_set, tipo_prev from form_hidraulica_mensal where id_hidraulica_mensal = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id_prev1);
            rs = pst.executeQuery();
            if (rs.next()) {
                txtDatIni1.setText(rs.getString(2));
                txtDatFin.setText(rs.getString(3));
                txtTemPre.setText(rs.getString(4));
                txtDatHor.setText(rs.getString(5));
                txtNomTec.setText(rs.getString(6));
                txtSetPre.setText(rs.getString(7));
                txtEquPre.setText(rs.getString(8));
                txtCodEquPre.setText(rs.getString(9));
                tipo_prev = (rs.getString(10));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método foto_prev
     *
     * Responsável por salvar no banco de dados o registro da imagem associada à
     * preventiva hidráulica trimestral. - Recupera o nome do arquivo da imagem
     * a partir do campo txtFotPre1. - Monta o caminho completo da imagem
     * utilizando a variável acesso. - Insere os dados na tabela
     * arquivos_hidraulica_mensal com os campos: nome_arq (nome do arquivo),
     * endere_arq (caminho completo), id_hidraulica_mensal (identificador da
     * preventiva). - Caso os campos obrigatórios estejam vazios, exibe uma
     * mensagem de alerta. - Se a inserção for bem-sucedida, chama o método
     * upload_file() e exibe mensagem de sucesso. - Em caso de erro na execução
     * da consulta, exibe uma mensagem de erro via JOptionPane.
     */
    private void foto_prev() {
        String prev = null;
        prev = txtFotPre1.getText();
        String sql = "insert into arquivos_hidraulica_mensal(nome_arq, endere_arq, id_hidraulica_mensal) values (?, ?, ?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtFotPre1.getText());
            pst.setString(2, acesso + prev);
            pst.setString(3, txtIdPreEle1.getText());
            if ((txtFotPre1.getText().isEmpty()) || (txtIdPreEle1.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha os Campos Obrigatórios!");
            } else {
                int Adicionar = pst.executeUpdate();

                if (Adicionar > 0) {
                    upload_file();
                    JOptionPane.showMessageDialog(null, "Imagem Salva!");
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Trecho de código para verificação de duplicidade de arquivo no servidor
     * FTP
     *
     * Este bloco realiza a verificação se já existe um arquivo com o mesmo nome
     * no diretório correspondente à preventiva hidráulica semestral no servidor
     * FTP.
     *
     * Etapas: - Recupera o ID da preventiva e o nome do arquivo a partir dos
     * campos txtIdPreEle e txtFotPre10. - Estabelece conexão com o servidor FTP
     * usando as credenciais e endereço definidos. - Navega até o diretório
     * correspondente ao ID da preventiva. - Lista os arquivos presentes no
     * diretório e verifica se algum possui o mesmo nome do arquivo informado. -
     * Caso exista, exibe uma mensagem informando que o arquivo já está salvo. -
     * Em caso de erro de conexão ou leitura, exibe a exceção capturada via
     * JOptionPane.
     *
     * Observação: a variável `igual` é usada como flag para indicar se houve
     * correspondência de nome.
     */
    private void verificarArquivo() {

        String idPrev, nomeArq;
        idPrev = txtIdPreEle1.getText();
        nomeArq = txtFotPre1.getText();
        boolean igual = false;
        try {

            FTPClient ftp = new FTPClient();
            //Fazendo a conexão
            ftp.connect(acesso);
            //Efetuando o Login
            ftp.login(log, sen);
            //Mundando o diretório de trabalho
            ftp.changeWorkingDirectory(dire + "/" + idPrev);
            //Adiquirindo o nome dos arquivos / diretórios existentes
            String[] arq = ftp.listNames();
//            System.out.println("Listando arquivos: \n");
            for (String f : arq) {
                if (f.equals(nomeArq)) {
                    igual = true;
                }
            }

            if (igual == true) {
                JOptionPane.showMessageDialog(null, "Já Existe um Arquivo com este Nome Salvo");

            }
        } catch (HeadlessException | IOException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método upload_file
     *
     * Responsável por realizar o upload de um arquivo para um servidor FTP. O
     * processo segue os seguintes passos:
     *
     * 1. Obtém o ID da preventiva a partir do campo txtIdPreEle. 2. Obtém o
     * caminho do arquivo a partir do campo txtFotPre10. 3. Conecta ao servidor
     * FTP utilizando as credenciais fornecidas. 4. Navega até o diretório
     * especificado e cria um subdiretório com o ID da preventiva. 5. Entra em
     * modo passivo e define o tipo de arquivo como binário. 6. Abre o arquivo
     * foto_ele1 e tenta armazená-lo no servidor com o nome especificado em
     * `dir`. 7. Se o upload for bem-sucedido, chama o método limpar_foto();
     * caso contrário, exibe uma mensagem de erro. 8. Em caso de exceção, exibe
     * a mensagem de erro em um JOptionPane.
     *
     */
    private void upload_file() {
        String idPrev;
        idPrev = txtIdPreEle1.getText();
        lblFotEltMen1.getIcon();

        String dir;
        dir = txtFotPre1.getText();
        FTPClient ftp = new FTPClient();
        try {

            ftp.connect(acesso);

            ftp.login(log, sen);

            ftp.makeDirectory(dire);
            ftp.changeWorkingDirectory(dire);
            ftp.makeDirectory(idPrev);
            ftp.changeWorkingDirectory(idPrev);
            ftp.enterLocalPassiveMode(); // Modo passivo
            ftp.setFileType(FTP.BINARY_FILE_TYPE);

            try (FileInputStream arqEnviar = new FileInputStream(foto_ele1)) {
                if (ftp.storeFile(dir, arqEnviar)) {
                    limpar_foto();
                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao Armazenar o Arquivo.");
                }
                arqEnviar.close();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    /**
     * Método prev_ini
     *
     * Responsável por iniciar uma nova preventiva hidráulica mensal. - Define o
     * status da preventiva como "Aberta" e atualiza o campo txtStaPre. -
     * Executa uma atualização no banco de dados, preenchendo os campos: -
     * tempo_ini_prev: com o valor de txtDatIni1 - situacao_prev: com o valor
     * "Aberta" - id_hidraulica_mensal: com o valor de txtIdPreEle - Se a
     * atualização for bem-sucedida, exibe uma mensagem de confirmação. - Caso
     * contrário, exibe uma mensagem de erro. - Em caso de exceção
     * (HeadlessException ou SQLException), exibe o erro capturado via
     * JOptionPane.
     */
    public void prev_ini() {

        String status = "Aberta";
        txtStaPre.setText(status);

        String sql = "update form_hidraulica_mensal set tempo_ini_prev = ?,  situacao_prev = ?  where id_hidraulica_mensal = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtDatIni1.getText());
            pst.setString(2, status);
            pst.setString(3, txtIdPreEle1.getText());
            int alterar1 = pst.executeUpdate();
            if (alterar1 > 0) {
                JOptionPane.showMessageDialog(null, "Preventiva Iniciada!");

            } else {
                JOptionPane.showMessageDialog(null, "Erro ao Iniciar Preventiva");
            }

        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Método iniciar_prev
     *
     * Finaliza ou atualiza os dados da preventiva hidráulica mensal no banco de
     * dados.
     *
     * Funcionalidades: - Atualiza os campos: - tempo_fin_prev: data/hora de
     * término da preventiva - tempo_total_prev: tempo total da preventiva -
     * situacao_prev: status atual da preventiva (ex: "Encerrada" ou "Aberta") -
     * a1 a a27: situação de conformidade de cada item (ex: "Conforme", "Não
     * Conforme") - obser1 a obser30: observações associadas a cada item -
     * Utiliza o ID da preventiva (txtIdPreEle) como chave para atualização. -
     * Exibe mensagens de sucesso ou erro conforme o resultado da operação. -
     * Fecha a conexão com o banco de dados e encerra a janela atual com
     * dispose().
     *
     * Observação: - Se o status for "Encerrada", exibe mensagem específica de
     * encerramento. - Em caso de falha na atualização, exibe mensagem de erro e
     * também encerra a janela.
     *
     * @throws SQLException em caso de erro na execução da consulta SQL
     */
    private void iniciar_prev() {
        String status = txtStaPre.getText();

        String sql = "update form_hidraulica_mensal set tempo_fin_prev = ?, tempo_total_prev = ?, situacao_prev = ?, a1 = ?, obser1 = ?, a2 = ?, obser2 = ?, a3 = ?, obser3 = ?, a4 = ?, obser4 = ?, a5 = ?, obser5 = ?, a6 = ?, obser6 = ?, a7 = ?, obser7 = ?, a8 = ?, obser8 = ?, a9 = ?, obser9 = ?, a10 = ?, obser10 = ?, a11 = ?, obser11 = ?, a12 = ?, obser12 = ?, a13 = ?, obser13 = ?, a14 = ?, obser14 = ?, a15 = ?, obser15 = ?, a16 = ?, obser16 = ?, a17 = ?, obser17 = ?, a18 = ?, obser18 = ?, a19 = ?, obser19 = ?, a20 = ?, obser20 = ?, a21 = ?, obser21 = ?, a22 = ?, obser22 = ?,a23 = ?, obser23 = ?, a24 = ?, obser24 = ?, a25 = ?, obser25 = ?, a26 = ?, obser26 = ?, a27 = ?, obser27 = ? where id_hidraulica_mensal = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtDatFin.getText());
            pst.setString(2, txtTemPre.getText());
            pst.setString(3, txtStaPre.getText());

            pst.setString(4, situacao);
            pst.setString(5, txtaA1.getText());

            pst.setString(6, situacao1);
            pst.setString(7, txtaA2.getText());

            pst.setString(8, situacao2);
            pst.setString(9, txtaA3.getText());

            pst.setString(10, situacao3);
            pst.setString(11, txtaA4.getText());

            pst.setString(12, situacao4);
            pst.setString(13, txtaA5.getText());

            pst.setString(14, situacao5);
            pst.setString(15, txtaA6.getText());

            pst.setString(16, situacao6);
            pst.setString(17, txtaA7.getText());

            pst.setString(18, situacao7);
            pst.setString(19, txtaA8.getText());

            pst.setString(20, situacao8);
            pst.setString(21, txtaA9.getText());

            pst.setString(22, situacao9);
            pst.setString(23, txtaA10.getText());

            pst.setString(24, situacao10);
            pst.setString(25, txtaA11.getText());

            pst.setString(26, situacao11);
            pst.setString(27, txtaA12.getText());

            pst.setString(28, situacao12);
            pst.setString(29, txtaA13.getText());

            pst.setString(30, situacao13);
            pst.setString(31, txtaA14.getText());

            pst.setString(32, situacao14);
            pst.setString(33, txtaA15.getText());

            pst.setString(34, situacao15);
            pst.setString(35, txtaA16.getText());

            pst.setString(36, situacao16);
            pst.setString(37, txtaA17.getText());

            pst.setString(38, situacao17);
            pst.setString(39, txtaA18.getText());

            pst.setString(40, situacao18);
            pst.setString(41, txtaA19.getText());

            pst.setString(42, situacao19);
            pst.setString(43, txtaA20.getText());

            pst.setString(44, situacao20);
            pst.setString(45, txtaA21.getText());

            pst.setString(46, situacao21);
            pst.setString(47, txtaA22.getText());

            pst.setString(48, situacao22);
            pst.setString(49, txtaA23.getText());

            pst.setString(50, situacao23);
            pst.setString(51, txtaA24.getText());

            pst.setString(52, situacao24);
            pst.setString(53, txtaA25.getText());

            pst.setString(54, situacao25);
            pst.setString(55, txtaA26.getText());

            pst.setString(56, situacao26);
            pst.setString(57, txtaA27.getText());

            pst.setString(58, txtIdPreEle1.getText());
            int alterar1 = pst.executeUpdate();
            if (alterar1 > 0) {
                if (status.equals("Encerrada")) {
                    JOptionPane.showMessageDialog(null, "Preventiva Encerrada com Sucesso!");
                    conexao.close();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Dados Salvos!");
                    conexao.close();
                    dispose();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Erro ao Alterar os Dados!");
                conexao.close();
                dispose();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
            System.out.println(e);
        }

    }

    /**
     * Método select_arq
     *
     * Responsável por permitir ao usuário selecionar uma imagem JPEG a partir
     * do sistema de arquivos e exibi-la na interface gráfica, além de gerar um
     * nome único para o arquivo e habilitar o botão de salvamento.
     *
     * Funcionalidades: - Limpa o ícone atual do rótulo lblFotEltMen1 e
     * desabilita o botão btnSalFotPre1. - Gera um número aleatório entre 1 e
     * 300000 para compor o nome do arquivo. - Abre um seletor de arquivos
     * (JFileChooser) com filtro para arquivos JPEG, apontando para a área de
     * trabalho. - Após a seleção, define o caminho da imagem em foto_ele1 e
     * exibe no console. - Define o nome do arquivo no campo txtFotPre1 com base
     * no prefixo nome_foto e o número aleatório. - Chama o método
     * verificarArquivo() para checar duplicidade. - Redimensiona e exibe a
     * imagem selecionada no rótulo lblFotEltMen1. - Habilita o botão
     * btnSalFotPre1 para permitir o salvamento da imagem.
     *
     * Em caso de erro (HeadlessException), exibe a exceção via JOptionPane.
     */
    private void select_arq() {

        lblFotEltMen1.setIcon(null);
        btnSalFotPre1.setEnabled(false);

        Random aleatorio = new Random();
        int valor = aleatorio.nextInt(300000) + 1;

        try {

            JFileChooser file = new JFileChooser();
            FileNameExtensionFilter filtro = new FileNameExtensionFilter("Imagem em JPEG ", "jpg");
            file.setCurrentDirectory(new File(System.getProperty("user.home") + "\\Desktop"));
            file.setFileFilter(filtro);
            file.setDialogTitle("Carregar Foto");
            file.showOpenDialog(this);
            foto_ele1 = file.getSelectedFile().getPath();
            txtFotPre1.setText(nome_foto.concat(Integer.toString(valor)).concat(".jpg"));
            verificarArquivo();
            ImageIcon icon = new ImageIcon(foto_ele1);
            icon.setImage(icon.getImage().getScaledInstance(lblFotEltMen1.getWidth(), lblFotEltMen1.getHeight(), Image.SCALE_SMOOTH));
            lblFotEltMen1.setIcon(icon);
            btnSalFotPre1.setEnabled(true);

        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    /**
     * Método cauc_tempo
     *
     * Calcula o tempo total decorrido entre a data/hora de início e a data/hora
     * de término da preventiva, formatando o resultado como "DD - HH : mm : ss"
     * e exibindo no campo txtTemPre.
     *
     * Funcionalidades: - Recupera os valores de data/hora dos campos txtDatIni1
     * e txtDatFin. - Converte as strings para objetos Date utilizando o formato
     * "dd/MM/yyyy HH:mm:ss". - Calcula a diferença entre as datas em
     * milissegundos. - Converte a diferença para dias, horas, minutos e
     * segundos. - Formata o resultado com zeros à esquerda e atualiza o campo
     * txtTemPre.
     *
     * Em caso de erro de parsing (ParseException), exibe uma mensagem de erro
     * via JOptionPane.
     */
    private void cauc_tempo() {

        String dateStart = txtDatIni1.getText();
        String dateStop = txtDatFin.getText();

        //HH converts hour in 24 hours format (0-23), day calculation
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        java.util.Date d1 = null;
        java.util.Date d2 = null;

        try {
            d1 = format.parse(dateStart);
            d2 = format.parse(dateStop);

            //in milliseconds
            long diff = d2.getTime() - d1.getTime();

            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);
            txtTemPre.setText(((diffDays <= 9 ? "0" : "") + diffDays) + (" - ") + ((diffHours <= 9 ? "0" : "") + diffHours) + (" : ") + ((diffMinutes <= 9 ? "0" : "") + diffMinutes) + (" : ") + ((diffSeconds <= 9 ? "0" : "") + diffSeconds));

        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(rootPane, "Ocorreu um erro ao calcular as Horas Trabalhadas!", "Atenção", JOptionPane.ERROR_MESSAGE + ex.getErrorOffset());
        }

    }

    /**
     * Método limpar_foto
     *
     * Responsável por limpar os dados e a visualização da imagem carregada na
     * interface.
     *
     * Funcionalidades: - Oculta o painel jPanel1 que exibe a imagem. - Remove o
     * ícone exibido no rótulo lblFotEltMen5. - Limpa o campo de texto
     * txtFotPre10, que armazena o nome do arquivo da imagem. - Desativa o botão
     * btnSalFotPre5, impedindo o salvamento de uma imagem inexistente.
     */
    private void limpar_foto() {
        jPanel1.setVisible(false);
        lblFotEltMen1.setIcon(null);
        txtFotPre1.setText(null);
        btnSalFotPre1.setEnabled(false);
    }

    /**
     * Método select_hidra_mensal1
     *
     * Responsável por recuperar e preencher na interface os dados de uma
     * preventiva hidráulica trimestral com base no identificador id_prev5. Os
     * dados são extraídos da tabela form_hidraulica_semestral.
     *
     * Funcionalidades: - Executa uma consulta SQL para obter dados gerais da
     * preventiva (datas, técnico, equipamento, etc.). - Preenche os campos da
     * interface com os valores retornados do banco de dados. - Para cada item
     * (a1 a a30), define o botão de conformidade correspondente como
     * selecionado com base no valor ("Conforme" ou "Não Conforme") e preenche o
     * campo de observação associado.
     *
     * Em caso de erro na consulta, exibe a exceção capturada via JOptionPane.
     */
    public void select_hidra_mensal1() {

        String sql = "select id_hidraulica_mensal, tempo_ini_prev, tempo_fin_prev, tempo_total_prev, tempo_dis, situacao_prev, nome_prev, setor_prev, nome_equi_set, cod_equi_set, tipo_prev, a1, obser1,   a2, obser2,   a3, obser3,   a4, obser4,   a5, obser5,  a6, obser6,  a7, obser7,  a8, obser8,   a9, obser9,  a10, obser10,   a11, obser11, a12, obser12,  a13, obser13,  a14, obser14,   a15, obser15,  a16, obser16, a17, obser17,  a18, obser18,  a19, obser19, a20, obser20,  a21, obser21,   a22, obser22,   a23, obser23,  a24, obser24,   a25, obser25,  a26, obser26,  a27, obser27 from form_hidraulica_mensal where id_hidraulica_mensal = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id_prev1);
            rs = pst.executeQuery();
            if (rs.next()) {
                txtDatHor.setText(rs.getString(2));
                txtDatFin.setText(rs.getString(3));
                txtTemPre.setText(rs.getString(4));
                txtDatIni1.setText(rs.getString(5));
                txtStaPre.setText(rs.getString(6));
                txtNomTec.setText(rs.getString(7));
                txtSetPre.setText(rs.getString(8));
                txtEquPre.setText(rs.getString(9));
                txtCodEquPre.setText(rs.getString(10));
                tipo_prev = (rs.getString(11));

                String situ1 = (rs.getString(12));
                if (situ1.equals("Não Conforme")) {
                    rbtA1.setSelected(true);
                    situacao = "Não Conforme";
                } else {
                    rbtA11.setSelected(true);
                    situacao = "Conforme";
                }
                txtaA1.setText(rs.getString(13));

                String situ2 = (rs.getString(14));
                if (situ2.equals("Não Conforme")) {
                    rbtA2.setSelected(true);
                    situacao1 = "Não Conforme";
                } else {
                    rbtA12.setSelected(true);
                    situacao1 = "Conforme";
                };
                txtaA2.setText(rs.getString(15));
//
                String situ3 = (rs.getString(16));
                if (situ3.equals("Não Conforme")) {
                    rbtA3.setSelected(true);
                    situacao2 = "Não Conforme";
                } else {
                    rbtA13.setSelected(true);
                    situacao2 = "Conforme";
                }
                txtaA3.setText(rs.getString(17));
//
                String situ4 = (rs.getString(18));
                if (situ4.equals("Não Conforme")) {
                    rbtA4.setSelected(true);
                    situacao3 = "Não Conforme";
                } else {
                    rbtA14.setSelected(true);
                    situacao3 = "Conforme";
                }
                txtaA4.setText(rs.getString(19));
//
                String situ5 = (rs.getString(20));
                if (situ5.equals("Não Conforme")) {
                    rbtA5.setSelected(true);
                    situacao4 = "Não Conforme";
                } else {
                    rbtA15.setSelected(true);
                    situacao4 = "Conforme";
                }
                txtaA5.setText(rs.getString(21));
//
                String situ6 = (rs.getString(22));
                if (situ6.equals("Não Conforme")) {
                    rbtA6.setSelected(true);
                    situacao5 = "Não Conforme";
                } else {
                    rbtA16.setSelected(true);
                    situacao5 = "Conforme";
                }
                txtaA6.setText(rs.getString(23));
//
                String situ7 = (rs.getString(24));
                if (situ7.equals("Não Conforme")) {
                    rbtA7.setSelected(true);
                    situacao6 = "Não Conforme";
                } else {
                    rbtA17.setSelected(true);
                    situacao6 = "Conforme";
                }
                txtaA7.setText(rs.getString(25));
//
                String situ8 = (rs.getString(26));
                if (situ8.equals("Não Conforme")) {
                    rbtA8.setSelected(true);
                    situacao7 = "Não Conforme";
                } else {
                    rbtA18.setSelected(true);
                    situacao7 = "Conforme";
                }
                txtaA8.setText(rs.getString(27));
//
                String situ9 = (rs.getString(28));
                if (situ9.equals("Não Conforme")) {
                    rbtA9.setSelected(true);
                    situacao8 = "Não Conforme";
                } else {
                    rbtA19.setSelected(true);
                    situacao8 = "Conforme";
                }
                txtaA9.setText(rs.getString(29));
//
                String situ10 = (rs.getString(30));
                if (situ10.equals("Não Conforme")) {
                    rbtA10.setSelected(true);
                    situacao9 = "Não Conforme";
                } else {
                    rbtA20.setSelected(true);
                    situacao9 = "Conforme";
                }
                txtaA10.setText(rs.getString(31));
//
                String situ11 = (rs.getString(32));
                if (situ11.equals("Não Conforme")) {
                    rbtA21.setSelected(true);
                    situacao10 = "Não Conforme";
                } else {
                    rbtA31.setSelected(true);
                    situacao10 = "Conforme";
                }
                txtaA11.setText(rs.getString(33));
//
                String situ12 = (rs.getString(34));
                if (situ12.equals("Não Conforme")) {
                    rbtA22.setSelected(true);
                    situacao11 = "Não Conforme";
                } else {
                    rbtA32.setSelected(true);
                    situacao11 = "Conforme";
                }
                txtaA12.setText(rs.getString(35));
//
                String situ13 = (rs.getString(36));
                if (situ13.equals("Não Conforme")) {
                    rbtA23.setSelected(true);
                    situacao12 = "Não Conforme";
                } else {
                    rbtA33.setSelected(true);
                    situacao12 = "Conforme";
                }
                txtaA13.setText(rs.getString(37));
//
                String situ14 = (rs.getString(38));
                if (situ14.equals("Não Conforme")) {
                    rbtA24.setSelected(true);
                    situacao13 = "Não Conforme";
                } else {
                    rbtA34.setSelected(true);
                    situacao13 = "Conforme";
                }
                txtaA14.setText(rs.getString(39));
//
                String situ15 = (rs.getString(40));
                if (situ15.equals("Não Conforme")) {
                    rbtA25.setSelected(true);
                    situacao14 = "Não Conforme";
                } else {
                    rbtA35.setSelected(true);
                    situacao14 = "Conforme";
                }
                txtaA15.setText(rs.getString(41));
//
                String situ16 = (rs.getString(42));
                if (situ16.equals("Não Conforme")) {
                    rbtA26.setSelected(true);
                    situacao15 = "Não Conforme";
                } else {
                    rbtA36.setSelected(true);
                    situacao15 = "Conforme";
                }
                txtaA16.setText(rs.getString(43));
//
                String situ17 = (rs.getString(44));
                if (situ17.equals("Não Conforme")) {
                    rbtA27.setSelected(true);
                    situacao16 = "Não Conforme";
                } else {
                    rbtA37.setSelected(true);
                    situacao16 = "Conforme";
                }
                txtaA17.setText(rs.getString(45));
//
                String situ18 = (rs.getString(46));
                if (situ18.equals("Não Conforme")) {
                    rbtA28.setSelected(true);
                    situacao17 = "Não Conforme";
                } else {
                    rbtA38.setSelected(true);
                    situacao17 = "Conforme";
                }
                txtaA18.setText(rs.getString(47));
//
                String situ19 = (rs.getString(48));
                if (situ19.equals("Não Conforme")) {
                    rbtA29.setSelected(true);
                    situacao18 = "Não Conforme";
                } else {
                    rbtA39.setSelected(true);
                    situacao18 = "Conforme";
                }
                txtaA19.setText(rs.getString(49));
//
                String situ20 = (rs.getString(50));
                if (situ20.equals("Não Conforme")) {
                    rbtA30.setSelected(true);
                    situacao19 = "Não Conforme";
                } else {
                    rbtA40.setSelected(true);
                    situacao19 = "Conforme";
                }
                txtaA20.setText(rs.getString(51));
//
                String situ21 = (rs.getString(52));
                if (situ21.equals("Não Conforme")) {
                    rbtA41.setSelected(true);
                    situacao20 = "Não Conforme";
                } else {
                    rbtA51.setSelected(true);
                    situacao20 = "Conforme";
                }
                txtaA21.setText(rs.getString(53));
//
                String situ22 = (rs.getString(54));
                if (situ22.equals("Não Conforme")) {
                    rbtA42.setSelected(true);
                    situacao21 = "Não Conforme";
                } else {
                    rbtA52.setSelected(true);
                    situacao21 = "Conforme";
                }
                txtaA22.setText(rs.getString(55));
//
                String situ23 = (rs.getString(56));
                if (situ23.equals("Não Conforme")) {
                    rbtA43.setSelected(true);
                    situacao22 = "Não Conforme";
                } else {
                    rbtA53.setSelected(true);
                    situacao22 = "Conforme";
                }
                txtaA23.setText(rs.getString(57));
//
                String situ24 = (rs.getString(58));
                if (situ24.equals("Não Conforme")) {
                    rbtA44.setSelected(true);
                    situacao23 = "Não Conforme";
                } else {
                    rbtA54.setSelected(true);
                    situacao23 = "Conforme";
                }
                txtaA24.setText(rs.getString(59));
//
                String situ25 = (rs.getString(60));
                if (situ25.equals("Não Conforme")) {
                    rbtA45.setSelected(true);
                    situacao24 = "Não Conforme";
                } else {
                    rbtA55.setSelected(true);
                    situacao24 = "Conforme";
                }
                txtaA25.setText(rs.getString(61));
//
                String situ26 = (rs.getString(62));
                if (situ26.equals("Não Conforme")) {
                    rbtA46.setSelected(true);
                    situacao25 = "Não Conforme";
                } else {
                    rbtA56.setSelected(true);
                    situacao25 = "Conforme";
                }
                txtaA26.setText(rs.getString(63));
//
                String situ27 = (rs.getString(64));
                if (situ27.equals("Não Conforme")) {
                    rbtA47.setSelected(true);
                    situacao26 = "Não Conforme";
                } else {
                    rbtA57.setSelected(true);
                    situacao26 = "Conforme";
                }
                txtaA27.setText(rs.getString(65));

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * KeyAdapter validadorTeclado
     *
     * Adaptador de teclado personalizado para validar a entrada de texto em um
     * JTextArea.
     *
     * Funcionalidades: - Permite apenas caracteres alfanuméricos, vírgula,
     * parênteses e espaço. - Limita o texto a no máximo 200 caracteres. - Caso
     * a entrada viole essas regras, o evento é consumido (bloqueado). -
     * Opcionalmente, pode emitir um som ao bloquear a entrada (linha
     * comentada).
     *
     * Uso: - Adicione este `KeyAdapter` ao componente JTextArea com
     * `addKeyListener(validadorTeclado)`.
     */
    KeyAdapter validadorTeclado = new KeyAdapter() {
        @Override
        public void keyTyped(KeyEvent evt) {
            JTextArea campo = (JTextArea) evt.getSource();
            String textoAtual = campo.getText() + evt.getKeyChar();

            // Validação personalizada: caracteres permitidos e limite de 200
            if (textoAtual.length() > 200 || !String.valueOf(evt.getKeyChar()).matches("[a-zA-Z0-9.,()áÁàãÃÀéÉèÈêÊôÔõÕóÓòíÍìÌÒÇç ]")) {
                evt.consume();
//                Toolkit.getDefaultToolkit().beep(); // opcional: som ao bloquear
            }
        }
    };

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        grbA1 = new javax.swing.ButtonGroup();
        grbA2 = new javax.swing.ButtonGroup();
        grbA3 = new javax.swing.ButtonGroup();
        grbA4 = new javax.swing.ButtonGroup();
        grbA5 = new javax.swing.ButtonGroup();
        grbA6 = new javax.swing.ButtonGroup();
        grbA7 = new javax.swing.ButtonGroup();
        grbA8 = new javax.swing.ButtonGroup();
        grbA9 = new javax.swing.ButtonGroup();
        grbA10 = new javax.swing.ButtonGroup();
        grbA11 = new javax.swing.ButtonGroup();
        grbA12 = new javax.swing.ButtonGroup();
        grbA13 = new javax.swing.ButtonGroup();
        grbA14 = new javax.swing.ButtonGroup();
        grbA15 = new javax.swing.ButtonGroup();
        grbA16 = new javax.swing.ButtonGroup();
        grbA17 = new javax.swing.ButtonGroup();
        grbA18 = new javax.swing.ButtonGroup();
        grbA19 = new javax.swing.ButtonGroup();
        gbrA20 = new javax.swing.ButtonGroup();
        gbrA21 = new javax.swing.ButtonGroup();
        gbrA22 = new javax.swing.ButtonGroup();
        gbrA23 = new javax.swing.ButtonGroup();
        gbrA24 = new javax.swing.ButtonGroup();
        gbrA25 = new javax.swing.ButtonGroup();
        gbrA26 = new javax.swing.ButtonGroup();
        gbrA27 = new javax.swing.ButtonGroup();
        gbrA28 = new javax.swing.ButtonGroup();
        gbrA29 = new javax.swing.ButtonGroup();
        gbrA30 = new javax.swing.ButtonGroup();
        gbrA31 = new javax.swing.ButtonGroup();
        gbrA32 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        lblFotEltMen1 = new javax.swing.JLabel();
        txtFotPre1 = new javax.swing.JTextField();
        btnSalFotPre1 = new javax.swing.JButton();
        btnArqFotoPre = new javax.swing.JButton();
        txtCanFot = new javax.swing.JButton();
        btnCamPre = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        rbtA1 = new javax.swing.JRadioButton();
        rbtA11 = new javax.swing.JRadioButton();
        btnAnoA1 = new javax.swing.JToggleButton();
        btnFotoA1 = new javax.swing.JButton();
        txpAAnot1 = new javax.swing.JScrollPane();
        txtaA1 = new javax.swing.JTextArea();
        txtIdPreEle1 = new javax.swing.JTextField();
        txtDatIni1 = new javax.swing.JTextField();
        txtDatFin = new javax.swing.JTextField();
        txtTemPre = new javax.swing.JTextField();
        txtDatHor = new javax.swing.JTextField();
        txtNomTec = new javax.swing.JTextField();
        txtCodEquPre = new javax.swing.JTextField();
        txtStaPre = new javax.swing.JTextField();
        rbtA12 = new javax.swing.JRadioButton();
        rbtA2 = new javax.swing.JRadioButton();
        btnFotoA2 = new javax.swing.JButton();
        btnAnoA2 = new javax.swing.JToggleButton();
        txpAAnot2 = new javax.swing.JScrollPane();
        txtaA2 = new javax.swing.JTextArea();
        rbtA3 = new javax.swing.JRadioButton();
        rbtA13 = new javax.swing.JRadioButton();
        txpAAnot3 = new javax.swing.JScrollPane();
        txtaA3 = new javax.swing.JTextArea();
        btnAnoA3 = new javax.swing.JToggleButton();
        btnFotoA3 = new javax.swing.JButton();
        rbtA4 = new javax.swing.JRadioButton();
        txpAAnot4 = new javax.swing.JScrollPane();
        txtaA4 = new javax.swing.JTextArea();
        rbtA14 = new javax.swing.JRadioButton();
        btnAnoA4 = new javax.swing.JToggleButton();
        btnFotoA4 = new javax.swing.JButton();
        rbtA5 = new javax.swing.JRadioButton();
        txpAAnot5 = new javax.swing.JScrollPane();
        txtaA5 = new javax.swing.JTextArea();
        rbtA15 = new javax.swing.JRadioButton();
        btnAnoA5 = new javax.swing.JToggleButton();
        btnFotoA5 = new javax.swing.JButton();
        rbtA6 = new javax.swing.JRadioButton();
        txpAAnot6 = new javax.swing.JScrollPane();
        txtaA6 = new javax.swing.JTextArea();
        rbtA16 = new javax.swing.JRadioButton();
        btnAnoA6 = new javax.swing.JToggleButton();
        btnFotoA6 = new javax.swing.JButton();
        rbtA7 = new javax.swing.JRadioButton();
        txpAAnot7 = new javax.swing.JScrollPane();
        txtaA7 = new javax.swing.JTextArea();
        rbtA17 = new javax.swing.JRadioButton();
        btnAnoA7 = new javax.swing.JToggleButton();
        btnFotoA7 = new javax.swing.JButton();
        rbtA8 = new javax.swing.JRadioButton();
        txpAAnot8 = new javax.swing.JScrollPane();
        txtaA8 = new javax.swing.JTextArea();
        rbtA18 = new javax.swing.JRadioButton();
        btnAnoA8 = new javax.swing.JToggleButton();
        btnFotoA8 = new javax.swing.JButton();
        rbtA9 = new javax.swing.JRadioButton();
        txpAAnot9 = new javax.swing.JScrollPane();
        txtaA9 = new javax.swing.JTextArea();
        rbtA19 = new javax.swing.JRadioButton();
        btnAnoA9 = new javax.swing.JToggleButton();
        btnFotoA9 = new javax.swing.JButton();
        rbtA10 = new javax.swing.JRadioButton();
        txpAAnot10 = new javax.swing.JScrollPane();
        txtaA10 = new javax.swing.JTextArea();
        rbtA20 = new javax.swing.JRadioButton();
        btnAnoA10 = new javax.swing.JToggleButton();
        btnFotoA10 = new javax.swing.JButton();
        rbtA21 = new javax.swing.JRadioButton();
        rbtA31 = new javax.swing.JRadioButton();
        txpAAnot11 = new javax.swing.JScrollPane();
        txtaA11 = new javax.swing.JTextArea();
        btnAnoA11 = new javax.swing.JToggleButton();
        btnFotoA11 = new javax.swing.JButton();
        rbtA22 = new javax.swing.JRadioButton();
        txpAAnot12 = new javax.swing.JScrollPane();
        txtaA12 = new javax.swing.JTextArea();
        rbtA32 = new javax.swing.JRadioButton();
        btnAnoA12 = new javax.swing.JToggleButton();
        btnFotoA12 = new javax.swing.JButton();
        rbtA23 = new javax.swing.JRadioButton();
        txpAAnot13 = new javax.swing.JScrollPane();
        txtaA13 = new javax.swing.JTextArea();
        rbtA33 = new javax.swing.JRadioButton();
        btnAnoA13 = new javax.swing.JToggleButton();
        btnFotoA13 = new javax.swing.JButton();
        rbtA24 = new javax.swing.JRadioButton();
        txpAAnot14 = new javax.swing.JScrollPane();
        txtaA14 = new javax.swing.JTextArea();
        rbtA34 = new javax.swing.JRadioButton();
        btnAnoA14 = new javax.swing.JToggleButton();
        btnFotoA14 = new javax.swing.JButton();
        rbtA25 = new javax.swing.JRadioButton();
        txpAAnot15 = new javax.swing.JScrollPane();
        txtaA15 = new javax.swing.JTextArea();
        rbtA35 = new javax.swing.JRadioButton();
        btnAnoA15 = new javax.swing.JToggleButton();
        btnFotoA15 = new javax.swing.JButton();
        rbtA26 = new javax.swing.JRadioButton();
        txpAAnot16 = new javax.swing.JScrollPane();
        txtaA16 = new javax.swing.JTextArea();
        rbtA36 = new javax.swing.JRadioButton();
        btnAnoA16 = new javax.swing.JToggleButton();
        btnFotoA16 = new javax.swing.JButton();
        rbtA27 = new javax.swing.JRadioButton();
        rbtA37 = new javax.swing.JRadioButton();
        txpAAnot17 = new javax.swing.JScrollPane();
        txtaA17 = new javax.swing.JTextArea();
        btnAnoA17 = new javax.swing.JToggleButton();
        btnFotoA17 = new javax.swing.JButton();
        rbtA28 = new javax.swing.JRadioButton();
        txpAAnot18 = new javax.swing.JScrollPane();
        txtaA18 = new javax.swing.JTextArea();
        rbtA38 = new javax.swing.JRadioButton();
        btnAnoA18 = new javax.swing.JToggleButton();
        btnFotoA18 = new javax.swing.JButton();
        rbtA29 = new javax.swing.JRadioButton();
        txpAAnot19 = new javax.swing.JScrollPane();
        txtaA19 = new javax.swing.JTextArea();
        rbtA39 = new javax.swing.JRadioButton();
        btnAnoA19 = new javax.swing.JToggleButton();
        btnFotoA19 = new javax.swing.JButton();
        rbtA30 = new javax.swing.JRadioButton();
        txpAAnot20 = new javax.swing.JScrollPane();
        txtaA20 = new javax.swing.JTextArea();
        rbtA40 = new javax.swing.JRadioButton();
        btnAnoA20 = new javax.swing.JToggleButton();
        btnFotoA20 = new javax.swing.JButton();
        rbtA41 = new javax.swing.JRadioButton();
        txpAAnot21 = new javax.swing.JScrollPane();
        txtaA21 = new javax.swing.JTextArea();
        rbtA51 = new javax.swing.JRadioButton();
        btnAnoA21 = new javax.swing.JToggleButton();
        btnFotoA21 = new javax.swing.JButton();
        rbtA42 = new javax.swing.JRadioButton();
        txpAAnot22 = new javax.swing.JScrollPane();
        txtaA22 = new javax.swing.JTextArea();
        rbtA52 = new javax.swing.JRadioButton();
        btnAnoA22 = new javax.swing.JToggleButton();
        btnFotoA22 = new javax.swing.JButton();
        rbtA43 = new javax.swing.JRadioButton();
        txpAAnot23 = new javax.swing.JScrollPane();
        txtaA23 = new javax.swing.JTextArea();
        rbtA53 = new javax.swing.JRadioButton();
        btnAnoA23 = new javax.swing.JToggleButton();
        btnFotoA23 = new javax.swing.JButton();
        rbtA44 = new javax.swing.JRadioButton();
        txpAAnot24 = new javax.swing.JScrollPane();
        txtaA24 = new javax.swing.JTextArea();
        rbtA54 = new javax.swing.JRadioButton();
        btnAnoA24 = new javax.swing.JToggleButton();
        btnFotoA24 = new javax.swing.JButton();
        rbtA45 = new javax.swing.JRadioButton();
        txpAAnot25 = new javax.swing.JScrollPane();
        txtaA25 = new javax.swing.JTextArea();
        rbtA55 = new javax.swing.JRadioButton();
        btnAnoA25 = new javax.swing.JToggleButton();
        btnFotoA25 = new javax.swing.JButton();
        rbtA46 = new javax.swing.JRadioButton();
        txpAAnot26 = new javax.swing.JScrollPane();
        txtaA26 = new javax.swing.JTextArea();
        rbtA56 = new javax.swing.JRadioButton();
        btnAnoA26 = new javax.swing.JToggleButton();
        btnFotoA26 = new javax.swing.JButton();
        rbtA47 = new javax.swing.JRadioButton();
        txpAAnot27 = new javax.swing.JScrollPane();
        txtaA27 = new javax.swing.JTextArea();
        btnAnoA27 = new javax.swing.JToggleButton();
        btnFotoA27 = new javax.swing.JButton();
        rbtA57 = new javax.swing.JRadioButton();
        btnEncPre = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtSetPre = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtEquPre = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Hidráulica Mensal");
        setPreferredSize(new java.awt.Dimension(1007, 672));
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosing(evt);
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

        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(51, 51, 51), new java.awt.Color(51, 51, 51), new java.awt.Color(51, 51, 51), new java.awt.Color(51, 51, 51)));
        jPanel1.setLayout(null);

        lblFotEltMen1.setBackground(new java.awt.Color(153, 153, 153));
        lblFotEltMen1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(51, 51, 51), new java.awt.Color(51, 51, 51), new java.awt.Color(51, 51, 51), new java.awt.Color(51, 51, 51)));
        lblFotEltMen1.setOpaque(true);
        jPanel1.add(lblFotEltMen1);
        lblFotEltMen1.setBounds(15, 15, 240, 180);

        txtFotPre1.setEditable(false);
        txtFotPre1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        txtFotPre1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(txtFotPre1);
        txtFotPre1.setBounds(15, 210, 93, 26);

        btnSalFotPre1.setText("Salvar");
        btnSalFotPre1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 51, 51), new java.awt.Color(0, 51, 51), new java.awt.Color(0, 51, 51), new java.awt.Color(0, 51, 51)));
        btnSalFotPre1.setEnabled(false);
        btnSalFotPre1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalFotPre1ActionPerformed(evt);
            }
        });
        jPanel1.add(btnSalFotPre1);
        btnSalFotPre1.setBounds(114, 222, 75, 24);

        btnArqFotoPre.setText("Arquivo");
        btnArqFotoPre.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 51, 51), new java.awt.Color(0, 51, 51), new java.awt.Color(0, 51, 51), new java.awt.Color(0, 51, 51)));
        btnArqFotoPre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnArqFotoPreActionPerformed(evt);
            }
        });
        jPanel1.add(btnArqFotoPre);
        btnArqFotoPre.setBounds(188, 198, 75, 24);

        txtCanFot.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        txtCanFot.setText("Cancelar");
        txtCanFot.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 51, 51), new java.awt.Color(0, 51, 51), new java.awt.Color(0, 51, 51), new java.awt.Color(0, 51, 51)));
        txtCanFot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCanFotActionPerformed(evt);
            }
        });
        jPanel1.add(txtCanFot);
        txtCanFot.setBounds(188, 222, 75, 24);

        btnCamPre.setText("Camera");
        btnCamPre.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 51, 51), new java.awt.Color(0, 51, 51), new java.awt.Color(0, 51, 51), new java.awt.Color(0, 51, 51)));
        btnCamPre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCamPreActionPerformed(evt);
            }
        });
        jPanel1.add(btnCamPre);
        btnCamPre.setBounds(114, 198, 75, 24);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(390, 90, 270, 30);

        jScrollPane1.setAutoscrolls(true);
        jScrollPane1.setMinimumSize(new java.awt.Dimension(1010, 2321));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(1010, 2025));

        jPanel2.setMinimumSize(new java.awt.Dimension(1008, 3179));
        jPanel2.setPreferredSize(new java.awt.Dimension(1010, 2025));
        jPanel2.setLayout(null);

        rbtA1.setBackground(new java.awt.Color(255, 255, 255));
        grbA1.add(rbtA1);
        rbtA1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA1ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA1);
        rbtA1.setBounds(722, 392, 40, 40);

        rbtA11.setBackground(new java.awt.Color(255, 255, 255));
        grbA1.add(rbtA11);
        rbtA11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA11.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA11ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA11);
        rbtA11.setBounds(778, 392, 40, 40);

        btnAnoA1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnoA1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        btnAnoA1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnoA1ActionPerformed(evt);
            }
        });
        jPanel2.add(btnAnoA1);
        btnAnoA1.setBounds(836, 392, 40, 40);

        btnFotoA1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotoA1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoA1ActionPerformed(evt);
            }
        });
        jPanel2.add(btnFotoA1);
        btnFotoA1.setBounds(892, 392, 40, 40);

        txpAAnot1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        txtaA1.setColumns(20);
        txtaA1.setLineWrap(true);
        txtaA1.setRows(5);
        txpAAnot1.setViewportView(txtaA1);

        jPanel2.add(txpAAnot1);
        txpAAnot1.setBounds(770, 430, 40, 10);

        txtIdPreEle1.setEditable(false);
        txtIdPreEle1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel2.add(txtIdPreEle1);
        txtIdPreEle1.setBounds(70, 182, 100, 28);

        txtDatIni1.setEditable(false);
        txtDatIni1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel2.add(txtDatIni1);
        txtDatIni1.setBounds(200, 182, 170, 28);

        txtDatFin.setEditable(false);
        txtDatFin.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel2.add(txtDatFin);
        txtDatFin.setBounds(400, 182, 170, 28);

        txtTemPre.setEditable(false);
        txtTemPre.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel2.add(txtTemPre);
        txtTemPre.setBounds(600, 182, 170, 28);

        txtDatHor.setEditable(false);
        txtDatHor.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        txtDatHor.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel2.add(txtDatHor);
        txtDatHor.setBounds(800, 182, 140, 28);

        txtNomTec.setEditable(false);
        txtNomTec.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel2.add(txtNomTec);
        txtNomTec.setBounds(70, 243, 180, 28);

        txtCodEquPre.setEditable(false);
        txtCodEquPre.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel2.add(txtCodEquPre);
        txtCodEquPre.setBounds(700, 243, 100, 28);

        txtStaPre.setEditable(false);
        txtStaPre.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel2.add(txtStaPre);
        txtStaPre.setBounds(830, 243, 110, 28);

        rbtA12.setBackground(new java.awt.Color(255, 255, 255));
        grbA2.add(rbtA12);
        rbtA12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA12.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA12ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA12);
        rbtA12.setBounds(778, 444, 40, 40);

        rbtA2.setBackground(new java.awt.Color(255, 255, 255));
        grbA2.add(rbtA2);
        rbtA2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA2ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA2);
        rbtA2.setBounds(722, 444, 40, 40);

        btnFotoA2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotoA2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoA2ActionPerformed(evt);
            }
        });
        jPanel2.add(btnFotoA2);
        btnFotoA2.setBounds(892, 444, 40, 40);

        btnAnoA2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnoA2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        btnAnoA2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnoA2ActionPerformed(evt);
            }
        });
        jPanel2.add(btnAnoA2);
        btnAnoA2.setBounds(836, 444, 40, 40);

        txpAAnot2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        txtaA2.setColumns(20);
        txtaA2.setLineWrap(true);
        txtaA2.setRows(5);
        txpAAnot2.setViewportView(txtaA2);

        jPanel2.add(txpAAnot2);
        txpAAnot2.setBounds(770, 482, 40, 10);

        rbtA3.setBackground(new java.awt.Color(255, 255, 255));
        grbA3.add(rbtA3);
        rbtA3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA3ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA3);
        rbtA3.setBounds(722, 497, 40, 40);

        rbtA13.setBackground(new java.awt.Color(255, 255, 255));
        grbA3.add(rbtA13);
        rbtA13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA13.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA13ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA13);
        rbtA13.setBounds(778, 497, 40, 40);

        txpAAnot3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        txtaA3.setColumns(20);
        txtaA3.setLineWrap(true);
        txtaA3.setRows(5);
        txpAAnot3.setViewportView(txtaA3);

        jPanel2.add(txpAAnot3);
        txpAAnot3.setBounds(770, 535, 40, 10);

        btnAnoA3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnoA3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        btnAnoA3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnoA3ActionPerformed(evt);
            }
        });
        jPanel2.add(btnAnoA3);
        btnAnoA3.setBounds(836, 497, 40, 40);

        btnFotoA3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotoA3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoA3ActionPerformed(evt);
            }
        });
        jPanel2.add(btnFotoA3);
        btnFotoA3.setBounds(892, 497, 40, 40);

        rbtA4.setBackground(new java.awt.Color(255, 255, 255));
        grbA4.add(rbtA4);
        rbtA4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA4ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA4);
        rbtA4.setBounds(722, 550, 40, 40);

        txpAAnot4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        txtaA4.setColumns(20);
        txtaA4.setLineWrap(true);
        txtaA4.setRows(5);
        txpAAnot4.setViewportView(txtaA4);

        jPanel2.add(txpAAnot4);
        txpAAnot4.setBounds(770, 588, 40, 10);

        rbtA14.setBackground(new java.awt.Color(255, 255, 255));
        grbA4.add(rbtA14);
        rbtA14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA14.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA14ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA14);
        rbtA14.setBounds(778, 550, 40, 40);

        btnAnoA4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnoA4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        btnAnoA4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnoA4ActionPerformed(evt);
            }
        });
        jPanel2.add(btnAnoA4);
        btnAnoA4.setBounds(836, 550, 40, 40);

        btnFotoA4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotoA4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoA4ActionPerformed(evt);
            }
        });
        jPanel2.add(btnFotoA4);
        btnFotoA4.setBounds(892, 550, 40, 40);

        rbtA5.setBackground(new java.awt.Color(255, 255, 255));
        grbA5.add(rbtA5);
        rbtA5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA5ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA5);
        rbtA5.setBounds(722, 604, 40, 40);

        txpAAnot5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        txtaA5.setColumns(20);
        txtaA5.setLineWrap(true);
        txtaA5.setRows(5);
        txpAAnot5.setViewportView(txtaA5);

        jPanel2.add(txpAAnot5);
        txpAAnot5.setBounds(770, 643, 40, 10);

        rbtA15.setBackground(new java.awt.Color(255, 255, 255));
        grbA5.add(rbtA15);
        rbtA15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA15.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA15ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA15);
        rbtA15.setBounds(778, 604, 40, 40);

        btnAnoA5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnoA5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        btnAnoA5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnoA5ActionPerformed(evt);
            }
        });
        jPanel2.add(btnAnoA5);
        btnAnoA5.setBounds(836, 604, 40, 40);

        btnFotoA5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotoA5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoA5ActionPerformed(evt);
            }
        });
        jPanel2.add(btnFotoA5);
        btnFotoA5.setBounds(892, 604, 40, 40);

        rbtA6.setBackground(new java.awt.Color(255, 255, 255));
        grbA6.add(rbtA6);
        rbtA6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA6ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA6);
        rbtA6.setBounds(722, 657, 40, 40);

        txpAAnot6.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        txtaA6.setColumns(20);
        txtaA6.setLineWrap(true);
        txtaA6.setRows(5);
        txpAAnot6.setViewportView(txtaA6);

        jPanel2.add(txpAAnot6);
        txpAAnot6.setBounds(770, 696, 40, 10);

        rbtA16.setBackground(new java.awt.Color(255, 255, 255));
        grbA6.add(rbtA16);
        rbtA16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA16.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA16ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA16);
        rbtA16.setBounds(778, 657, 40, 40);

        btnAnoA6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnoA6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        btnAnoA6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnoA6ActionPerformed(evt);
            }
        });
        jPanel2.add(btnAnoA6);
        btnAnoA6.setBounds(836, 657, 40, 40);

        btnFotoA6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotoA6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoA6ActionPerformed(evt);
            }
        });
        jPanel2.add(btnFotoA6);
        btnFotoA6.setBounds(892, 657, 40, 40);

        rbtA7.setBackground(new java.awt.Color(255, 255, 255));
        grbA7.add(rbtA7);
        rbtA7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA7ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA7);
        rbtA7.setBounds(722, 709, 40, 40);

        txpAAnot7.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        txtaA7.setColumns(20);
        txtaA7.setLineWrap(true);
        txtaA7.setRows(5);
        txpAAnot7.setViewportView(txtaA7);

        jPanel2.add(txpAAnot7);
        txpAAnot7.setBounds(770, 749, 40, 10);

        rbtA17.setBackground(new java.awt.Color(255, 255, 255));
        grbA7.add(rbtA17);
        rbtA17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA17.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA17ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA17);
        rbtA17.setBounds(778, 709, 40, 40);

        btnAnoA7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnoA7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        btnAnoA7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnoA7ActionPerformed(evt);
            }
        });
        jPanel2.add(btnAnoA7);
        btnAnoA7.setBounds(836, 709, 40, 40);

        btnFotoA7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotoA7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoA7ActionPerformed(evt);
            }
        });
        jPanel2.add(btnFotoA7);
        btnFotoA7.setBounds(892, 709, 40, 40);

        rbtA8.setBackground(new java.awt.Color(255, 255, 255));
        grbA8.add(rbtA8);
        rbtA8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA8ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA8);
        rbtA8.setBounds(722, 762, 40, 40);

        txpAAnot8.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        txtaA8.setColumns(20);
        txtaA8.setLineWrap(true);
        txtaA8.setRows(5);
        txpAAnot8.setViewportView(txtaA8);

        jPanel2.add(txpAAnot8);
        txpAAnot8.setBounds(770, 800, 40, 10);

        rbtA18.setBackground(new java.awt.Color(255, 255, 255));
        grbA8.add(rbtA18);
        rbtA18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA18.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA18ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA18);
        rbtA18.setBounds(778, 762, 40, 40);

        btnAnoA8.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnoA8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        btnAnoA8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnoA8ActionPerformed(evt);
            }
        });
        jPanel2.add(btnAnoA8);
        btnAnoA8.setBounds(836, 762, 40, 40);

        btnFotoA8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotoA8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoA8ActionPerformed(evt);
            }
        });
        jPanel2.add(btnFotoA8);
        btnFotoA8.setBounds(892, 762, 40, 40);

        rbtA9.setBackground(new java.awt.Color(255, 255, 255));
        grbA9.add(rbtA9);
        rbtA9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA9ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA9);
        rbtA9.setBounds(722, 815, 40, 40);

        txpAAnot9.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        txtaA9.setColumns(20);
        txtaA9.setLineWrap(true);
        txtaA9.setRows(5);
        txpAAnot9.setViewportView(txtaA9);

        jPanel2.add(txpAAnot9);
        txpAAnot9.setBounds(770, 854, 40, 10);

        rbtA19.setBackground(new java.awt.Color(255, 255, 255));
        grbA9.add(rbtA19);
        rbtA19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA19.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA19ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA19);
        rbtA19.setBounds(778, 815, 40, 40);

        btnAnoA9.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnoA9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        btnAnoA9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnoA9ActionPerformed(evt);
            }
        });
        jPanel2.add(btnAnoA9);
        btnAnoA9.setBounds(836, 815, 40, 40);

        btnFotoA9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotoA9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoA9ActionPerformed(evt);
            }
        });
        jPanel2.add(btnFotoA9);
        btnFotoA9.setBounds(892, 815, 40, 40);

        rbtA10.setBackground(new java.awt.Color(255, 255, 255));
        grbA10.add(rbtA10);
        rbtA10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA10.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA10ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA10);
        rbtA10.setBounds(722, 868, 40, 40);

        txpAAnot10.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        txtaA10.setColumns(20);
        txtaA10.setLineWrap(true);
        txtaA10.setRows(5);
        txpAAnot10.setViewportView(txtaA10);

        jPanel2.add(txpAAnot10);
        txpAAnot10.setBounds(770, 907, 40, 10);

        rbtA20.setBackground(new java.awt.Color(255, 255, 255));
        grbA10.add(rbtA20);
        rbtA20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA20.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA20ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA20);
        rbtA20.setBounds(778, 868, 40, 40);

        btnAnoA10.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnoA10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        btnAnoA10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnoA10ActionPerformed(evt);
            }
        });
        jPanel2.add(btnAnoA10);
        btnAnoA10.setBounds(836, 868, 40, 40);

        btnFotoA10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotoA10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoA10ActionPerformed(evt);
            }
        });
        jPanel2.add(btnFotoA10);
        btnFotoA10.setBounds(892, 868, 40, 40);

        rbtA21.setBackground(new java.awt.Color(255, 255, 255));
        grbA11.add(rbtA21);
        rbtA21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA21.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA21ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA21);
        rbtA21.setBounds(722, 922, 40, 40);

        rbtA31.setBackground(new java.awt.Color(255, 255, 255));
        grbA11.add(rbtA31);
        rbtA31.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA31.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA31ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA31);
        rbtA31.setBounds(778, 922, 40, 40);

        txpAAnot11.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        txtaA11.setColumns(20);
        txtaA11.setLineWrap(true);
        txtaA11.setRows(5);
        txpAAnot11.setViewportView(txtaA11);

        jPanel2.add(txpAAnot11);
        txpAAnot11.setBounds(770, 960, 40, 10);

        btnAnoA11.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnoA11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        btnAnoA11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnoA11ActionPerformed(evt);
            }
        });
        jPanel2.add(btnAnoA11);
        btnAnoA11.setBounds(836, 922, 40, 40);

        btnFotoA11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotoA11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoA11ActionPerformed(evt);
            }
        });
        jPanel2.add(btnFotoA11);
        btnFotoA11.setBounds(892, 922, 40, 40);

        rbtA22.setBackground(new java.awt.Color(255, 255, 255));
        grbA12.add(rbtA22);
        rbtA22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA22.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA22ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA22);
        rbtA22.setBounds(722, 975, 40, 40);

        txpAAnot12.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        txtaA12.setColumns(20);
        txtaA12.setLineWrap(true);
        txtaA12.setRows(5);
        txpAAnot12.setViewportView(txtaA12);

        jPanel2.add(txpAAnot12);
        txpAAnot12.setBounds(770, 1014, 40, 10);

        rbtA32.setBackground(new java.awt.Color(255, 255, 255));
        grbA12.add(rbtA32);
        rbtA32.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA32.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA32ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA32);
        rbtA32.setBounds(778, 975, 40, 40);

        btnAnoA12.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnoA12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        btnAnoA12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnoA12ActionPerformed(evt);
            }
        });
        jPanel2.add(btnAnoA12);
        btnAnoA12.setBounds(836, 975, 40, 40);

        btnFotoA12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotoA12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoA12ActionPerformed(evt);
            }
        });
        jPanel2.add(btnFotoA12);
        btnFotoA12.setBounds(892, 975, 40, 40);

        rbtA23.setBackground(new java.awt.Color(255, 255, 255));
        grbA13.add(rbtA23);
        rbtA23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA23.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA23ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA23);
        rbtA23.setBounds(722, 1028, 40, 40);

        txpAAnot13.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        txtaA13.setColumns(20);
        txtaA13.setLineWrap(true);
        txtaA13.setRows(5);
        txpAAnot13.setViewportView(txtaA13);

        jPanel2.add(txpAAnot13);
        txpAAnot13.setBounds(770, 1067, 40, 10);

        rbtA33.setBackground(new java.awt.Color(255, 255, 255));
        grbA13.add(rbtA33);
        rbtA33.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA33.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA33ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA33);
        rbtA33.setBounds(778, 1028, 40, 40);

        btnAnoA13.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnoA13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        btnAnoA13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnoA13ActionPerformed(evt);
            }
        });
        jPanel2.add(btnAnoA13);
        btnAnoA13.setBounds(836, 1028, 40, 40);

        btnFotoA13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotoA13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoA13ActionPerformed(evt);
            }
        });
        jPanel2.add(btnFotoA13);
        btnFotoA13.setBounds(892, 1028, 40, 40);

        rbtA24.setBackground(new java.awt.Color(255, 255, 255));
        grbA14.add(rbtA24);
        rbtA24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA24.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA24ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA24);
        rbtA24.setBounds(722, 1081, 40, 40);

        txpAAnot14.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        txtaA14.setColumns(20);
        txtaA14.setLineWrap(true);
        txtaA14.setRows(5);
        txpAAnot14.setViewportView(txtaA14);

        jPanel2.add(txpAAnot14);
        txpAAnot14.setBounds(770, 1120, 40, 10);

        rbtA34.setBackground(new java.awt.Color(255, 255, 255));
        grbA14.add(rbtA34);
        rbtA34.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA34.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA34ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA34);
        rbtA34.setBounds(778, 1081, 40, 40);

        btnAnoA14.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnoA14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        btnAnoA14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnoA14ActionPerformed(evt);
            }
        });
        jPanel2.add(btnAnoA14);
        btnAnoA14.setBounds(836, 1081, 40, 40);

        btnFotoA14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotoA14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoA14ActionPerformed(evt);
            }
        });
        jPanel2.add(btnFotoA14);
        btnFotoA14.setBounds(892, 1081, 40, 40);

        rbtA25.setBackground(new java.awt.Color(255, 255, 255));
        grbA15.add(rbtA25);
        rbtA25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA25.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA25ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA25);
        rbtA25.setBounds(722, 1134, 40, 40);

        txpAAnot15.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        txtaA15.setColumns(20);
        txtaA15.setLineWrap(true);
        txtaA15.setRows(5);
        txpAAnot15.setViewportView(txtaA15);

        jPanel2.add(txpAAnot15);
        txpAAnot15.setBounds(770, 1172, 40, 10);

        rbtA35.setBackground(new java.awt.Color(255, 255, 255));
        grbA15.add(rbtA35);
        rbtA35.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA35.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA35ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA35);
        rbtA35.setBounds(778, 1134, 40, 40);

        btnAnoA15.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnoA15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        btnAnoA15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnoA15ActionPerformed(evt);
            }
        });
        jPanel2.add(btnAnoA15);
        btnAnoA15.setBounds(836, 1134, 40, 40);

        btnFotoA15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotoA15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoA15ActionPerformed(evt);
            }
        });
        jPanel2.add(btnFotoA15);
        btnFotoA15.setBounds(892, 1134, 40, 40);

        rbtA26.setBackground(new java.awt.Color(255, 255, 255));
        grbA16.add(rbtA26);
        rbtA26.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA26.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA26ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA26);
        rbtA26.setBounds(722, 1187, 40, 40);

        txpAAnot16.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        txtaA16.setColumns(20);
        txtaA16.setLineWrap(true);
        txtaA16.setRows(5);
        txpAAnot16.setViewportView(txtaA16);

        jPanel2.add(txpAAnot16);
        txpAAnot16.setBounds(770, 1226, 40, 10);

        rbtA36.setBackground(new java.awt.Color(255, 255, 255));
        grbA16.add(rbtA36);
        rbtA36.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA36.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA36ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA36);
        rbtA36.setBounds(778, 1187, 40, 40);

        btnAnoA16.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnoA16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        btnAnoA16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnoA16ActionPerformed(evt);
            }
        });
        jPanel2.add(btnAnoA16);
        btnAnoA16.setBounds(836, 1187, 40, 40);

        btnFotoA16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotoA16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoA16ActionPerformed(evt);
            }
        });
        jPanel2.add(btnFotoA16);
        btnFotoA16.setBounds(892, 1187, 40, 40);

        rbtA27.setBackground(new java.awt.Color(255, 255, 255));
        grbA17.add(rbtA27);
        rbtA27.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA27.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA27ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA27);
        rbtA27.setBounds(722, 1240, 40, 40);

        rbtA37.setBackground(new java.awt.Color(255, 255, 255));
        grbA17.add(rbtA37);
        rbtA37.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA37.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA37.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA37ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA37);
        rbtA37.setBounds(778, 1240, 40, 40);

        txpAAnot17.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        txtaA17.setColumns(20);
        txtaA17.setLineWrap(true);
        txtaA17.setRows(5);
        txpAAnot17.setViewportView(txtaA17);

        jPanel2.add(txpAAnot17);
        txpAAnot17.setBounds(770, 1278, 40, 10);

        btnAnoA17.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnoA17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        btnAnoA17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnoA17ActionPerformed(evt);
            }
        });
        jPanel2.add(btnAnoA17);
        btnAnoA17.setBounds(836, 1240, 40, 40);

        btnFotoA17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotoA17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoA17ActionPerformed(evt);
            }
        });
        jPanel2.add(btnFotoA17);
        btnFotoA17.setBounds(892, 1240, 40, 40);

        rbtA28.setBackground(new java.awt.Color(255, 255, 255));
        grbA18.add(rbtA28);
        rbtA28.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA28.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA28ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA28);
        rbtA28.setBounds(722, 1292, 40, 40);

        txpAAnot18.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        txtaA18.setColumns(20);
        txtaA18.setLineWrap(true);
        txtaA18.setRows(5);
        txpAAnot18.setViewportView(txtaA18);

        jPanel2.add(txpAAnot18);
        txpAAnot18.setBounds(770, 1330, 40, 10);

        rbtA38.setBackground(new java.awt.Color(255, 255, 255));
        grbA18.add(rbtA38);
        rbtA38.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA38.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA38.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA38ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA38);
        rbtA38.setBounds(778, 1292, 40, 40);

        btnAnoA18.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnoA18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        btnAnoA18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnoA18ActionPerformed(evt);
            }
        });
        jPanel2.add(btnAnoA18);
        btnAnoA18.setBounds(836, 1291, 40, 40);

        btnFotoA18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotoA18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoA18ActionPerformed(evt);
            }
        });
        jPanel2.add(btnFotoA18);
        btnFotoA18.setBounds(892, 1292, 40, 40);

        rbtA29.setBackground(new java.awt.Color(255, 255, 255));
        grbA19.add(rbtA29);
        rbtA29.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA29.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA29ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA29);
        rbtA29.setBounds(722, 1346, 40, 40);

        txpAAnot19.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        txtaA19.setColumns(20);
        txtaA19.setLineWrap(true);
        txtaA19.setRows(5);
        txpAAnot19.setViewportView(txtaA19);

        jPanel2.add(txpAAnot19);
        txpAAnot19.setBounds(770, 1386, 40, 10);

        rbtA39.setBackground(new java.awt.Color(255, 255, 255));
        grbA19.add(rbtA39);
        rbtA39.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA39.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA39.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA39ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA39);
        rbtA39.setBounds(778, 1346, 40, 40);

        btnAnoA19.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnoA19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        btnAnoA19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnoA19ActionPerformed(evt);
            }
        });
        jPanel2.add(btnAnoA19);
        btnAnoA19.setBounds(836, 1346, 40, 40);

        btnFotoA19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotoA19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoA19ActionPerformed(evt);
            }
        });
        jPanel2.add(btnFotoA19);
        btnFotoA19.setBounds(892, 1346, 40, 40);

        rbtA30.setBackground(new java.awt.Color(255, 255, 255));
        gbrA20.add(rbtA30);
        rbtA30.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA30.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA30ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA30);
        rbtA30.setBounds(722, 1398, 40, 40);

        txpAAnot20.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        txtaA20.setColumns(20);
        txtaA20.setLineWrap(true);
        txtaA20.setRows(5);
        txpAAnot20.setViewportView(txtaA20);

        jPanel2.add(txpAAnot20);
        txpAAnot20.setBounds(770, 1438, 40, 10);

        rbtA40.setBackground(new java.awt.Color(255, 255, 255));
        gbrA20.add(rbtA40);
        rbtA40.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA40.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA40.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA40ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA40);
        rbtA40.setBounds(778, 1398, 40, 40);

        btnAnoA20.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnoA20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        btnAnoA20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnoA20ActionPerformed(evt);
            }
        });
        jPanel2.add(btnAnoA20);
        btnAnoA20.setBounds(836, 1398, 40, 40);

        btnFotoA20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotoA20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoA20ActionPerformed(evt);
            }
        });
        jPanel2.add(btnFotoA20);
        btnFotoA20.setBounds(892, 1398, 40, 40);

        rbtA41.setBackground(new java.awt.Color(255, 255, 255));
        gbrA21.add(rbtA41);
        rbtA41.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA41.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA41.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA41ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA41);
        rbtA41.setBounds(722, 1452, 40, 40);

        txpAAnot21.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        txtaA21.setColumns(20);
        txtaA21.setLineWrap(true);
        txtaA21.setRows(5);
        txpAAnot21.setViewportView(txtaA21);

        jPanel2.add(txpAAnot21);
        txpAAnot21.setBounds(770, 1492, 40, 10);

        rbtA51.setBackground(new java.awt.Color(255, 255, 255));
        gbrA21.add(rbtA51);
        rbtA51.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA51.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA51.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA51ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA51);
        rbtA51.setBounds(778, 1452, 40, 40);

        btnAnoA21.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnoA21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        btnAnoA21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnoA21ActionPerformed(evt);
            }
        });
        jPanel2.add(btnAnoA21);
        btnAnoA21.setBounds(836, 1452, 40, 40);

        btnFotoA21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotoA21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoA21ActionPerformed(evt);
            }
        });
        jPanel2.add(btnFotoA21);
        btnFotoA21.setBounds(892, 1452, 40, 40);

        rbtA42.setBackground(new java.awt.Color(255, 255, 255));
        gbrA22.add(rbtA42);
        rbtA42.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA42.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA42.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA42ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA42);
        rbtA42.setBounds(722, 1505, 40, 40);

        txpAAnot22.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        txtaA22.setColumns(20);
        txtaA22.setLineWrap(true);
        txtaA22.setRows(5);
        txpAAnot22.setViewportView(txtaA22);

        jPanel2.add(txpAAnot22);
        txpAAnot22.setBounds(770, 1544, 40, 10);

        rbtA52.setBackground(new java.awt.Color(255, 255, 255));
        gbrA22.add(rbtA52);
        rbtA52.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA52.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA52.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA52ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA52);
        rbtA52.setBounds(778, 1505, 40, 40);

        btnAnoA22.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnoA22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        btnAnoA22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnoA22ActionPerformed(evt);
            }
        });
        jPanel2.add(btnAnoA22);
        btnAnoA22.setBounds(836, 1505, 40, 40);

        btnFotoA22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotoA22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoA22ActionPerformed(evt);
            }
        });
        jPanel2.add(btnFotoA22);
        btnFotoA22.setBounds(892, 1505, 40, 40);

        rbtA43.setBackground(new java.awt.Color(255, 255, 255));
        gbrA23.add(rbtA43);
        rbtA43.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA43.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA43.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA43ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA43);
        rbtA43.setBounds(722, 1557, 40, 40);

        txpAAnot23.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        txtaA23.setColumns(20);
        txtaA23.setLineWrap(true);
        txtaA23.setRows(5);
        txpAAnot23.setViewportView(txtaA23);

        jPanel2.add(txpAAnot23);
        txpAAnot23.setBounds(770, 1596, 40, 10);

        rbtA53.setBackground(new java.awt.Color(255, 255, 255));
        gbrA23.add(rbtA53);
        rbtA53.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA53.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA53.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA53ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA53);
        rbtA53.setBounds(778, 1557, 40, 40);

        btnAnoA23.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnoA23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        btnAnoA23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnoA23ActionPerformed(evt);
            }
        });
        jPanel2.add(btnAnoA23);
        btnAnoA23.setBounds(836, 1557, 40, 40);

        btnFotoA23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotoA23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoA23ActionPerformed(evt);
            }
        });
        jPanel2.add(btnFotoA23);
        btnFotoA23.setBounds(892, 1557, 40, 40);

        rbtA44.setBackground(new java.awt.Color(255, 255, 255));
        gbrA24.add(rbtA44);
        rbtA44.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA44.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA44.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA44ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA44);
        rbtA44.setBounds(722, 1610, 40, 40);

        txpAAnot24.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        txtaA24.setColumns(20);
        txtaA24.setLineWrap(true);
        txtaA24.setRows(5);
        txpAAnot24.setViewportView(txtaA24);

        jPanel2.add(txpAAnot24);
        txpAAnot24.setBounds(770, 1650, 40, 10);

        rbtA54.setBackground(new java.awt.Color(255, 255, 255));
        gbrA24.add(rbtA54);
        rbtA54.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA54.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA54.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA54ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA54);
        rbtA54.setBounds(778, 1610, 40, 40);

        btnAnoA24.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnoA24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        btnAnoA24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnoA24ActionPerformed(evt);
            }
        });
        jPanel2.add(btnAnoA24);
        btnAnoA24.setBounds(836, 1610, 40, 40);

        btnFotoA24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotoA24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoA24ActionPerformed(evt);
            }
        });
        jPanel2.add(btnFotoA24);
        btnFotoA24.setBounds(892, 1610, 40, 40);

        rbtA45.setBackground(new java.awt.Color(255, 255, 255));
        gbrA25.add(rbtA45);
        rbtA45.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA45.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA45.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA45ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA45);
        rbtA45.setBounds(722, 1664, 40, 40);

        txpAAnot25.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        txtaA25.setColumns(20);
        txtaA25.setLineWrap(true);
        txtaA25.setRows(5);
        txpAAnot25.setViewportView(txtaA25);

        jPanel2.add(txpAAnot25);
        txpAAnot25.setBounds(770, 1702, 40, 10);

        rbtA55.setBackground(new java.awt.Color(255, 255, 255));
        gbrA25.add(rbtA55);
        rbtA55.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA55.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA55.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA55ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA55);
        rbtA55.setBounds(778, 1664, 40, 40);

        btnAnoA25.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnoA25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        btnAnoA25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnoA25ActionPerformed(evt);
            }
        });
        jPanel2.add(btnAnoA25);
        btnAnoA25.setBounds(836, 1664, 40, 40);

        btnFotoA25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotoA25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoA25ActionPerformed(evt);
            }
        });
        jPanel2.add(btnFotoA25);
        btnFotoA25.setBounds(892, 1664, 40, 40);

        rbtA46.setBackground(new java.awt.Color(255, 255, 255));
        gbrA26.add(rbtA46);
        rbtA46.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA46.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA46.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA46ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA46);
        rbtA46.setBounds(722, 1716, 40, 40);

        txpAAnot26.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        txtaA26.setColumns(20);
        txtaA26.setLineWrap(true);
        txtaA26.setRows(5);
        txpAAnot26.setViewportView(txtaA26);

        jPanel2.add(txpAAnot26);
        txpAAnot26.setBounds(770, 1754, 40, 10);

        rbtA56.setBackground(new java.awt.Color(255, 255, 255));
        gbrA26.add(rbtA56);
        rbtA56.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA56.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA56.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA56ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA56);
        rbtA56.setBounds(778, 1716, 40, 40);

        btnAnoA26.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnoA26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        btnAnoA26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnoA26ActionPerformed(evt);
            }
        });
        jPanel2.add(btnAnoA26);
        btnAnoA26.setBounds(836, 1716, 40, 40);

        btnFotoA26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotoA26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoA26ActionPerformed(evt);
            }
        });
        jPanel2.add(btnFotoA26);
        btnFotoA26.setBounds(892, 1716, 40, 40);

        rbtA47.setBackground(new java.awt.Color(255, 255, 255));
        gbrA27.add(rbtA47);
        rbtA47.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA47.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA47.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA47ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA47);
        rbtA47.setBounds(722, 1770, 40, 40);

        txpAAnot27.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        txtaA27.setColumns(20);
        txtaA27.setLineWrap(true);
        txtaA27.setRows(5);
        txpAAnot27.setViewportView(txtaA27);

        jPanel2.add(txpAAnot27);
        txpAAnot27.setBounds(770, 1810, 40, 10);

        btnAnoA27.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAnoA27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/7124219_annotation_icon.png"))); // NOI18N
        btnAnoA27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnoA27ActionPerformed(evt);
            }
        });
        jPanel2.add(btnAnoA27);
        btnAnoA27.setBounds(836, 1770, 40, 40);

        btnFotoA27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/290125_camera_image_photo_photography_video_icon.png"))); // NOI18N
        btnFotoA27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoA27ActionPerformed(evt);
            }
        });
        jPanel2.add(btnFotoA27);
        btnFotoA27.setBounds(892, 1770, 40, 40);

        rbtA57.setBackground(new java.awt.Color(255, 255, 255));
        gbrA27.add(rbtA57);
        rbtA57.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbtA57.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rbtA57.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtA57ActionPerformed(evt);
            }
        });
        jPanel2.add(rbtA57);
        rbtA57.setBounds(778, 1770, 40, 40);

        btnEncPre.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/icones/48763_edit_edit_file_file.png"))); // NOI18N
        btnEncPre.setToolTipText("Encerrar Preventiva");
        btnEncPre.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEncPre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEncPreActionPerformed(evt);
            }
        });
        jPanel2.add(btnEncPre);
        btnEncPre.setBounds(834, 1923, 64, 64);

        txtSetPre.setEditable(false);
        txtSetPre.setColumns(20);
        txtSetPre.setLineWrap(true);
        txtSetPre.setRows(5);
        jScrollPane2.setViewportView(txtSetPre);

        jPanel2.add(jScrollPane2);
        jScrollPane2.setBounds(278, 238, 184, 38);

        txtEquPre.setEditable(false);
        txtEquPre.setColumns(20);
        txtEquPre.setLineWrap(true);
        txtEquPre.setRows(5);
        jScrollPane3.setViewportView(txtEquPre);

        jPanel2.add(jScrollPane3);
        jScrollPane3.setBounds(488, 238, 184, 38);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/prjmanutencao/telas/imagens_fundo/Hidrau mensal1 – 1.png"))); // NOI18N
        jPanel2.add(jLabel1);
        jLabel1.setBounds(0, 0, 1010, 2025);

        jScrollPane1.setViewportView(jPanel2);
        jPanel2.getAccessibleContext().setAccessibleName("");

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(-4, -3, 1004, 645);
        jScrollPane1.getAccessibleContext().setAccessibleName("");

        setBounds(0, 0, 1007, 672);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Método formInternalFrameOpened
     *
     * Acionado automaticamente quando a janela interna (`JInternalFrame`) é
     * aberta.
     *
     * Funcionalidades: 1. **Inicializa os botões de rádio** `Não Conforme`: -
     * Cria um array com 27 botões de rádio (`JRadioButton`) representando itens
     * específicos. - Marca todos como selecionados (`setSelected(true)`). -
     * Define a situação de cada item como `"Não Conforme"` e armazena em um
     * array de `String`.
     *
     * 2. **Atribui valores às variáveis individuais de situação**: - Copia os
     * valores do array `situacoes` para variáveis como `situacao`, `situacao1`,
     * ..., `situacao26`. - Isso garante compatibilidade com outras partes do
     * código que usam variáveis separadas.
     *
     * 3. **Aplica o validador de teclado aos campos de texto**: - Para cada
     * campo `txtaA1` até `txtaA27`, adiciona o `KeyAdapter` `validadorTeclado`.
     * - O validador restringe a entrada a caracteres permitidos e limita o
     * texto a 200 caracteres. - Utiliza reflexão (`getDeclaredField`) para
     * acessar dinamicamente os campos. - Em caso de erro, exibe uma mensagem de
     * alerta via `JOptionPane`.
     *
     * Esse método garante que, ao abrir a janela, todos os itens estejam
     * inicialmente marcados como "Não Conforme" e que os campos de observação
     * estejam protegidos contra entradas inválidas.
     *
     * @param evt o evento gerado pela abertura da janela interna
     */
    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened

        // Array de botões de "Não Conforme"
        JRadioButton[] botoesNaoConforme = {
            rbtA1, rbtA2, rbtA3, rbtA4, rbtA5, rbtA6, rbtA7, rbtA8,
            rbtA9, rbtA10, rbtA21, rbtA22, rbtA23, rbtA24, rbtA25, rbtA26,
            rbtA27, rbtA28, rbtA29, rbtA30, rbtA41, rbtA42, rbtA43, rbtA44,
            rbtA45, rbtA46, rbtA47
        };
        // Referências para as variáveis de situação
        String[] situacoes = new String[27];
//
        for (int i = 0; i < botoesNaoConforme.length; i++) {
            botoesNaoConforme[i].setSelected(true);
            situacoes[i] = "Não Conforme";
        }
        // Atribui as variáveis individuais para manter compatibilidade com o restante do código
        situacao = situacoes[0];
        situacao1 = situacoes[1];
        situacao2 = situacoes[2];
        situacao3 = situacoes[3];
        situacao4 = situacoes[4];
        situacao5 = situacoes[5];
        situacao6 = situacoes[6];
        situacao7 = situacoes[7];
        situacao8 = situacoes[8];
        situacao9 = situacoes[9];
        situacao10 = situacoes[10];
        situacao11 = situacoes[11];
        situacao12 = situacoes[12];
        situacao13 = situacoes[13];
        situacao14 = situacoes[14];
        situacao15 = situacoes[15];
        situacao16 = situacoes[16];
        situacao17 = situacoes[17];
        situacao18 = situacoes[18];
        situacao19 = situacoes[19];
        situacao20 = situacoes[20];
        situacao21 = situacoes[21];
        situacao22 = situacoes[22];
        situacao23 = situacoes[23];
        situacao24 = situacoes[24];
        situacao25 = situacoes[25];
        situacao26 = situacoes[26];

        for (int i = 1; i <= 27; i++) {
            try {
                JTextArea campo = (JTextArea) this.getClass().getDeclaredField("txtaA" + i).get(this);
                campo.addKeyListener(validadorTeclado);
            } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException e) {
                JOptionPane.showMessageDialog(null, "Erro" + e.getMessage(), "Atenção", 3);
            }
        }

    }//GEN-LAST:event_formInternalFrameOpened

    private void txtCanFotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCanFotActionPerformed

        jPanel1.setVisible(false);
        lblFotEltMen1.setIcon(null);
        txtFotPre1.setText(null);
        btnSalFotPre1.setEnabled(false);
    }//GEN-LAST:event_txtCanFotActionPerformed

    private void btnArqFotoPreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnArqFotoPreActionPerformed
        // TODO add your handling code here:
        select_arq();
        nome_foto = nome1;

    }//GEN-LAST:event_btnArqFotoPreActionPerformed

    private void btnSalFotPre1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalFotPre1ActionPerformed
        // TODO add your handling code here:
        foto_prev();
    }//GEN-LAST:event_btnSalFotPre1ActionPerformed

    private void btnCamPreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCamPreActionPerformed
        // TODO add your handling code here:
        TelaWebCam1 captura1 = new TelaWebCam1();
        captura1.setVisible(true);
        tipo_prev = "Hidráulica - Mensal";
    }//GEN-LAST:event_btnCamPreActionPerformed

    /**
     * Método btnFotoA27ActionPerformed
     *
     * Acionado ao clicar no botão de foto do item A27. Define o nome da imagem
     * como "A27 - " e atribui à variável nome_foto. Em seguida, exibe o painel
     * jPanel1 e ajusta seu tamanho para 270x250 pixels, permitindo a
     * visualização ou captura da imagem correspondente ao item A27.
     *
     * @param evt o evento gerado pelo clique no botão de foto do item A27
     */
    private void btnFotoA27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoA27ActionPerformed

        nome1 = "A27 - ";
        nome_foto = nome1;
        jPanel1.setVisible(true);
        jPanel1.setSize(270, 250);
    }//GEN-LAST:event_btnFotoA27ActionPerformed

    /**
     * Método btnAnoA27ActionPerformed
     *
     * Acionado ao clicar no botão de anotação do item A27. Exibe ou oculta a
     * área de anotação txpAAnot27 com base no estado do botão btnAnoA27. Se
     * estiver selecionado, o campo é exibido com tamanho 170x100 pixels; caso
     * contrário, é ocultado.
     *
     * @param evt o evento gerado pela interação com o botão de anotação do item
     * A27
     */
    private void btnAnoA27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnoA27ActionPerformed

        if (btnAnoA27.isSelected()) {
            txpAAnot27.setVisible(true);
            txpAAnot27.setSize(170, 100);
            txtaA27.requestFocus();
        } else {
            txpAAnot27.setVisible(false);
        }
    }//GEN-LAST:event_btnAnoA27ActionPerformed

    /**
     * Método rbtA47ActionPerformed
     *
     * Acionado ao selecionar o botão de rádio "Não Conforme" do item A27.
     * Define a variável situacao26 como "Não Conforme", indicando que o item
     * A27 está fora de conformidade.
     *
     * @param evt o evento gerado pela seleção do botão de rádio do item A27
     */
    private void rbtA47ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA47ActionPerformed

        situacao26 = "Não Conforme";
    }//GEN-LAST:event_rbtA47ActionPerformed

    /**
     * Método btnFotoA26ActionPerformed
     *
     * Acionado ao clicar no botão de foto do item A26. Define o nome da imagem
     * como "A26 - " e atribui à variável nome_foto. Em seguida, exibe o painel
     * jPanel1 e ajusta seu tamanho para 270x250 pixels, permitindo a
     * visualização ou captura da imagem correspondente ao item A26.
     *
     * @param evt o evento gerado pelo clique no botão de foto do item A26
     */
    private void btnFotoA26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoA26ActionPerformed

        nome1 = "A26 - ";
        nome_foto = nome1;
        jPanel1.setVisible(true);
        jPanel1.setSize(270, 250);
    }//GEN-LAST:event_btnFotoA26ActionPerformed

    /**
     * Método btnAnoA26ActionPerformed
     *
     * Acionado ao clicar no botão de anotação do item A26. Exibe ou oculta a
     * área de anotação txpAAnot26 com base no estado do botão btnAnoA26. Se
     * estiver selecionado, o campo é exibido com tamanho 170x100 pixels; caso
     * contrário, é ocultado.
     *
     * @param evt o evento gerado pela interação com o botão de anotação do item
     * A26
     */
    private void btnAnoA26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnoA26ActionPerformed

        if (btnAnoA26.isSelected()) {
            txpAAnot26.setVisible(true);
            txpAAnot26.setSize(170, 100);
            txtaA26.requestFocus();
        } else {
            txpAAnot26.setVisible(false);
        }
    }//GEN-LAST:event_btnAnoA26ActionPerformed

    /**
     * Método rbtA56ActionPerformed
     *
     * Acionado ao selecionar o botão de rádio "Conforme" do item A26. Define a
     * variável situacao25 como "Conforme", indicando que o item A26 está em
     * conformidade.
     *
     * @param evt o evento gerado pela seleção do botão de rádio do item A26
     */
    private void rbtA56ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA56ActionPerformed

        situacao25 = "Conforme";
    }//GEN-LAST:event_rbtA56ActionPerformed

    /**
     * Método rbtA46ActionPerformed
     *
     * Acionado ao selecionar o botão de rádio "Não Conforme" do item A26.
     * Define a variável situacao25 como "Não Conforme", indicando que o item
     * A26 está fora de conformidade.
     *
     * @param evt o evento gerado pela seleção do botão de rádio do item A26
     */
    private void rbtA46ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA46ActionPerformed

        situacao25 = "Não Conforme";
    }//GEN-LAST:event_rbtA46ActionPerformed

    /**
     * Método btnFotoA25ActionPerformed
     *
     * Acionado ao clicar no botão de foto do item A25. Define o nome da imagem
     * como "A25 - " e atribui à variável nome_foto. Em seguida, exibe o painel
     * jPanel1 e ajusta seu tamanho para 270x250 pixels, permitindo a
     * visualização ou captura da imagem correspondente ao item A25.
     *
     * @param evt o evento gerado pelo clique no botão de foto do item A25
     */
    private void btnFotoA25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoA25ActionPerformed

        nome1 = "A25 - ";
        nome_foto = nome1;
        jPanel1.setVisible(true);
        jPanel1.setSize(270, 250);
    }//GEN-LAST:event_btnFotoA25ActionPerformed

    /**
     * Método btnAnoA25ActionPerformed
     *
     * Acionado ao clicar no botão de anotação do item A25. Exibe ou oculta a
     * área de anotação txpAAnot25 com base no estado do botão btnAnoA25. Se
     * estiver selecionado, o campo é exibido com tamanho 170x100 pixels; caso
     * contrário, é ocultado.
     *
     * @param evt o evento gerado pela interação com o botão de anotação do item
     * A25
     */
    private void btnAnoA25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnoA25ActionPerformed

        if (btnAnoA25.isSelected()) {
            txpAAnot25.setVisible(true);
            txpAAnot25.setSize(170, 100);
            txtaA25.requestFocus();
        } else {
            txpAAnot25.setVisible(false);
        }
    }//GEN-LAST:event_btnAnoA25ActionPerformed

    /**
     * Método rbtA55ActionPerformed
     *
     * Acionado ao selecionar o botão de rádio "Conforme" do item A25. Define a
     * variável situacao24 como "Conforme", indicando que o item A25 está em
     * conformidade.
     *
     * @param evt o evento gerado pela seleção do botão de rádio do item A25
     */
    private void rbtA55ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA55ActionPerformed

        situacao24 = "Conforme";
    }//GEN-LAST:event_rbtA55ActionPerformed

    /**
     * Método rbtA45ActionPerformed
     *
     * Acionado ao selecionar o botão de rádio "Não Conforme" do item A25.
     * Define a variável situacao24 como "Não Conforme", indicando que o item
     * A25 está fora de conformidade.
     *
     * @param evt o evento gerado pela seleção do botão de rádio do item A25
     */
    private void rbtA45ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA45ActionPerformed

        situacao24 = "Não Conforme";
    }//GEN-LAST:event_rbtA45ActionPerformed

    /**
     * Método btnFotoA24ActionPerformed
     *
     * Acionado ao clicar no botão de foto do item A24. Define o nome da imagem
     * como "A24 - " e atribui à variável nome_foto. Em seguida, exibe o painel
     * jPanel1 e ajusta seu tamanho para 270x250 pixels, permitindo a
     * visualização ou captura da imagem correspondente ao item A24.
     *
     * @param evt o evento gerado pelo clique no botão de foto do item A24
     */
    private void btnFotoA24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoA24ActionPerformed

        nome1 = "A24 - ";
        nome_foto = nome1;
        jPanel1.setVisible(true);
        jPanel1.setSize(270, 250);
    }//GEN-LAST:event_btnFotoA24ActionPerformed

    /**
     * Método btnAnoA24ActionPerformed
     *
     * Acionado ao clicar no botão de anotação do item A24. Exibe ou oculta a
     * área de anotação txpAAnot24 com base no estado do botão btnAnoA24. Se
     * estiver selecionado, o campo é exibido com tamanho 170x100 pixels; caso
     * contrário, é ocultado.
     *
     * @param evt o evento gerado pela interação com o botão de anotação do item
     * A24
     */
    private void btnAnoA24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnoA24ActionPerformed

        if (btnAnoA24.isSelected()) {
            txpAAnot24.setVisible(true);
            txpAAnot24.setSize(170, 100);
            txtaA24.requestFocus();
        } else {
            txpAAnot24.setVisible(false);;
        }
    }//GEN-LAST:event_btnAnoA24ActionPerformed

    /**
     * Método rbtA54ActionPerformed
     *
     * Acionado ao selecionar o botão de rádio "Conforme" do item A24. Define a
     * variável situacao23 como "Conforme", indicando que o item A24 está em
     * conformidade.
     *
     * @param evt o evento gerado pela seleção do botão de rádio do item A24
     */
    private void rbtA54ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA54ActionPerformed

        situacao23 = "Conforme";
    }//GEN-LAST:event_rbtA54ActionPerformed

    /**
     * Método rbtA44ActionPerformed
     *
     * Acionado ao selecionar o botão de rádio "Não Conforme" do item A24.
     * Define a variável situacao23 como "Não Conforme", indicando que o item
     * A24 está fora de conformidade.
     *
     * @param evt o evento gerado pela seleção do botão de rádio do item A24
     */
    private void rbtA44ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA44ActionPerformed

        situacao23 = "Não Conforme";
    }//GEN-LAST:event_rbtA44ActionPerformed

    /**
     * Método btnFotoA23ActionPerformed
     *
     * Acionado ao clicar no botão de foto do item A23. Define o nome da imagem
     * como "A23 - " e atribui à variável nome_foto. Em seguida, exibe o painel
     * jPanel1 e ajusta seu tamanho para 270x250 pixels, permitindo a
     * visualização ou captura da imagem correspondente ao item A23.
     *
     * @param evt o evento gerado pelo clique no botão de foto do item A23
     */
    private void btnFotoA23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoA23ActionPerformed

        nome1 = "A23 - ";
        nome_foto = nome1;
        jPanel1.setVisible(true);
        jPanel1.setSize(270, 250);
    }//GEN-LAST:event_btnFotoA23ActionPerformed

    /**
     * Método btnAnoA23ActionPerformed
     *
     * Acionado ao clicar no botão de anotação do item A23. Exibe ou oculta a
     * área de anotação txpAAnot23 com base no estado do botão btnAnoA23. Se
     * estiver selecionado, o campo é exibido com tamanho 170x100 pixels; caso
     * contrário, é ocultado.
     *
     * @param evt o evento gerado pela interação com o botão de anotação do item
     * A23
     */
    private void btnAnoA23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnoA23ActionPerformed

        if (btnAnoA23.isSelected()) {
            txpAAnot23.setVisible(true);
            txpAAnot23.setSize(170, 100);
            txtaA23.requestFocus();
        } else {
            txpAAnot23.setVisible(false);
        }
    }//GEN-LAST:event_btnAnoA23ActionPerformed

    /**
     * Método rbtA53ActionPerformed
     *
     * Acionado ao selecionar o botão de rádio "Conforme" do item A23. Define a
     * variável situacao22 como "Conforme", indicando que o item A23 está em
     * conformidade.
     *
     * @param evt o evento gerado pela seleção do botão de rádio do item A23
     */
    private void rbtA53ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA53ActionPerformed

        situacao22 = "Conforme";
    }//GEN-LAST:event_rbtA53ActionPerformed

    /**
     * Método rbtA43ActionPerformed
     *
     * Acionado ao selecionar o botão de rádio "Não Conforme" do item A23.
     * Define a variável situacao22 como "Não Conforme", indicando que o item
     * A23 está fora de conformidade.
     *
     * @param evt o evento gerado pela seleção do botão de rádio do item A23
     */
    private void rbtA43ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA43ActionPerformed

        situacao22 = "Não Conforme";
    }//GEN-LAST:event_rbtA43ActionPerformed

    /**
     * Método btnFotoA22ActionPerformed
     *
     * Define o nome da imagem como "A22 - " e exibe o painel jPanel1 com
     * tamanho 270x250 pixels, permitindo a visualização ou captura da imagem
     * correspondente ao item A22.
     *
     * @param evt o evento gerado pelo clique no botão de foto do item A22
     */
    private void btnFotoA22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoA22ActionPerformed

        nome1 = "A22 - ";
        nome_foto = nome1;
        jPanel1.setVisible(true);
        jPanel1.setSize(270, 250);
    }//GEN-LAST:event_btnFotoA22ActionPerformed

    /**
     * Método btnAnoA22ActionPerformed
     *
     * Exibe ou oculta a área de anotação txpAAnot22 com base no estado do botão
     * btnAnoA22. Se estiver selecionado, o campo é exibido com tamanho 170x100
     * pixels; caso contrário, é ocultado.
     *
     * @param evt o evento gerado pela interação com o botão de anotação do item
     * A22
     */
    private void btnAnoA22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnoA22ActionPerformed

        if (btnAnoA22.isSelected()) {
            txpAAnot22.setVisible(true);
            txpAAnot22.setSize(170, 100);
            txtaA22.requestFocus();
        } else {
            txpAAnot22.setVisible(false);
        }
    }//GEN-LAST:event_btnAnoA22ActionPerformed

    /**
     * Método rbtA52ActionPerformed
     *
     * Acionado ao selecionar o botão de rádio "Conforme" do item A22. Define a
     * variável situacao21 como "Conforme".
     *
     * @param evt o evento gerado pela seleção do botão de rádio do item A22
     */
    private void rbtA52ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA52ActionPerformed

        situacao21 = "Conforme";
    }//GEN-LAST:event_rbtA52ActionPerformed

    /**
     * Método rbtA42ActionPerformed
     *
     * Acionado ao selecionar o botão de rádio "Não Conforme" do item A22.
     * Define a variável situacao21 como "Não Conforme".
     *
     * @param evt o evento gerado pela seleção do botão de rádio do item A22
     */
    private void rbtA42ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA42ActionPerformed

        situacao21 = "Não Conforme";
    }//GEN-LAST:event_rbtA42ActionPerformed

    /**
     * Método btnFotoA21ActionPerformed
     *
     * Define o nome da imagem como "A21 - " e exibe o painel jPanel1 com
     * tamanho 270x250 pixels, permitindo a visualização ou captura da imagem
     * correspondente ao item A21.
     *
     * @param evt o evento gerado pelo clique no botão de foto do item A21
     */
    private void btnFotoA21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoA21ActionPerformed

        nome1 = "A21 - ";
        nome_foto = nome1;
        jPanel1.setVisible(true);
        jPanel1.setSize(270, 250);
    }//GEN-LAST:event_btnFotoA21ActionPerformed

    /**
     * Método btnFotoA21ActionPerformed
     *
     * Define o nome da imagem como "A21 - " e exibe o painel jPanel1 com
     * tamanho 270x250 pixels, permitindo a visualização ou captura da imagem
     * correspondente ao item A21.
     *
     * @param evt o evento gerado pelo clique no botão de foto do item A21
     */
    private void btnAnoA21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnoA21ActionPerformed

        if (btnAnoA21.isSelected()) {
            txpAAnot21.setVisible(true);
            txpAAnot21.setSize(170, 100);
            txtaA21.requestFocus();
        } else {
            txpAAnot21.setVisible(false);
        }
    }//GEN-LAST:event_btnAnoA21ActionPerformed

    /**
     * Método rbtA51ActionPerformed
     *
     * Acionado ao selecionar o botão de rádio "Conforme" do item A21. Define a
     * variável situacao20 como "Conforme".
     *
     * @param evt o evento gerado pela seleção do botão de rádio do item A21
     */
    private void rbtA51ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA51ActionPerformed

        situacao20 = "Conforme";
    }//GEN-LAST:event_rbtA51ActionPerformed

    /**
     * Método rbtA41ActionPerformed
     *
     * Acionado ao selecionar o botão de rádio "Não Conforme" do item A21.
     * Define a variável situacao20 como "Não Conforme".
     *
     * @param evt o evento gerado pela seleção do botão de rádio do item A21
     */
    private void rbtA41ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA41ActionPerformed

        situacao20 = "Não Conforme";
    }//GEN-LAST:event_rbtA41ActionPerformed

    /**
     * Método btnFotoA20ActionPerformed
     *
     * Acionado ao clicar no botão de foto do item A20. Define o nome da imagem
     * como "A20 - " e atribui à variável nome_foto. Em seguida, exibe o painel
     * jPanel1 e ajusta seu tamanho para 270x250 pixels, permitindo a
     * visualização ou captura da imagem correspondente ao item A20.
     *
     * @param evt o evento gerado pelo clique no botão de foto do item A20
     */
    private void btnFotoA20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoA20ActionPerformed

        nome1 = "A20 - ";
        nome_foto = nome1;
        jPanel1.setVisible(true);
        jPanel1.setSize(270, 250);
    }//GEN-LAST:event_btnFotoA20ActionPerformed

    /**
     * Método btnAnoA20ActionPerformed
     *
     * Acionado ao clicar no botão de anotação do item A20. Exibe ou oculta a
     * área de anotação txpAAnot20 com base no estado do botão btnAnoA20. Se
     * estiver selecionado, o campo é exibido com tamanho 170x100 pixels; caso
     * contrário, é ocultado.
     *
     * @param evt o evento gerado pela interação com o botão de anotação do item
     * A20
     */
    private void btnAnoA20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnoA20ActionPerformed

        if (btnAnoA20.isSelected()) {
            txpAAnot20.setVisible(true);
            txpAAnot20.setSize(170, 100);
            txtaA20.requestFocus();
        } else {
            txpAAnot20.setVisible(false);
        }
    }//GEN-LAST:event_btnAnoA20ActionPerformed

    /**
     * Método rbtA40ActionPerformed
     *
     * Acionado ao selecionar o botão de rádio "Conforme" do item A20. Define a
     * variável situacao19 como "Conforme", indicando que o item A20 está em
     * conformidade.
     *
     * @param evt o evento gerado pela seleção do botão de rádio do item A20
     */
    private void rbtA40ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA40ActionPerformed

        situacao19 = "Conforme";
    }//GEN-LAST:event_rbtA40ActionPerformed

    /**
     * Método rbtA30ActionPerformed
     *
     * Acionado ao selecionar o botão de rádio "Não Conforme" do item A20.
     * Define a variável situacao19 como "Não Conforme", indicando que o item
     * A20 está fora de conformidade.
     *
     * @param evt o evento gerado pela seleção do botão de rádio do item A20
     */
    private void rbtA30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA30ActionPerformed

        situacao19 = "Não Conforme";
    }//GEN-LAST:event_rbtA30ActionPerformed

    /**
     * Método btnFotoA19ActionPerformed
     *
     * Acionado ao clicar no botão de foto do item A19. Define o nome da imagem
     * como "A19 - " e atribui à variável nome_foto. Em seguida, exibe o painel
     * jPanel1 e ajusta seu tamanho para 270x250 pixels, permitindo a
     * visualização ou captura da imagem correspondente ao item A19.
     *
     * @param evt o evento gerado pelo clique no botão de foto do item A19
     */
    private void btnFotoA19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoA19ActionPerformed

        nome1 = "A19 - ";
        nome_foto = nome1;
        jPanel1.setVisible(true);
        jPanel1.setSize(270, 250);
    }//GEN-LAST:event_btnFotoA19ActionPerformed

    /**
     * Método btnAnoA19ActionPerformed
     *
     * Acionado ao clicar no botão de anotação do item A19. Exibe ou oculta a
     * área de anotação txpAAnot19 com base no estado do botão btnAnoA19. Se
     * estiver selecionado, o campo é exibido com tamanho 170x100 pixels; caso
     * contrário, é ocultado.
     *
     * @param evt o evento gerado pela interação com o botão de anotação do item
     * A19
     */
    private void btnAnoA19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnoA19ActionPerformed

        if (btnAnoA19.isSelected()) {
            txpAAnot19.setVisible(true);
            txpAAnot19.setSize(170, 100);
            txtaA19.requestFocus();
        } else {
            txpAAnot19.setVisible(false);
        }
    }//GEN-LAST:event_btnAnoA19ActionPerformed

    /**
     * Método rbtA39ActionPerformed
     *
     * Acionado ao selecionar o botão de rádio "Conforme" do item A19. Define a
     * variável situacao18 como "Conforme", indicando que o item A19 está em
     * conformidade.
     *
     * @param evt o evento gerado pela seleção do botão de rádio do item A19
     */
    private void rbtA39ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA39ActionPerformed

        situacao18 = "Conforme";
    }//GEN-LAST:event_rbtA39ActionPerformed

    /**
     * Método rbtA29ActionPerformed
     *
     * Acionado ao selecionar o botão de rádio "Não Conforme" do item A19.
     * Define a variável situacao18 como "Não Conforme", indicando que o item
     * A19 está fora de conformidade.
     *
     * @param evt o evento gerado pela seleção do botão de rádio do item A19
     */
    private void rbtA29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA29ActionPerformed

        situacao18 = "Não Conforme";
    }//GEN-LAST:event_rbtA29ActionPerformed

    /**
     * Método btnFotoA18ActionPerformed
     *
     * Acionado ao clicar no botão de foto do item A18. Define o nome da imagem
     * como "A18 - " e atribui à variável nome_foto. Em seguida, exibe o painel
     * jPanel1 e ajusta seu tamanho para 270x250 pixels, permitindo a
     * visualização ou captura da imagem correspondente ao item A18.
     *
     * @param evt o evento gerado pelo clique no botão de foto do item A18
     */
    private void btnFotoA18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoA18ActionPerformed

        nome1 = "A18 - ";
        nome_foto = nome1;
        jPanel1.setVisible(true);
        jPanel1.setSize(270, 250);
    }//GEN-LAST:event_btnFotoA18ActionPerformed

    /**
     * Método btnAnoA18ActionPerformed
     *
     * Acionado ao clicar no botão de anotação do item A18. Exibe ou oculta a
     * área de anotação txpAAnot18 com base no estado do botão btnAnoA18. Se
     * estiver selecionado, o campo é exibido com tamanho 170x100 pixels; caso
     * contrário, é ocultado.
     *
     * @param evt o evento gerado pela interação com o botão de anotação do item
     * A18
     */
    private void btnAnoA18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnoA18ActionPerformed

        if (btnAnoA18.isSelected()) {
            txpAAnot18.setVisible(true);
            txpAAnot18.setSize(170, 100);
            txtaA18.requestFocus();
        } else {
            txpAAnot18.setVisible(false);
        }
    }//GEN-LAST:event_btnAnoA18ActionPerformed

    /**
     * Método rbtA38ActionPerformed
     *
     * Acionado ao selecionar o botão de rádio "Conforme" do item A18. Define a
     * variável situacao17 como "Conforme", indicando que o item A18 está em
     * conformidade.
     *
     * @param evt o evento gerado pela seleção do botão de rádio do item A18
     */
    private void rbtA38ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA38ActionPerformed

        situacao17 = "Conforme";
    }//GEN-LAST:event_rbtA38ActionPerformed

    /**
     * Método rbtA28ActionPerformed
     *
     * Acionado ao selecionar o botão de rádio "Não Conforme" do item A18.
     * Define a variável situacao17 como "Não Conforme", indicando que o item
     * A18 está fora de conformidade.
     *
     * @param evt o evento gerado pela seleção do botão de rádio do item A18
     */
    private void rbtA28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA28ActionPerformed

        situacao17 = "Não Conforme";
    }//GEN-LAST:event_rbtA28ActionPerformed

    /**
     * Método btnFotoA17ActionPerformed
     *
     * Acionado ao clicar no botão de foto do item A17. Define o nome da imagem
     * como "A17 - " e atribui à variável nome_foto. Em seguida, exibe o painel
     * jPanel1 e ajusta seu tamanho para 270x250 pixels, permitindo a
     * visualização ou captura da imagem correspondente ao item A17.
     *
     * @param evt o evento gerado pelo clique no botão de foto do item A17
     */
    private void btnFotoA17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoA17ActionPerformed

        nome1 = "A17 - ";
        nome_foto = nome1;
        jPanel1.setVisible(true);
        jPanel1.setSize(270, 250);
    }//GEN-LAST:event_btnFotoA17ActionPerformed

    /**
     * Método btnAnoA17ActionPerformed
     *
     * Acionado ao clicar no botão de anotação do item A17. Exibe ou oculta a
     * área de anotação txpAAnot17 com base no estado do botão btnAnoA17. Se
     * estiver selecionado, o campo é exibido com tamanho 170x100 pixels; caso
     * contrário, é ocultado.
     *
     * @param evt o evento gerado pela interação com o botão de anotação do item
     * A17
     */
    private void btnAnoA17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnoA17ActionPerformed

        if (btnAnoA17.isSelected()) {
            txpAAnot17.setVisible(true);
            txpAAnot17.setSize(170, 100);
            txtaA17.requestFocus();
        } else {
            txpAAnot17.setVisible(false);
        }
    }//GEN-LAST:event_btnAnoA17ActionPerformed

    /**
     * Método rbtA37ActionPerformed
     *
     * Acionado ao selecionar o botão de rádio "Conforme" do item A17. Define a
     * variável situacao16 como "Conforme", indicando que o item A17 está em
     * conformidade.
     *
     * @param evt o evento gerado pela seleção do botão de rádio do item A17
     */
    private void rbtA37ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA37ActionPerformed

        situacao16 = "Conforme";
    }//GEN-LAST:event_rbtA37ActionPerformed

    /**
     * Método rbtA27ActionPerformed
     *
     * Acionado ao selecionar o botão de rádio "Não Conforme" do item A17.
     * Define a variável situacao16 como "Não Conforme", indicando que o item
     * A17 está fora de conformidade.
     *
     * @param evt o evento gerado pela seleção do botão de rádio do item A17
     */
    private void rbtA27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA27ActionPerformed

        situacao16 = "Não Conforme";
    }//GEN-LAST:event_rbtA27ActionPerformed

    /**
     * Método btnFotoA16ActionPerformed
     *
     * Acionado ao clicar no botão de foto do item A16. Define o nome da imagem
     * como "A16 - " e atribui à variável nome_foto. Em seguida, exibe o painel
     * jPanel1 e ajusta seu tamanho para 270x250 pixels, permitindo a
     * visualização ou captura da imagem correspondente ao item A16.
     *
     * @param evt o evento gerado pelo clique no botão de foto do item A16
     */
    private void btnFotoA16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoA16ActionPerformed

        nome1 = "A16 - ";
        nome_foto = nome1;
        jPanel1.setVisible(true);
        jPanel1.setSize(270, 250);
    }//GEN-LAST:event_btnFotoA16ActionPerformed

    /**
     * Método btnAnoA16ActionPerformed
     *
     * Acionado ao clicar no botão de anotação do item A16. Exibe ou oculta a
     * área de anotação txpAAnot16 com base no estado do botão btnAnoA16. Se
     * estiver selecionado, o campo é exibido com tamanho 170x100 pixels; caso
     * contrário, é ocultado.
     *
     * @param evt o evento gerado pela interação com o botão de anotação do item
     * A16
     */
    private void btnAnoA16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnoA16ActionPerformed

        if (btnAnoA16.isSelected()) {
            txpAAnot16.setVisible(true);
            txpAAnot16.setSize(170, 100);
            txtaA16.requestFocus();
        } else {
            txpAAnot16.setVisible(false);
        }
    }//GEN-LAST:event_btnAnoA16ActionPerformed

    /**
     * Método rbtA36ActionPerformed
     *
     * Acionado ao selecionar o botão de rádio "Conforme" do item A16. Define a
     * variável situacao15 como "Conforme", indicando que o item A16 está em
     * conformidade.
     *
     * @param evt o evento gerado pela seleção do botão de rádio do item A16
     */
    private void rbtA36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA36ActionPerformed

        situacao15 = "Conforme";
    }//GEN-LAST:event_rbtA36ActionPerformed

    /**
     * Método rbtA26ActionPerformed
     *
     * Acionado ao selecionar o botão de rádio "Não Conforme" do item A16.
     * Define a variável situacao15 como "Não Conforme", indicando que o item
     * A16 está fora de conformidade.
     *
     * @param evt o evento gerado pela seleção do botão de rádio do item A16
     */
    private void rbtA26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA26ActionPerformed

        situacao15 = "Não Conforme";
    }//GEN-LAST:event_rbtA26ActionPerformed

    /**
     * Método btnFotoA15ActionPerformed
     *
     * Acionado ao clicar no botão de foto do item A15. Define o nome da imagem
     * como "A15 - " e atribui à variável nome_foto. Em seguida, exibe o painel
     * jPanel1 e ajusta seu tamanho para 270x250 pixels, permitindo a
     * visualização ou captura da imagem correspondente ao item A15.
     *
     * @param evt o evento gerado pelo clique no botão de foto do item A15
     */
    private void btnFotoA15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoA15ActionPerformed

        nome1 = "A15 - ";
        nome_foto = nome1;
        jPanel1.setVisible(true);
        jPanel1.setSize(270, 250);
    }//GEN-LAST:event_btnFotoA15ActionPerformed

    /**
     * Método btnAnoA15ActionPerformed
     *
     * Acionado ao clicar no botão de anotação do item A15. Exibe ou oculta a
     * área de anotação txpAAnot15 com base no estado do botão btnAnoA15. Se
     * estiver selecionado, o campo é exibido com tamanho 170x100 pixels; caso
     * contrário, é ocultado.
     *
     * @param evt o evento gerado pela interação com o botão de anotação do item
     * A15
     */
    private void btnAnoA15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnoA15ActionPerformed

        if (btnAnoA15.isSelected()) {
            txpAAnot15.setVisible(true);
            txpAAnot15.setSize(170, 100);
            txtaA15.requestFocus();
        } else {
            txpAAnot15.setVisible(false);
        }
    }//GEN-LAST:event_btnAnoA15ActionPerformed

    /**
     * Método rbtA35ActionPerformed
     *
     * Acionado ao selecionar o botão de rádio "Conforme" do item A15. Define a
     * variável situacao14 como "Conforme", indicando que o item A15 está em
     * conformidade.
     *
     * @param evt o evento gerado pela seleção do botão de rádio do item A15
     */
    private void rbtA35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA35ActionPerformed

        situacao14 = "Conforme";
    }//GEN-LAST:event_rbtA35ActionPerformed

    /**
     * Método rbtA25ActionPerformed
     *
     * Acionado ao selecionar o botão de rádio "Não Conforme" do item A15.
     * Define a variável situacao14 como "Não Conforme", indicando que o item
     * A15 está fora de conformidade.
     *
     * @param evt o evento gerado pela seleção do botão de rádio do item A15
     */
    private void rbtA25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA25ActionPerformed

        situacao14 = "Não Conforme";
    }//GEN-LAST:event_rbtA25ActionPerformed

    /**
     * Método btnFotoA14ActionPerformed
     *
     * Define o nome da imagem como "A14 - " e exibe o painel jPanel1 com
     * tamanho 270x250 pixels.
     *
     * @param evt o evento gerado pelo clique no botão de foto do item A14
     */
    private void btnFotoA14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoA14ActionPerformed

        nome1 = "A14 - ";
        nome_foto = nome1;
        jPanel1.setVisible(true);
        jPanel1.setSize(270, 250);
    }//GEN-LAST:event_btnFotoA14ActionPerformed

    /**
     * Método btnFotoA14ActionPerformed
     *
     * Define o nome da imagem como "A14 - " e exibe o painel jPanel1 com
     * tamanho 270x250 pixels.
     *
     * @param evt o evento gerado pelo clique no botão de foto do item A14
     */
    private void btnAnoA14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnoA14ActionPerformed

        if (btnAnoA14.isSelected()) {
            txpAAnot14.setVisible(true);
            txpAAnot14.setSize(170, 100);
            txtaA14.requestFocus();
        } else {
            txpAAnot14.setVisible(false);
        }
    }//GEN-LAST:event_btnAnoA14ActionPerformed

    /**
     * Método rbtA34ActionPerformed
     *
     * Define a variável situacao13 como "Conforme" ao selecionar o botão
     * correspondente ao item A14.
     *
     * @param evt o evento gerado pela seleção do botão de rádio do item A14
     */
    private void rbtA34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA34ActionPerformed

        situacao13 = "Conforme";
    }//GEN-LAST:event_rbtA34ActionPerformed

    /**
     * Método rbtA24ActionPerformed
     *
     * Define a variável situacao13 como "Não Conforme" ao selecionar o botão
     * correspondente ao item A14.
     *
     * @param evt o evento gerado pela seleção do botão de rádio do item A14
     */
    private void rbtA24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA24ActionPerformed

        situacao13 = "Não Conforme";
    }//GEN-LAST:event_rbtA24ActionPerformed

    /**
     * Método btnAnoA13ActionPerformed
     *
     * Exibe ou oculta a área de anotação txpAAnot13 com base no estado do botão
     * btnAnoA13.
     *
     * @param evt o evento gerado pela interação com o botão de anotação do item
     * A13
     */
    private void btnFotoA13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoA13ActionPerformed

        nome1 = "A13 - ";
        nome_foto = nome1;
        jPanel1.setVisible(true);
        jPanel1.setSize(270, 250);
    }//GEN-LAST:event_btnFotoA13ActionPerformed

    /**
     * Método rbtA33ActionPerformed
     *
     * Define a variável situacao12 como "Conforme" ao selecionar o botão
     * correspondente ao item A13.
     *
     * @param evt o evento gerado pela seleção do botão de rádio do item A13
     */
    private void btnAnoA13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnoA13ActionPerformed

        if (btnAnoA13.isSelected()) {
            txpAAnot13.setVisible(true);
            txpAAnot13.setSize(170, 100);
            txtaA13.requestFocus();
        } else {
            txpAAnot13.setVisible(false);
        }
    }//GEN-LAST:event_btnAnoA13ActionPerformed

    /**
     * Método rbtA33ActionPerformed
     *
     * Define a variável situacao12 como "Conforme" ao selecionar o botão
     * correspondente ao item A13.
     *
     * @param evt o evento gerado pela seleção do botão de rádio do item A13
     */
    private void rbtA33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA33ActionPerformed

        situacao12 = "Conforme";
    }//GEN-LAST:event_rbtA33ActionPerformed

    /**
     * Método rbtA23ActionPerformed
     *
     * Define a variável situacao12 como "Não Conforme" ao selecionar o botão
     * correspondente ao item A13.
     *
     * @param evt o evento gerado pela seleção do botão de rádio do item A13
     */
    private void rbtA23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA23ActionPerformed

        situacao12 = "Não Conforme";
    }//GEN-LAST:event_rbtA23ActionPerformed

    /**
     * Método btnFotoA12ActionPerformed
     *
     * Define o nome da imagem como "A12 - " e exibe o painel jPanel1 com
     * tamanho 270x250 pixels.
     *
     * @param evt o evento gerado pelo clique no botão de foto do item A12
     */
    private void btnFotoA12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoA12ActionPerformed

        nome1 = "A12 - ";
        nome_foto = nome1;
        jPanel1.setVisible(true);
        jPanel1.setSize(270, 250);
    }//GEN-LAST:event_btnFotoA12ActionPerformed

    /**
     * Método btnAnoA12ActionPerformed
     *
     * Exibe ou oculta a área de anotação txpAAnot12 com base no estado do botão
     * btnAnoA12.
     *
     * @param evt o evento gerado pela interação com o botão de anotação do item
     * A12
     */
    private void btnAnoA12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnoA12ActionPerformed

        if (btnAnoA12.isSelected()) {
            txpAAnot12.setVisible(true);
            txpAAnot12.setSize(170, 100);
            txtaA12.requestFocus();
        } else {
            txpAAnot12.setVisible(false);
        }
    }//GEN-LAST:event_btnAnoA12ActionPerformed

    /**
     * Método rbtA32ActionPerformed
     *
     * Define a variável situacao11 como "Conforme" ao selecionar o botão
     * correspondente ao item A12.
     *
     * @param evt o evento gerado pela seleção do botão de rádio do item A12
     */
    private void rbtA32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA32ActionPerformed

        situacao11 = "Conforme";
    }//GEN-LAST:event_rbtA32ActionPerformed

    /**
     * Método btnFotoA11ActionPerformed
     *
     * Define o nome da imagem como "A11 - " e exibe o painel jPanel1 com
     * tamanho 270x250 pixels.
     *
     * @param evt o evento gerado pelo clique no botão de foto do item A11
     */
    private void rbtA22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA22ActionPerformed

        situacao11 = "Não Conforme";
    }//GEN-LAST:event_rbtA22ActionPerformed

    /**
     * Método btnAnoA11ActionPerformed
     *
     * Exibe ou oculta a área de anotação txpAAnot11 com base no estado do botão
     * btnAnoA11.
     *
     * @param evt o evento gerado pela interação com o botão de anotação do item
     * A11
     */
    private void btnFotoA11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoA11ActionPerformed

        nome1 = "A11 - ";
        nome_foto = nome1;
        jPanel1.setVisible(true);
        jPanel1.setSize(270, 250);
    }//GEN-LAST:event_btnFotoA11ActionPerformed

    /**
     * Método rbtA31ActionPerformed
     *
     * Acionado ao selecionar o botão de rádio "Conforme" do item A11. Define a
     * variável situacao10 como "Conforme".
     *
     * @param evt o evento gerado pela seleção do botão de rádio do item A11
     */
    private void btnAnoA11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnoA11ActionPerformed

        if (btnAnoA11.isSelected()) {
            txpAAnot11.setVisible(true);
            txpAAnot11.setSize(170, 100);
            txtaA11.requestFocus();
        } else {
            txpAAnot11.setVisible(false);
        }
    }//GEN-LAST:event_btnAnoA11ActionPerformed

    /**
     * Método rbtA31ActionPerformed
     *
     * Acionado ao selecionar o botão de rádio "Conforme" do item A11. Define a
     * variável situacao10 como "Conforme".
     *
     * @param evt o evento gerado pela seleção do botão de rádio do item A11
     */
    private void rbtA31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA31ActionPerformed

        situacao10 = "Conforme";
    }//GEN-LAST:event_rbtA31ActionPerformed

    /**
     * Método rbtA21ActionPerformed
     *
     * Acionado ao selecionar o botão de rádio "Não Conforme" do item A11.
     * Define a variável situacao10 como "Não Conforme".
     *
     * @param evt o evento gerado pela seleção do botão de rádio do item A11
     */
    private void rbtA21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA21ActionPerformed

        situacao10 = "Não Conforme";
    }//GEN-LAST:event_rbtA21ActionPerformed

    /**
     * Método btnFotoA10ActionPerformed
     *
     * Acionado ao clicar no botão de foto do item A10. Define o nome da imagem
     * como "A10 - " e atribui à variável nome_foto. Em seguida, exibe o painel
     * jPanel1 e ajusta seu tamanho para 270x250 pixels, permitindo a
     * visualização ou captura da imagem correspondente ao item A10.
     *
     * @param evt o evento gerado pelo clique no botão de foto do item A10
     */
    private void btnFotoA10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoA10ActionPerformed

        nome1 = "A10 - ";
        nome_foto = nome1;
        jPanel1.setVisible(true);
        jPanel1.setSize(270, 250);
    }//GEN-LAST:event_btnFotoA10ActionPerformed

    /**
     * Método btnAnoA10ActionPerformed
     *
     * Acionado ao clicar no botão de anotação do item A10. Exibe ou oculta a
     * área de anotação txpAAnot10 com base no estado do botão btnAnoA10. Se
     * estiver selecionado, o campo é exibido com tamanho 170x100 pixels; caso
     * contrário, é ocultado.
     *
     * @param evt o evento gerado pela interação com o botão de anotação do item
     * A10
     */
    private void btnAnoA10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnoA10ActionPerformed

        if (btnAnoA10.isSelected()) {
            txpAAnot10.setVisible(true);
            txpAAnot10.setSize(170, 100);
            txtaA10.requestFocus();
        } else {
            txpAAnot10.setVisible(false);
        }
    }//GEN-LAST:event_btnAnoA10ActionPerformed

    /**
     * Método rbtA20ActionPerformed
     *
     * Acionado ao selecionar o botão de rádio "Conforme" do item A10. Define a
     * variável situacao9 como "Conforme", indicando que o item A10 está em
     * conformidade.
     *
     * @param evt o evento gerado pela seleção do botão de rádio do item A10
     */
    private void rbtA20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA20ActionPerformed

        situacao9 = "Conforme";
    }//GEN-LAST:event_rbtA20ActionPerformed

    /**
     * Método rbtA10ActionPerformed
     *
     * Acionado ao selecionar o botão de rádio "Não Conforme" do item A10.
     * Define a variável situacao9 como "Não Conforme", indicando que o item A10
     * está fora de conformidade.
     *
     * @param evt o evento gerado pela seleção do botão de rádio do item A10
     */
    private void rbtA10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA10ActionPerformed

        situacao9 = "Não Conforme";
    }//GEN-LAST:event_rbtA10ActionPerformed

    /**
     * Método btnFotoA9ActionPerformed
     *
     * Acionado ao clicar no botão de foto do item A9. Define o nome da imagem
     * como "A9 - " e atribui à variável nome_foto. Em seguida, exibe o painel
     * jPanel1 e ajusta seu tamanho para 270x250 pixels, permitindo a
     * visualização ou captura da imagem correspondente ao item A9.
     *
     * @param evt o evento gerado pelo clique no botão de foto do item A9
     */
    private void btnFotoA9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoA9ActionPerformed

        nome1 = "A9 - ";
        nome_foto = nome1;
        jPanel1.setVisible(true);
        jPanel1.setSize(270, 250);
    }//GEN-LAST:event_btnFotoA9ActionPerformed

    /**
     * Método btnAnoA9ActionPerformed
     *
     * Acionado ao clicar no botão de anotação do item A9. Exibe ou oculta a
     * área de anotação txpAAnot9 com base no estado do botão btnAnoA9. Se
     * estiver selecionado, o campo é exibido com tamanho 170x100 pixels; caso
     * contrário, é ocultado.
     *
     * @param evt o evento gerado pela interação com o botão de anotação do item
     * A9
     */
    private void btnAnoA9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnoA9ActionPerformed

        if (btnAnoA9.isSelected()) {
            txpAAnot9.setVisible(true);
            txpAAnot9.setSize(170, 100);
            txtaA9.requestFocus();
        } else {
            txpAAnot9.setVisible(false);
        }
    }//GEN-LAST:event_btnAnoA9ActionPerformed

    /**
     * Método rbtA19ActionPerformed
     *
     * Acionado ao selecionar o botão de rádio "Conforme" do item A9. Define a
     * variável situacao8 como "Conforme", indicando que o item A9 está em
     * conformidade.
     *
     * @param evt o evento gerado pela seleção do botão de rádio do item A9
     */
    private void rbtA19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA19ActionPerformed

        situacao8 = "Conforme";
    }//GEN-LAST:event_rbtA19ActionPerformed

    /**
     * Método rbtA9ActionPerformed
     *
     * Acionado ao selecionar o botão de rádio "Não Conforme" do item A9. Define
     * a variável situacao8 como "Não Conforme", indicando que o item A9 está
     * fora de conformidade.
     *
     * @param evt o evento gerado pela seleção do botão de rádio do item A9
     */
    private void rbtA9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA9ActionPerformed

        situacao8 = "Não Conforme";
    }//GEN-LAST:event_rbtA9ActionPerformed

    /**
     * Método btnFotoA8ActionPerformed
     *
     * Acionado ao clicar no botão de foto do item A8. Define o nome da imagem
     * como "A8 - " e atribui à variável nome_foto. Em seguida, exibe o painel
     * jPanel1 e ajusta seu tamanho para 270x250 pixels, permitindo a
     * visualização ou captura da imagem correspondente ao item A8.
     *
     * @param evt o evento gerado pelo clique no botão de foto do item A8
     */
    private void btnFotoA8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoA8ActionPerformed

        nome1 = "A8 - ";
        nome_foto = nome1;
        jPanel1.setVisible(true);
        jPanel1.setSize(270, 250);
    }//GEN-LAST:event_btnFotoA8ActionPerformed

    /**
     * Método btnAnoA8ActionPerformed
     *
     * Acionado ao clicar no botão de anotação do item A8. Exibe ou oculta a
     * área de anotação txpAAnot8 com base no estado do botão btnAnoA8. Se
     * estiver selecionado, o campo é exibido com tamanho 170x100 pixels; caso
     * contrário, é ocultado.
     *
     * @param evt o evento gerado pela interação com o botão de anotação do item
     * A8
     */
    private void btnAnoA8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnoA8ActionPerformed

        if (btnAnoA8.isSelected()) {
            txpAAnot8.setVisible(true);
            txpAAnot8.setSize(170, 100);
            txtaA8.requestFocus();
        } else {
            txpAAnot8.setVisible(false);
        }
    }//GEN-LAST:event_btnAnoA8ActionPerformed

    /**
     * Método rbtA18ActionPerformed
     *
     * Acionado ao selecionar o botão de rádio "Conforme" do item A8. Define a
     * variável situacao7 como "Conforme", indicando que o item A8 está em
     * conformidade.
     *
     * @param evt o evento gerado pela seleção do botão de rádio do item A8
     */
    private void rbtA18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA18ActionPerformed

        situacao7 = "Conforme";
    }//GEN-LAST:event_rbtA18ActionPerformed

    /**
     * Método rbtA18ActionPerformed
     *
     * Acionado ao selecionar o botão de rádio "Conforme" do item A8. Define a
     * variável situacao7 como "Conforme", indicando que o item A8 está em
     * conformidade.
     *
     * @param evt o evento gerado pela seleção do botão de rádio do item A8
     */
    private void rbtA8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA8ActionPerformed

        situacao7 = "Não Conforme";
    }//GEN-LAST:event_rbtA8ActionPerformed

    /**
     * Método btnFotoA7ActionPerformed
     *
     * Acionado ao clicar no botão de foto do item A7. Define o nome da imagem
     * como "A7 - " e atribui à variável nome_foto. Em seguida, exibe o painel
     * jPanel1 e ajusta seu tamanho para 270x250 pixels, permitindo a
     * visualização ou captura da imagem correspondente ao item A7.
     *
     * @param evt o evento gerado pelo clique no botão de foto do item A7
     */
    private void btnFotoA7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoA7ActionPerformed

        nome1 = "A7 - ";
        nome_foto = nome1;
        jPanel1.setVisible(true);
        jPanel1.setSize(270, 250);
    }//GEN-LAST:event_btnFotoA7ActionPerformed

    /**
     * Método btnAnoA7ActionPerformed
     *
     * Acionado ao clicar no botão de anotação do item A7. Exibe ou oculta a
     * área de anotação txpAAnot7 com base no estado do botão btnAnoA7. Se
     * estiver selecionado, o campo é exibido com tamanho 170x100 pixels; caso
     * contrário, é ocultado.
     *
     * @param evt o evento gerado pela interação com o botão de anotação do item
     * A7
     */
    private void btnAnoA7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnoA7ActionPerformed

        if (btnAnoA7.isSelected()) {
            txpAAnot7.setVisible(true);
            txpAAnot7.setSize(170, 100);
            txtaA7.requestFocus();
        } else {
            txpAAnot7.setVisible(false);
        }
    }//GEN-LAST:event_btnAnoA7ActionPerformed

    /**
     * Método rbtA17ActionPerformed
     *
     * Acionado ao selecionar o botão de rádio "Conforme" do item A7. Define a
     * variável situacao6 como "Conforme", indicando que o item A7 está em
     * conformidade.
     *
     * @param evt o evento gerado pela seleção do botão de rádio do item A7
     */
    private void rbtA17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA17ActionPerformed

        situacao6 = "Conforme";
    }//GEN-LAST:event_rbtA17ActionPerformed

    /**
     * Método rbtA7ActionPerformed
     *
     * Acionado ao selecionar o botão de rádio "Não Conforme" do item A7. Define
     * a variável situacao6 como "Não Conforme", indicando que o item A7 está
     * fora de conformidade.
     *
     * @param evt o evento gerado pela seleção do botão de rádio do item A7
     */
    private void rbtA7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA7ActionPerformed

        situacao6 = "Não Conforme";
    }//GEN-LAST:event_rbtA7ActionPerformed

    /**
     * Método btnFotoA6ActionPerformed
     *
     * Acionado ao clicar no botão de foto do item A6. Define o nome da imagem
     * como "A6 - " e atribui à variável nome_foto. Em seguida, exibe o painel
     * jPanel1 e ajusta seu tamanho para 270x250 pixels, permitindo a
     * visualização ou captura da imagem correspondente ao item A6.
     *
     * @param evt o evento gerado pelo clique no botão de foto do item A6
     */
    private void btnFotoA6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoA6ActionPerformed

        nome1 = "A6 - ";
        nome_foto = nome1;
        jPanel1.setVisible(true);
        jPanel1.setSize(270, 250);
    }//GEN-LAST:event_btnFotoA6ActionPerformed

    /**
     * Método btnAnoA6ActionPerformed
     *
     * Acionado ao clicar no botão de anotação do item A6. Exibe ou oculta a
     * área de anotação txpAAnot6 com base no estado do botão btnAnoA6. Se
     * estiver selecionado, o campo é exibido com tamanho 170x100 pixels; caso
     * contrário, é ocultado.
     *
     * @param evt o evento gerado pela interação com o botão de anotação do item
     * A6
     */
    private void btnAnoA6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnoA6ActionPerformed

        if (btnAnoA6.isSelected()) {
            txpAAnot6.setVisible(true);
            txpAAnot6.setSize(170, 100);
            txtaA6.requestFocus();
        } else {
            txpAAnot6.setVisible(false);
        }
    }//GEN-LAST:event_btnAnoA6ActionPerformed

    /**
     * Método rbtA16ActionPerformed
     *
     * Acionado ao selecionar o botão de rádio "Conforme" do item A6. Define a
     * variável situacao5 como "Conforme", indicando que o item A6 está em
     * conformidade.
     *
     * @param evt o evento gerado pela seleção do botão de rádio do item A6
     */
    private void rbtA16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA16ActionPerformed

        situacao5 = "Conforme";
    }//GEN-LAST:event_rbtA16ActionPerformed

    /**
     * Método rbtA6ActionPerformed
     *
     * Acionado ao selecionar o botão de rádio "Não Conforme" do item A6. Define
     * a variável situacao5 como "Não Conforme", indicando que o item A6 está
     * fora de conformidade.
     *
     * @param evt o evento gerado pela seleção do botão de rádio do item A6
     */
    private void rbtA6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA6ActionPerformed

        situacao5 = "Não Conforme";
    }//GEN-LAST:event_rbtA6ActionPerformed

    /**
     * Método btnFotoA5ActionPerformed
     *
     * Acionado ao clicar no botão de foto do item A5. Define o nome da imagem
     * como "A5 - " e atribui à variável nome_foto. Em seguida, exibe o painel
     * jPanel1 e ajusta seu tamanho para 270x250 pixels, permitindo a
     * visualização ou captura da imagem correspondente ao item A5.
     *
     * @param evt o evento gerado pelo clique no botão de foto do item A5
     */
    private void btnFotoA5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoA5ActionPerformed

        nome1 = "A5 - ";
        nome_foto = nome1;
        jPanel1.setVisible(true);
        jPanel1.setSize(270, 250);
    }//GEN-LAST:event_btnFotoA5ActionPerformed

    /**
     * Método btnAnoA5ActionPerformed
     *
     * Acionado ao clicar no botão de anotação do item A5. Exibe ou oculta a
     * área de anotação txpAAnot5 com base no estado do botão btnAnoA5. Se
     * estiver selecionado, o campo é exibido com tamanho 170x100 pixels; caso
     * contrário, é ocultado.
     *
     * @param evt o evento gerado pela interação com o botão de anotação do item
     * A5
     */
    private void btnAnoA5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnoA5ActionPerformed

        if (btnAnoA5.isSelected()) {
            txpAAnot5.setVisible(true);
            txpAAnot5.setSize(170, 100);
            txtaA5.requestFocus();
        } else {
            txpAAnot5.setVisible(false);
        }
    }//GEN-LAST:event_btnAnoA5ActionPerformed

    /**
     * Método rbtA15ActionPerformed
     *
     * Acionado ao selecionar o botão de rádio "Conforme" do item A5. Define a
     * variável situacao4 como "Conforme", indicando que o item A5 está em
     * conformidade.
     *
     * @param evt o evento gerado pela seleção do botão de rádio do item A5
     */
    private void rbtA15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA15ActionPerformed

        situacao4 = "Conforme";
    }//GEN-LAST:event_rbtA15ActionPerformed

    /**
     * Método rbtA5ActionPerformed
     *
     * Acionado ao selecionar o botão de rádio "Não Conforme" do item A5. Define
     * a variável situacao4 como "Não Conforme", indicando que o item A5 está
     * fora de conformidade.
     *
     * @param evt o evento gerado pela seleção do botão de rádio do item A5
     */
    private void rbtA5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA5ActionPerformed

        situacao4 = "Não Conforme";
    }//GEN-LAST:event_rbtA5ActionPerformed

    /**
     * Método btnFotoA4ActionPerformed
     *
     * Acionado ao clicar no botão de foto do item A4. Define o nome da imagem
     * como "A4 - " e atribui à variável nome_foto. Em seguida, exibe o painel
     * jPanel1 e ajusta seu tamanho para 270x250 pixels, permitindo a
     * visualização ou captura da imagem correspondente ao item A4.
     *
     * @param evt o evento gerado pelo clique no botão de foto do item A4
     */
    private void btnFotoA4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoA4ActionPerformed

        nome1 = "A4 - ";
        nome_foto = nome1;
        jPanel1.setVisible(true);
        jPanel1.setSize(270, 250);
    }//GEN-LAST:event_btnFotoA4ActionPerformed

    /**
     * Método btnAnoA4ActionPerformed
     *
     * Acionado ao clicar no botão de anotação do item A4. Exibe ou oculta a
     * área de anotação txpAAnot4 com base no estado do botão btnAnoA4. Se
     * estiver selecionado, o campo é exibido com tamanho 170x100 pixels; caso
     * contrário, é ocultado.
     *
     * @param evt o evento gerado pela interação com o botão de anotação do item
     * A4
     */
    private void btnAnoA4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnoA4ActionPerformed

        if (btnAnoA4.isSelected()) {
            txpAAnot4.setVisible(true);
            txpAAnot4.setSize(170, 100);
            txtaA4.requestFocus();
        } else {
            txpAAnot4.setVisible(false);
        }
    }//GEN-LAST:event_btnAnoA4ActionPerformed

    /**
     * Método rbtA14ActionPerformed
     *
     * Acionado ao selecionar o botão de rádio "Conforme" do item A4. Define a
     * variável situacao3 como "Conforme", indicando que o item A4 está em
     * conformidade.
     *
     * @param evt o evento gerado pela seleção do botão de rádio do item A4
     */
    private void rbtA14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA14ActionPerformed

        situacao3 = "Conforme";
    }//GEN-LAST:event_rbtA14ActionPerformed

    /**
     * Método rbtA4ActionPerformed
     *
     * Acionado ao selecionar o botão de rádio "Não Conforme" do item A4. Define
     * a variável situacao3 como "Não Conforme", indicando que o item A4 está
     * fora de conformidade.
     *
     * @param evt o evento gerado pela seleção do botão de rádio do item A4
     */
    private void rbtA4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA4ActionPerformed

        situacao3 = "Não Conforme";
    }//GEN-LAST:event_rbtA4ActionPerformed

    /**
     * Método btnFotoA3ActionPerformed
     *
     * Acionado ao clicar no botão de foto do item A3. Define o nome da imagem
     * como "A3 - " e atribui à variável nome_foto. Em seguida, exibe o painel
     * jPanel1 e ajusta seu tamanho para 270x250 pixels, permitindo a
     * visualização ou captura da imagem correspondente ao item A3.
     *
     * @param evt o evento gerado pelo clique no botão de foto do item A3
     */
    private void btnFotoA3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoA3ActionPerformed

        nome1 = "A3 - ";
        nome_foto = nome1;
        jPanel1.setVisible(true);
        jPanel1.setSize(270, 250);
    }//GEN-LAST:event_btnFotoA3ActionPerformed

    /**
     * Método btnAnoA3ActionPerformed
     *
     * Acionado ao clicar no botão de anotação do item A3. Exibe ou oculta a
     * área de anotação txpAAnot3 com base no estado do botão btnAnoA3. Se
     * estiver selecionado, o campo é exibido com tamanho 170x100 pixels; caso
     * contrário, é ocultado.
     *
     * @param evt o evento gerado pela interação com o botão de anotação do item
     * A3
     */
    private void btnAnoA3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnoA3ActionPerformed

        if (btnAnoA3.isSelected()) {
            txpAAnot3.setVisible(true);
            txpAAnot3.setSize(170, 100);
            txtaA3.requestFocus();
        } else {
            txpAAnot3.setVisible(false);
        }
    }//GEN-LAST:event_btnAnoA3ActionPerformed

    /**
     * Método rbtA13ActionPerformed
     *
     * Acionado ao selecionar o botão de rádio "Conforme" do item A3. Define a
     * variável situacao2 como "Conforme", indicando que o item A3 está em
     * conformidade.
     *
     * @param evt o evento gerado pela seleção do botão de rádio do item A3
     */
    private void rbtA13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA13ActionPerformed

        situacao2 = "Conforme";
    }//GEN-LAST:event_rbtA13ActionPerformed

    /**
     * Método rbtA3ActionPerformed
     *
     * Acionado ao selecionar o botão de rádio "Não Conforme" do item A3. Define
     * a variável situacao2 como "Não Conforme", indicando que o item A3 está
     * fora de conformidade.
     *
     * @param evt o evento gerado pela seleção do botão de rádio do item A3
     */
    private void rbtA3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA3ActionPerformed

        situacao2 = "Não Conforme";
    }//GEN-LAST:event_rbtA3ActionPerformed

    /**
     * Método btnAnoA2ActionPerformed
     *
     * Acionado ao clicar no botão de anotação do item A2. Exibe ou oculta a
     * área de anotação txpAAnot2 com base no estado do botão btnAnoA2. Se
     * estiver selecionado, o campo é exibido com tamanho 170x100 pixels; caso
     * contrário, é ocultado.
     *
     * @param evt o evento gerado pela interação com o botão de anotação do item
     * A2
     */
    private void btnAnoA2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnoA2ActionPerformed

        if (btnAnoA2.isSelected()) {
            txpAAnot2.setVisible(true);
            txpAAnot2.setSize(170, 100);
            txtaA2.requestFocus();
        } else {
            txpAAnot2.setVisible(false);
        }
    }//GEN-LAST:event_btnAnoA2ActionPerformed

    /**
     * Método btnFotoA2ActionPerformed
     *
     * Acionado ao clicar no botão de foto do item A2. Define o nome da imagem
     * como "A2 - " e atribui à variável nome_foto. Em seguida, exibe o painel
     * jPanel1 e ajusta seu tamanho para 270x250 pixels, permitindo a
     * visualização ou captura da imagem correspondente ao item A2.
     *
     * @param evt o evento gerado pelo clique no botão de foto do item A2
     */
    private void btnFotoA2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoA2ActionPerformed

        nome1 = "A2 - ";
        nome_foto = nome1;
        jPanel1.setVisible(true);
        jPanel1.setSize(270, 250);
    }//GEN-LAST:event_btnFotoA2ActionPerformed

    /**
     * Método rbtA2ActionPerformed
     *
     * Acionado ao selecionar o botão de rádio "Não Conforme" do item A2. Define
     * a variável situacao1 como "Não Conforme", indicando que o item A2 está
     * fora de conformidade.
     *
     * @param evt o evento gerado pela seleção do botão de rádio do item A2
     */
    private void rbtA2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA2ActionPerformed

        situacao1 = "Não Conforme";
    }//GEN-LAST:event_rbtA2ActionPerformed

    /**
     * Método rbtA12ActionPerformed
     *
     * Acionado ao selecionar o botão de rádio "Conforme" do item A1. Define a
     * variável situacao1 como "Conforme", indicando que o item A1 está em
     * conformidade.
     *
     * @param evt o evento gerado pela seleção do botão de rádio do item A1
     */
    private void rbtA12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA12ActionPerformed

        situacao1 = "Conforme";
    }//GEN-LAST:event_rbtA12ActionPerformed

    /**
     * Método btnEncPreActionPerformed
     *
     * Acionado ao clicar no botão de encerramento da preventiva. Exibe uma
     * caixa de diálogo de confirmação. Se o usuário confirmar: - Obtém a data e
     * hora atual formatada e define no campo txtDatFin. - Chama o método
     * cauc_tempo() para calcular o tempo da preventiva. - Define o status como
     * "Encerrada" no campo txtStaPre. - Reinicia o processo preventivo com
     * iniciar_prev().
     *
     * @param evt o evento gerado pelo clique no botão de encerramento
     */
    private void btnEncPreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEncPreActionPerformed

        int confirma = JOptionPane.showConfirmDialog(null, "Deseja Ecerrar a Preventiva?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            DateTimeFormatter data_hora = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
            ZonedDateTime zdtNow = ZonedDateTime.now();
            String format = data_hora.format(zdtNow);
            txtDatFin.setText(format);
            cauc_tempo();

            String status = "Encerrada";
            txtStaPre.setText(status);
            iniciar_prev();
        }
    }//GEN-LAST:event_btnEncPreActionPerformed

    /**
     * Método btnFotoA1ActionPerformed
     *
     * Acionado ao clicar no botão de foto do item A1. Define o nome da imagem
     * como "A1 - " e atribui à variável nome_foto. Em seguida, exibe o painel
     * jPanel1 e ajusta seu tamanho para 270x250 pixels, permitindo a
     * visualização ou captura da imagem correspondente ao item A1.
     *
     * @param evt o evento gerado pelo clique no botão de foto do item A1
     */
    private void btnFotoA1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoA1ActionPerformed

        nome1 = "A1 - ";
        nome_foto = nome1;
        jPanel1.setVisible(true);
        jPanel1.setSize(270, 250);
    }//GEN-LAST:event_btnFotoA1ActionPerformed

    /**
     * Método btnAnoA1ActionPerformed
     *
     * Acionado ao clicar no botão de anotação do item A1. Exibe ou oculta a
     * área de anotação txpAAnot1 com base no estado do botão btnAnoA1. Se
     * estiver selecionado, o campo é exibido com tamanho 170x100 pixels; caso
     * contrário, é ocultado.
     *
     * @param evt o evento gerado pela interação com o botão de anotação do item
     * A1
     */
    private void btnAnoA1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnoA1ActionPerformed

        if (btnAnoA1.isSelected()) {
            txpAAnot1.setVisible(true);
            txpAAnot1.setSize(170, 100);
            txtaA1.requestFocus();
        } else {
            txpAAnot1.setVisible(false);
        }
    }//GEN-LAST:event_btnAnoA1ActionPerformed

    /**
     * Método rbtA11ActionPerformed
     *
     * Acionado ao selecionar o botão de rádio "Conforme" do item A1. Define a
     * variável situacao como "Conforme", indicando que o item A1 está em
     * conformidade.
     *
     * @param evt o evento gerado pela seleção do botão de rádio do item A1
     */
    private void rbtA11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA11ActionPerformed

        situacao = "Conforme";
    }//GEN-LAST:event_rbtA11ActionPerformed

    /**
     * Método rbtA1ActionPerformed
     *
     * Acionado ao selecionar o botão de rádio "Não Conforme" do item A1. Define
     * a variável situacao como "Não Conforme", indicando que o item A1 está
     * fora de conformidade.
     *
     * @param evt o evento gerado pela seleção do botão de rádio do item A1
     */
    private void rbtA1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA1ActionPerformed

        situacao = "Não Conforme";
    }//GEN-LAST:event_rbtA1ActionPerformed

    /**
     * Método formInternalFrameClosing
     *
     * Acionado ao tentar fechar a janela interna (JInternalFrame). Exibe uma
     * caixa de diálogo de confirmação perguntando ao usuário se deseja sair. Se
     * o usuário confirmar (opção YES), executa o método iniciar_prev() e fecha
     * a janela com dispose().
     *
     * @param evt o evento gerado pelo fechamento da janela interna
     */
    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing

        int sair = JOptionPane.showConfirmDialog(null, "Tem Certeza Que Deseja Sair?", "Atenção!", JOptionPane.YES_NO_OPTION);
        if (sair == JOptionPane.YES_OPTION) {
            iniciar_prev();
            dispose();
        }
    }//GEN-LAST:event_formInternalFrameClosing

    /**
     * Método rbtA57ActionPerformed
     *
     * Acionado ao selecionar o botão de rádio "Conforme" do item A27. Define a
     * variável situacao26 como "Conforme", indicando que o item A27 está em
     * conformidade.
     *
     * @param evt o evento gerado pela seleção do botão de rádio do item A27
     */
    private void rbtA57ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtA57ActionPerformed

        situacao26 = "Conforme";

    }//GEN-LAST:event_rbtA57ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton btnAnoA1;
    private javax.swing.JToggleButton btnAnoA10;
    private javax.swing.JToggleButton btnAnoA11;
    private javax.swing.JToggleButton btnAnoA12;
    private javax.swing.JToggleButton btnAnoA13;
    private javax.swing.JToggleButton btnAnoA14;
    private javax.swing.JToggleButton btnAnoA15;
    private javax.swing.JToggleButton btnAnoA16;
    private javax.swing.JToggleButton btnAnoA17;
    private javax.swing.JToggleButton btnAnoA18;
    private javax.swing.JToggleButton btnAnoA19;
    private javax.swing.JToggleButton btnAnoA2;
    private javax.swing.JToggleButton btnAnoA20;
    private javax.swing.JToggleButton btnAnoA21;
    private javax.swing.JToggleButton btnAnoA22;
    private javax.swing.JToggleButton btnAnoA23;
    private javax.swing.JToggleButton btnAnoA24;
    private javax.swing.JToggleButton btnAnoA25;
    private javax.swing.JToggleButton btnAnoA26;
    private javax.swing.JToggleButton btnAnoA27;
    private javax.swing.JToggleButton btnAnoA3;
    private javax.swing.JToggleButton btnAnoA4;
    private javax.swing.JToggleButton btnAnoA5;
    private javax.swing.JToggleButton btnAnoA6;
    private javax.swing.JToggleButton btnAnoA7;
    private javax.swing.JToggleButton btnAnoA8;
    private javax.swing.JToggleButton btnAnoA9;
    private javax.swing.JButton btnArqFotoPre;
    private javax.swing.JButton btnCamPre;
    private javax.swing.JButton btnEncPre;
    private javax.swing.JButton btnFotoA1;
    private javax.swing.JButton btnFotoA10;
    private javax.swing.JButton btnFotoA11;
    private javax.swing.JButton btnFotoA12;
    private javax.swing.JButton btnFotoA13;
    private javax.swing.JButton btnFotoA14;
    private javax.swing.JButton btnFotoA15;
    private javax.swing.JButton btnFotoA16;
    private javax.swing.JButton btnFotoA17;
    private javax.swing.JButton btnFotoA18;
    private javax.swing.JButton btnFotoA19;
    private javax.swing.JButton btnFotoA2;
    private javax.swing.JButton btnFotoA20;
    private javax.swing.JButton btnFotoA21;
    private javax.swing.JButton btnFotoA22;
    private javax.swing.JButton btnFotoA23;
    private javax.swing.JButton btnFotoA24;
    private javax.swing.JButton btnFotoA25;
    private javax.swing.JButton btnFotoA26;
    private javax.swing.JButton btnFotoA27;
    private javax.swing.JButton btnFotoA3;
    private javax.swing.JButton btnFotoA4;
    private javax.swing.JButton btnFotoA5;
    private javax.swing.JButton btnFotoA6;
    private javax.swing.JButton btnFotoA7;
    private javax.swing.JButton btnFotoA8;
    private javax.swing.JButton btnFotoA9;
    public static javax.swing.JButton btnSalFotPre1;
    private javax.swing.ButtonGroup gbrA20;
    private javax.swing.ButtonGroup gbrA21;
    private javax.swing.ButtonGroup gbrA22;
    private javax.swing.ButtonGroup gbrA23;
    private javax.swing.ButtonGroup gbrA24;
    private javax.swing.ButtonGroup gbrA25;
    private javax.swing.ButtonGroup gbrA26;
    private javax.swing.ButtonGroup gbrA27;
    private javax.swing.ButtonGroup gbrA28;
    private javax.swing.ButtonGroup gbrA29;
    private javax.swing.ButtonGroup gbrA30;
    private javax.swing.ButtonGroup gbrA31;
    private javax.swing.ButtonGroup gbrA32;
    private javax.swing.ButtonGroup grbA1;
    private javax.swing.ButtonGroup grbA10;
    private javax.swing.ButtonGroup grbA11;
    private javax.swing.ButtonGroup grbA12;
    private javax.swing.ButtonGroup grbA13;
    private javax.swing.ButtonGroup grbA14;
    private javax.swing.ButtonGroup grbA15;
    private javax.swing.ButtonGroup grbA16;
    private javax.swing.ButtonGroup grbA17;
    private javax.swing.ButtonGroup grbA18;
    private javax.swing.ButtonGroup grbA19;
    private javax.swing.ButtonGroup grbA2;
    private javax.swing.ButtonGroup grbA3;
    private javax.swing.ButtonGroup grbA4;
    private javax.swing.ButtonGroup grbA5;
    private javax.swing.ButtonGroup grbA6;
    private javax.swing.ButtonGroup grbA7;
    private javax.swing.ButtonGroup grbA8;
    private javax.swing.ButtonGroup grbA9;
    private javax.swing.JLabel jLabel1;
    public static javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    public static javax.swing.JLabel lblFotEltMen1;
    private javax.swing.JRadioButton rbtA1;
    private javax.swing.JRadioButton rbtA10;
    private javax.swing.JRadioButton rbtA11;
    private javax.swing.JRadioButton rbtA12;
    private javax.swing.JRadioButton rbtA13;
    private javax.swing.JRadioButton rbtA14;
    private javax.swing.JRadioButton rbtA15;
    private javax.swing.JRadioButton rbtA16;
    private javax.swing.JRadioButton rbtA17;
    private javax.swing.JRadioButton rbtA18;
    private javax.swing.JRadioButton rbtA19;
    private javax.swing.JRadioButton rbtA2;
    private javax.swing.JRadioButton rbtA20;
    private javax.swing.JRadioButton rbtA21;
    private javax.swing.JRadioButton rbtA22;
    private javax.swing.JRadioButton rbtA23;
    private javax.swing.JRadioButton rbtA24;
    private javax.swing.JRadioButton rbtA25;
    private javax.swing.JRadioButton rbtA26;
    private javax.swing.JRadioButton rbtA27;
    private javax.swing.JRadioButton rbtA28;
    private javax.swing.JRadioButton rbtA29;
    private javax.swing.JRadioButton rbtA3;
    private javax.swing.JRadioButton rbtA30;
    private javax.swing.JRadioButton rbtA31;
    private javax.swing.JRadioButton rbtA32;
    private javax.swing.JRadioButton rbtA33;
    private javax.swing.JRadioButton rbtA34;
    private javax.swing.JRadioButton rbtA35;
    private javax.swing.JRadioButton rbtA36;
    private javax.swing.JRadioButton rbtA37;
    private javax.swing.JRadioButton rbtA38;
    private javax.swing.JRadioButton rbtA39;
    private javax.swing.JRadioButton rbtA4;
    private javax.swing.JRadioButton rbtA40;
    private javax.swing.JRadioButton rbtA41;
    private javax.swing.JRadioButton rbtA42;
    private javax.swing.JRadioButton rbtA43;
    private javax.swing.JRadioButton rbtA44;
    private javax.swing.JRadioButton rbtA45;
    private javax.swing.JRadioButton rbtA46;
    private javax.swing.JRadioButton rbtA47;
    private javax.swing.JRadioButton rbtA5;
    private javax.swing.JRadioButton rbtA51;
    private javax.swing.JRadioButton rbtA52;
    private javax.swing.JRadioButton rbtA53;
    private javax.swing.JRadioButton rbtA54;
    private javax.swing.JRadioButton rbtA55;
    private javax.swing.JRadioButton rbtA56;
    private javax.swing.JRadioButton rbtA57;
    private javax.swing.JRadioButton rbtA6;
    private javax.swing.JRadioButton rbtA7;
    private javax.swing.JRadioButton rbtA8;
    private javax.swing.JRadioButton rbtA9;
    private javax.swing.JScrollPane txpAAnot1;
    private javax.swing.JScrollPane txpAAnot10;
    private javax.swing.JScrollPane txpAAnot11;
    private javax.swing.JScrollPane txpAAnot12;
    private javax.swing.JScrollPane txpAAnot13;
    private javax.swing.JScrollPane txpAAnot14;
    private javax.swing.JScrollPane txpAAnot15;
    private javax.swing.JScrollPane txpAAnot16;
    private javax.swing.JScrollPane txpAAnot17;
    private javax.swing.JScrollPane txpAAnot18;
    private javax.swing.JScrollPane txpAAnot19;
    private javax.swing.JScrollPane txpAAnot2;
    private javax.swing.JScrollPane txpAAnot20;
    private javax.swing.JScrollPane txpAAnot21;
    private javax.swing.JScrollPane txpAAnot22;
    private javax.swing.JScrollPane txpAAnot23;
    private javax.swing.JScrollPane txpAAnot24;
    private javax.swing.JScrollPane txpAAnot25;
    private javax.swing.JScrollPane txpAAnot26;
    private javax.swing.JScrollPane txpAAnot27;
    private javax.swing.JScrollPane txpAAnot3;
    private javax.swing.JScrollPane txpAAnot4;
    private javax.swing.JScrollPane txpAAnot5;
    private javax.swing.JScrollPane txpAAnot6;
    private javax.swing.JScrollPane txpAAnot7;
    private javax.swing.JScrollPane txpAAnot8;
    private javax.swing.JScrollPane txpAAnot9;
    private javax.swing.JButton txtCanFot;
    private javax.swing.JTextField txtCodEquPre;
    private javax.swing.JTextField txtDatFin;
    private javax.swing.JTextField txtDatHor;
    public static javax.swing.JTextField txtDatIni1;
    private javax.swing.JTextArea txtEquPre;
    public static javax.swing.JTextField txtFotPre1;
    private javax.swing.JTextField txtIdPreEle1;
    private javax.swing.JTextField txtNomTec;
    private javax.swing.JTextArea txtSetPre;
    private javax.swing.JTextField txtStaPre;
    private javax.swing.JTextField txtTemPre;
    private javax.swing.JTextArea txtaA1;
    private javax.swing.JTextArea txtaA10;
    private javax.swing.JTextArea txtaA11;
    private javax.swing.JTextArea txtaA12;
    private javax.swing.JTextArea txtaA13;
    private javax.swing.JTextArea txtaA14;
    private javax.swing.JTextArea txtaA15;
    private javax.swing.JTextArea txtaA16;
    private javax.swing.JTextArea txtaA17;
    private javax.swing.JTextArea txtaA18;
    private javax.swing.JTextArea txtaA19;
    private javax.swing.JTextArea txtaA2;
    private javax.swing.JTextArea txtaA20;
    private javax.swing.JTextArea txtaA21;
    private javax.swing.JTextArea txtaA22;
    private javax.swing.JTextArea txtaA23;
    private javax.swing.JTextArea txtaA24;
    private javax.swing.JTextArea txtaA25;
    private javax.swing.JTextArea txtaA26;
    private javax.swing.JTextArea txtaA27;
    private javax.swing.JTextArea txtaA3;
    private javax.swing.JTextArea txtaA4;
    private javax.swing.JTextArea txtaA5;
    private javax.swing.JTextArea txtaA6;
    private javax.swing.JTextArea txtaA7;
    private javax.swing.JTextArea txtaA8;
    private javax.swing.JTextArea txtaA9;
    // End of variables declaration//GEN-END:variables
}
