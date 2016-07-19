package ass03;

import java.util.Arrays;
import java.util.List;

public class PointCloudTest {

	public static void main(String[] args) {
		Region region1 = new Region(new P2d(0, 10), new P2d(10, 0));
		V2d vector1 = new V2d(new P2d(2, 2), new P2d(0, 0));
		List<P2d> points1 = Arrays.asList(new P2d(1, 1), new P2d(1, 3),
				new P2d(2, 1), new P2d(4, 3), new P2d(5, 1), new P2d(5, 5));
		MyPointCloud mpc = new MyPointCloud(points1);
		System.out.println(mpc.toString());
		mpc.move(vector1);
		System.out.println(mpc.getPointsInRegion(region1).toString());
		System.out.println(mpc.getNearestPoint(new P2d(5, 5)).toString());
		System.out.println(mpc.toString());
	}

}
