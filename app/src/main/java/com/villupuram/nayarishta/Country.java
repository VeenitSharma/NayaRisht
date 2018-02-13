package com.villupuram.nayarishta;

/**
 * Created by vinit on 7/22/2017.
 */

public class Country {


        String name = null;
        boolean selected = false;

        public Country( String name) {
            super();

            this.name = name;
        }


        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }

        public boolean isSelected() {
            return selected;
        }
        public void setSelected(boolean selected) {
            this.selected = selected;
        }
}
