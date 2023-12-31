
public class SubnetTest {


//    public static void main(String... args) {
//
//        var overallRange = new IPAddressString("10.1.0.0/16").getAddress();
//        var overallRangeWithLargerPrefix = overallRange.setPrefixLength(overallRange.getPrefixLength()  + 2);
//
//
//
//
//        var subnetOneAndRemainder = getSubnetAndRemainder(overallRangeWithLargerPrefix);
//
//        System.out.println("Overall ranger: " + overallRange);
//        System.out.println("Subnet 1: " + subnetOneAndRemainder.t1);
//        System.out.println("Remainder is " + subnetOneAndRemainder.t2);
//
//
//        var subnetTwoAndRemainder = getSubnetAndRemainder(subnetOneAndRemainder.t2, 2);
//        System.out.println("Subnet 2: " + subnetTwoAndRemainder.t1);
//        System.out.println("Remainder is " + subnetTwoAndRemainder.t2);
//
//
//        //        var subnetThreeAndRemainder = getSubnetAndRemainder(subnetTwoAndRemainder.t2, 2);
////        System.out.println("Subnet 3: " + subnetThreeAndRemainder.t1);
//    }
//
//    private static Tuples.Tuple2<IPAddress, IPAddress> getSubnetAndRemainder(IPAddress remainingRange) {
//
////        var remainder = remainingRange.subtract(subnet);
//
//        if(remainder.length != 1) {
//
//            throw new RuntimeException("Remainder is segmented");
//        }
//
//
//        return Tuples.of(subnet, remainder[0]);
//    }

}