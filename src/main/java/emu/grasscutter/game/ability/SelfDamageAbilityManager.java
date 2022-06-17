package emu.grasscutter.game.ability;

import emu.grasscutter.game.entity.EntityAvatar;
import emu.grasscutter.game.player.Player;
import emu.grasscutter.game.props.FightProperty;
import emu.grasscutter.net.proto.AbilityInvokeEntryOuterClass.AbilityInvokeEntry;
import emu.grasscutter.net.proto.AbilityMetaModifierChangeOuterClass.AbilityMetaModifierChange;
import lombok.Data;

import java.util.ArrayList;


public class SelfDamageAbilityManager {

    private final Player player;
    private final ArrayList<avatar> avatars;

    @Data
    private class avatar {
        private String name;
        private float damagePercent;
        private int id;

        public avatar(String name, float damagePercent, int id) {
            this.name = name;
            this.damagePercent = damagePercent;
            this.id = id;
        }
    }

    public SelfDamageAbilityManager(Player player) {
        this.player = player;

        avatars = new ArrayList<>();

        avatars.add(new avatar("Hutao", .30f, 9));
        avatars.add(new avatar("Shinobu", .30f, 5));
        //TODO: add Xiao?

    }


    public void damageHandler(AbilityInvokeEntry invoke) throws Exception {

        AbilityMetaModifierChange data = AbilityMetaModifierChange.parseFrom(invoke.getAbilityData());

        if (data == null) return;

        String modifierString = data.getParentAbilityName().getStr();

        if (modifierString.isBlank()) return;

//        System.out.println(modifierString);

//        System.out.println(currentAvatar.getMetaOverrideMap().toString());

        for (avatar avatar : avatars) {

            if ((modifierString.contains(avatar.getName())) && (data.getModifierLocalId() == avatar.getId())) {

                EntityAvatar currentAvatar = (EntityAvatar) player.getScene().getEntityById(data.getApplyEntityId());

                float currentHealth = currentAvatar.getFightProperty(FightProperty.FIGHT_PROP_CUR_HP);
                float damageAmount = avatar.getDamagePercent() * currentHealth;

                if (!(currentHealth - damageAmount <= 1)) {
                    //reduce health
                    currentAvatar.damage(damageAmount);
                    System.out.printf("damaging %s%n", avatar.getName());
                }
            }
        }
    }
}
