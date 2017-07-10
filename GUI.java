import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.input.*;
import javafx.scene.control.*;
import javafx.animation.*;
import javafx.scene.layout.*;
import javafx.scene.canvas.*;
import javafx.scene.paint.*;
import javafx.scene.image.*;
import javafx.scene.transform.*;
import javafx.geometry.*;
import javafx.event.*;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

public class GUI
{
   public static void main(String args[]) 
   {
		Graph graph = new SingleGraph("Tutorial 1");
      graph.addNode("A" );
      graph.addNode("B" );
      graph.addNode("C" );
      graph.addEdge("AB", "A", "B");
      graph.addEdge("BC", "B", "C");
      graph.addEdge("CA", "C", "A");
      graph.display();
	}
}
