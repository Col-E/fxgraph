package com.fxgraph.layout;

import org.abego.treelayout.Configuration.Location;
import org.abego.treelayout.NodeExtentProvider;
import org.abego.treelayout.TreeLayout;
import org.abego.treelayout.util.DefaultConfiguration;
import org.abego.treelayout.util.DefaultTreeForTreeLayout;

import com.fxgraph.graph.Graph;
import com.fxgraph.graph.ICell;

public class AbegoTreeLayout implements Layout {

	@Override
	public void execute(Graph graph) {
		final DefaultTreeForTreeLayout<ICell> layout = new DefaultTreeForTreeLayout<>(graph.getModel().getRoot());
		addRecursively(layout, graph.getModel().getRoot());
		final DefaultConfiguration<ICell> config = new DefaultConfiguration<ICell>(100, 45, Location.Bottom);
		final NodeExtentProvider<ICell> nodeExtentProvider = new NodeExtentProvider<ICell>() {

			@Override
			public double getWidth(ICell tn) {
				if (tn == graph.getModel().getRoot()) {
					return 0;
				}
				return graph.getGraphic(tn).getWidth();
			}

			@Override
			public double getHeight(ICell tn) {
				if (tn == graph.getModel().getRoot()) {
					return 0;
				}
				return graph.getGraphic(tn).getHeight();
			}
		};
		final TreeLayout<ICell> treeLayout = new TreeLayout<>(layout, nodeExtentProvider, config);
		treeLayout.getNodeBounds().entrySet().stream().filter(entry -> entry.getKey() != graph.getModel().getRoot())
				.forEach(entry -> {
					graph.getGraphic(entry.getKey()).setLayoutX(entry.getValue().getX());
					graph.getGraphic(entry.getKey()).setLayoutY(entry.getValue().getY());
				});
	}

	public void addRecursively(DefaultTreeForTreeLayout<ICell> layout, ICell node) {
		node.getCellChildren().forEach(cell -> {
			System.out.println("Adding: " + cell);
			layout.addChild(node, cell);
			addRecursively(layout, cell);
		});
	}

}
