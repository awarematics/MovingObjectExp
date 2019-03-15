package com.awarematics.postmedia.test;
import javax.swing.*;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Polygon;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.*;
import java.util.*;

public class MainFrame extends JFrame { 

	private JPanel p = null;
	
	public MainFrame() {
		
	}
	public MainFrame(ArrayList<Polygon> polys ) {
	
		initWindow();
		showWindow();
		Graphics g = p.getGraphics();
		g.setColor(Color.GREEN);
		p.paint(g);
		drawTrajectory(polys);
	}

	
	private void drawTrajectory(ArrayList<Polygon> polys ) {
		Graphics g = p.getGraphics();
		g.setColor(Color.GREEN);
		System.out.println(polys.size());
		for (int i = 0; i < polys.size();i++) {
			Coordinate coords[] = polys.get(i).getCoordinates() ;
			for (int m = 0; m < coords.length-2 ;m++) {
				int startX = (int)coords[m].x*100;
				int startY = (int)coords[m].y*100;
				int endX = (int)coords[m+1].x*100;
				int endY = (int)coords[m+1].y*100;
				g.drawLine(startX, startY, endX, endY);
			}
		}
		
	}
	
	private void initWindow() {
		if (null == p) {
		   p = new JPanel();
		}    
	}
	
	public void showWindow() {
		this.setBounds(200, 200, 1200, 900);
		this.setContentPane(p);
		this.setTitle("TraClusAlgorithm ---- By Bo Liu");
		this.setVisible(true);
		this.setResizable(true);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
		   }
		});
	}
}
	