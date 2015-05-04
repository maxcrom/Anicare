package sep.software.anicare.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.plus.model.people.Person;

import sep.software.anicare.util.RandomUtil;

/**
 * Created by hongkunyoo on 15. 5. 2..
 */
public class AniCarePet extends AniCareModel {
    public enum Category {
        DOG(0), CAT(1), BIRD(2), ETC(3);

        private final int value;
        Category(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
    }
    public enum Size {
        BIG(0), MIDDLE(1), SMALL(2);

        private final int value;
        Size(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
    }
    public enum Personality {
        BRIGHT(0), SHY(1), NORMAL(2);

        private final int value;
        Personality(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
    }

    private String id;
    private String name;
    private int rawCategory;
    private int rawSize;
    private int rawPersonality;
    private boolean isMale;
    private boolean isNeutralized;
    private boolean isPetFood;

    public String getId(){ return id; }
    public void setId(String id) { this.id = id; }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return Category.values()[this.rawCategory];
    }

    public void setCategory(Category category) {
        this.rawCategory = category.getValue();
    }

    public Size getSize() {
        return Size.values()[this.rawSize];
    }

    public void setSize(Size size) {
        this.rawSize = size.getValue();
    }

    public Personality getPersonality() {
        return Personality.values()[this.rawPersonality];
    }

    public void setPersonality(Personality personality) {
        this.rawPersonality = personality.getValue();
    }

    public boolean isMale() {
        return isMale;
    }

    public void setMale(boolean isMale) {
        this.isMale = isMale;
    }

    public boolean isNeutralized() {
        return isNeutralized;
    }

    public void setNeutralized(boolean isNeutralized) {
        this.isNeutralized = isNeutralized;
    }

    public boolean isPetFood() {
        return isPetFood;
    }

    public void setPetFood(boolean isPetFood) {
        this.isPetFood = isPetFood;
    }

    public int getRawCategory() {
        return rawCategory;
    }

    public void setRawCategory(int rawCategory) {
        this.rawCategory = rawCategory;
    }

    public int getRawSize() {
        return rawSize;
    }

    public void setRawSize(int rawSize) {
        this.rawSize = rawSize;
    }

    public int getRawPersonality() {
        return rawPersonality;
    }

    public void setRawPersonality(int rawPersonality) {
        this.rawPersonality = rawPersonality;
    }

    public static AniCarePet rand() {
        return rand(false);
    }

    public static AniCarePet rand(boolean hasId) {
        AniCarePet pet = new AniCarePet();
        if (hasId)
            pet.setId(RandomUtil.getId());

        pet.setName(RandomUtil.getName());
        pet.setRawCategory(RandomUtil.getInt(Category.values().length));
        pet.setRawSize(RandomUtil.getInt(Size.values().length));
        pet.setRawPersonality(RandomUtil.getInt(Personality.values().length));
        pet.setMale(RandomUtil.getBoolean());
        pet.setNeutralized(RandomUtil.getBoolean());
        pet.setPetFood(RandomUtil.getBoolean());

        return pet;
    }

    /*
	 * Parcelable
	 */
    public static final Parcelable.Creator<AniCarePet> CREATOR = new Creator<AniCarePet>(){
        public AniCarePet createFromParcel(Parcel in){
            return toClass(in, AniCarePet.class);
        }
        public AniCarePet[] newArray(int size){
            return new AniCarePet[size];
        }
    };


}
