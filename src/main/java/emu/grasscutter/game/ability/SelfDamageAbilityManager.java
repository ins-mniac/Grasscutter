package emu.grasscutter.game.ability;

import emu.grasscutter.game.entity.EntityAvatar;
import emu.grasscutter.game.player.Player;
import emu.grasscutter.game.props.FightProperty;
import emu.grasscutter.net.proto.AbilityInvokeEntryOuterClass.AbilityInvokeEntry;
import emu.grasscutter.net.proto.AbilityMetaModifierChangeOuterClass.AbilityMetaModifierChange;

import java.util.HashMap;
import java.util.List;


public class SelfDamageAbilityManager {

    private final Player player;
    private final HashMap<String, Float> damageAvatarList;
//    private final ArrayList<String> cooldownList;

//    private class avatar{
//        String name;
//        float damagePercent;
//
//    }

    public SelfDamageAbilityManager(Player player) {
        this.player = player;
//        cooldownList = new ArrayList<>();
        damageAvatarList = new HashMap<>();
        //Avatar name, damage amount
        damageAvatarList.put("Hutao", .30f /* 30% of Hu Tao's health */);

    }


    public void damageHandler(AbilityInvokeEntry invoke) throws Exception {
        AbilityMetaModifierChange data = AbilityMetaModifierChange.parseFrom(invoke.getAbilityData());

        if (data == null) return;
        if (data.getParentAbilityName().getStr().isBlank())
            return; // Also functions as an == null check, though getParentAbilityName is NotNull? /shrug

        System.out.println(data);
        String modifierString = data.getParentAbilityName().getStr();
        System.out.println(modifierString);
        for (String avatarName : damageAvatarList.keySet()) {
            if (modifierString.contains(avatarName)) {

                List<EntityAvatar> activeTeam = player.getTeamManager().getActiveTeam();
                int currentIndex = player.getTeamManager().getCurrentCharacterIndex();
                EntityAvatar currentAvatar = activeTeam.get(currentIndex);

                float currentHealth = currentAvatar.getFightProperty(FightProperty.FIGHT_PROP_CUR_HP);
                float damageAmount = damageAvatarList.get(avatarName) * currentHealth;

                if (!(currentHealth - damageAmount <= 1)) {
                    currentAvatar.damage(damageAvatarList.get(avatarName) * currentHealth);
//                    cooldownList.add(modifierString);
                }
            }
        }

    }
}
