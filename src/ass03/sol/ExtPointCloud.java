package ass03.sol;

import java.util.function.Consumer;

import ass03.*;

public interface ExtPointCloud extends PointCloud {

	void apply(Consumer<P2d> t);
}
