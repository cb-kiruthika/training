
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

class MobileStation {

    class Mobile {

        protected String name = "";
        protected Integer remainingCharge = 0;

        public void name() {
            System.out.println(this.name);
        }

        public void remainingCharge() {
            System.out.println(this.remainingCharge);
        }

        Mobile(String name) {
            this.name = name;
        }
    }

    MobileStation() {
        List<Mobile> mobiles = new LinkedList<>();
        mobiles.add(new Mobile("samsung") {
            @Override
            public void remainingCharge() {
                this.remainingCharge = (int) (100 * (Math.random()));
                System.out.println(this.remainingCharge);
            }
        });
        mobiles.add(new Mobile("redmi") {
            @Override
            public void remainingCharge() {
                this.remainingCharge = (int) (100 * (Math.random()));
                System.out.println(this.remainingCharge);
            }
        });
        mobiles.add(new Mobile("oppo") {
            @Override
            public void remainingCharge() {
                this.remainingCharge = (int) (100 * (Math.random()));
                System.out.println(this.remainingCharge);
            }
        });
        mobiles.add(new Mobile("sony") {
            @Override
            public void remainingCharge() {
                this.remainingCharge = (int) (100 * (Math.random()));
                System.out.println(this.remainingCharge);
            }
        });
        for (Mobile m : mobiles) {
            m.remainingCharge();
        }

    }

    public static void main(String[] args) {
        MobileStation ms = new MobileStation();
    }

}
