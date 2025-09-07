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

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JLabel;
import javax.swing.border.AbstractBorder;

/**
 * Classe LabelPersonalizada
 *
 * Estende JLabel para permitir personalizações visuais ou funcionais
 * específicas. Pode ser usada para criar rótulos com estilos gráficos
 * diferenciados, como gradientes, bordas customizadas ou comportamento
 * adicional.
 *
 * Essa classe serve como base para componentes visuais que exibem texto ou
 * ícones de forma não interativa, mantendo o papel tradicional de um JLabel.
 */
public class LabelPersonalizada extends JLabel {

    /**
     * Sobrescreve o método paintComponent para desenhar um fundo personalizado
     * quando o componente é transparente e possui uma borda arredondada.
     *
     * Isso permite que o fundo acompanhe o formato da borda, criando um efeito
     * visual mais refinado em componentes com cantos arredondados.
     *
     * @param g o contexto gráfico usado para desenhar o componente
     */
    @Override
    protected void paintComponent(Graphics g) {
        if (!isOpaque() && getBorder() instanceof RoundedCornerBorder) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setPaint(getBackground());
            g2.fill(((RoundedCornerBorder) getBorder()).getBorderShape(
                    0, 0, getWidth() - 1, getHeight() - 1));
            g2.dispose();
        }
        super.paintComponent(g);
    }

    /**
     * Atualiza a interface do usuário do componente.
     *
     * Este método garante que o LabelPersonalizada mantenha sua aparência
     * personalizada após mudanças no Look and Feel, definindo o componente como
     * transparente e aplicando uma borda com cantos arredondados.
     */
    @Override
    public void updateUI() {
        super.updateUI();
        setOpaque(false);
        setBorder(new RoundedCornerBorder());
    }
};

/**
 * Borda com cantos arredondados e suporte a transparência.
 *
 * Esta classe desenha uma borda arredondada ao redor do componente, removendo
 * visualmente os cantos com preenchimento transparente. Ideal para componentes
 * personalizados que exigem estética refinada.
 */
class RoundedCornerBorder extends AbstractBorder {

    private static final Color ALPHA_ZERO = new Color(0x0, true);

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Shape border = getBorderShape(x, y, width - 1, height - 1);
        g2.setPaint(ALPHA_ZERO);
        Area corner = new Area(new Rectangle2D.Double(x, y, width, height));
        corner.subtract(new Area(border));
        g2.fill(corner);
        g2.draw(border);
        g2.dispose();
    }

    public Shape getBorderShape(int x, int y, int w, int h) {
        int r = h / 18;
        return new RoundRectangle2D.Double(x, y, w, h, r, r);
    }

    /**
     * Retorna os espaçamentos internos da borda.
     *
     * Define margens para garantir que o conteúdo do componente não encoste na
     * borda arredondada, preservando a estética.
     *
     * @param c o componente ao qual a borda está aplicada
     * @return os espaçamentos internos da borda
     */
    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(4, 8, 4, 8);
    }

    /**
     * Atualiza os espaçamentos internos da borda reutilizando o objeto Insets
     * fornecido.
     *
     * Essa versão é mais eficiente, pois evita a criação de novos objetos.
     * Define margens assimétricas para acomodar a borda arredondada com melhor
     * estética.
     *
     * @param c o componente ao qual a borda está aplicada
     * @param insets o objeto Insets a ser atualizado
     * @return o objeto Insets atualizado com os valores desejados
     */
    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.set(2, 8, 4, 8);
        return insets;
    }

}
