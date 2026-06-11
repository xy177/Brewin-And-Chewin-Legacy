package xy177.brewinandchewinlegacy.common.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class BNCCoasterTestCommand extends CommandBase {
    private static final BoxSpec[] BOXES = {
        new BoxSpec("Coaster Test - BNC Drinks",
            item("brewinandchewinlegacy:tankard"),
            item("brewinandchewinlegacy:beer"),
            item("brewinandchewinlegacy:vodka"),
            item("brewinandchewinlegacy:mead"),
            item("brewinandchewinlegacy:rice_wine"),
            item("brewinandchewinlegacy:egg_grog"),
            item("brewinandchewinlegacy:strongroot_ale"),
            item("brewinandchewinlegacy:saccharine_rum"),
            item("brewinandchewinlegacy:bloody_mary"),
            item("brewinandchewinlegacy:steel_toe_stout"),
            item("brewinandchewinlegacy:glittering_grenadine"),
            item("brewinandchewinlegacy:pale_jane"),
            item("brewinandchewinlegacy:salty_folly"),
            item("brewinandchewinlegacy:dread_nog"),
            item("brewinandchewinlegacy:red_rum"),
            item("brewinandchewinlegacy:withering_dross"),
            item("brewinandchewinlegacy:kombucha")),
        new BoxSpec("Coaster Test - BNC Foods",
            item("brewinandchewinlegacy:flaxen_cheese_wedge"),
            item("brewinandchewinlegacy:scarlet_cheese_wedge"),
            item("brewinandchewinlegacy:creamy_onion_soup"),
            item("brewinandchewinlegacy:fiery_fondue"),
            item("brewinandchewinlegacy:sweet_berries"),
            item("brewinandchewinlegacy:glow_berries"),
            item("brewinandchewinlegacy:sweet_berry_jam"),
            item("brewinandchewinlegacy:glow_berry_marmalade"),
            item("brewinandchewinlegacy:apple_jelly")),
        new BoxSpec("Coaster Test - FD 1",
            item("farmersdelight:apple_cider"),
            item("farmersdelight:baked_cod_stew"),
            item("farmersdelight:beef_stew"),
            item("farmersdelight:bone_broth"),
            item("farmersdelight:cabbage"),
            item("farmersdelight:chicken_soup"),
            item("farmersdelight:cod_roll"),
            item("farmersdelight:cooked_rice"),
            item("farmersdelight:dog_food"),
            item("farmersdelight:fish_stew"),
            item("farmersdelight:fried_egg"),
            item("farmersdelight:fried_rice"),
            item("farmersdelight:fruit_salad"),
            item("farmersdelight:glow_berry_custard"),
            item("farmersdelight:honey_cookie"),
            item("farmersdelight:hot_cocoa"),
            item("farmersdelight:kelp_roll")),
        new BoxSpec("Coaster Test - FD 2",
            item("farmersdelight:kelp_roll_slice"),
            item("farmersdelight:melon_juice"),
            item("farmersdelight:melon_popsicle"),
            item("farmersdelight:milk_bottle"),
            item("farmersdelight:mixed_salad"),
            item("farmersdelight:nether_salad"),
            item("farmersdelight:noodle_soup"),
            item("farmersdelight:onion"),
            item("farmersdelight:pumpkin_slice"),
            item("farmersdelight:pumpkin_soup"),
            item("farmersdelight:rotten_tomato"),
            item("farmersdelight:salmon_roll"),
            item("farmersdelight:stuffed_pumpkin"),
            item("farmersdelight:sweet_berry_cookie"),
            item("farmersdelight:tomato"),
            item("farmersdelight:tomato_sauce"),
            item("farmersdelight:vegetable_soup")),
        new BoxSpec("Coaster Test - Vanilla Foods",
            item("minecraft:apple"),
            item("minecraft:beef"),
            item("minecraft:beetroot"),
            item("minecraft:beetroot_soup"),
            item("minecraft:bowl"),
            item("minecraft:bread"),
            item("minecraft:carrot"),
            item("minecraft:chicken"),
            item("minecraft:chorus_fruit"),
            item("minecraft:cooked_beef"),
            item("minecraft:cooked_chicken"),
            item("minecraft:cooked_mutton"),
            item("minecraft:cooked_porkchop"),
            item("minecraft:cooked_rabbit"),
            item("minecraft:cookie"),
            item("minecraft:egg"),
            item("minecraft:golden_apple", 0),
            item("minecraft:golden_apple", 1),
            item("minecraft:mushroom_stew"),
            item("minecraft:mutton"),
            item("minecraft:poisonous_potato"),
            item("minecraft:porkchop"),
            item("minecraft:potato"),
            item("minecraft:pumpkin_pie"),
            item("minecraft:rabbit"),
            item("minecraft:rabbit_stew")),
        new BoxSpec("Coaster Test - Fish Potion FMC",
            item("minecraft:glass_bottle"),
            potion("minecraft:potion", PotionTypes.WATER),
            potion("minecraft:splash_potion", PotionTypes.HEALING),
            potion("minecraft:lingering_potion", PotionTypes.POISON),
            item("minecraft:fish", 0),
            item("minecraft:fish", 1),
            item("minecraft:fish", 2),
            item("minecraft:fish", 3),
            item("minecraft:cooked_fish", 0),
            item("minecraft:cooked_fish", 1),
            item("futuremc:sweet_berries"),
            item("futuremc:honey_bottle"),
            item("futuremc:suspicious_stew"))
    };

    @Override
    public String getName() {
        return "bnc_coaster_test";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/bnc_coaster_test";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        EntityPlayerMP player = getCommandSenderAsPlayer(sender);
        int boxes = 0;
        int skipped = 0;

        for (BoxSpec box : BOXES) {
            BoxResult result = createBox(box);
            skipped += result.skipped;
            if (!result.stack.isEmpty()) {
                give(player, result.stack);
                boxes++;
            }
        }

        player.sendMessage(new TextComponentString("Gave " + boxes + " coaster test shulker boxes. Skipped " + skipped + " missing optional items."));
    }

    private static BoxResult createBox(BoxSpec spec) {
        Item shulker = ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft:purple_shulker_box"));
        if (shulker == null) {
            return new BoxResult(ItemStack.EMPTY, spec.entries.length);
        }

        NBTTagList items = new NBTTagList();
        int slot = 0;
        int skipped = 0;
        for (Entry entry : spec.entries) {
            ItemStack stack = entry.createStack();
            if (stack.isEmpty()) {
                skipped++;
                continue;
            }

            NBTTagCompound itemTag = new NBTTagCompound();
            stack.writeToNBT(itemTag);
            itemTag.setByte("Slot", (byte) slot++);
            items.appendTag(itemTag);
        }

        if (items.tagCount() <= 0) {
            return new BoxResult(ItemStack.EMPTY, skipped);
        }

        ItemStack box = new ItemStack(shulker);
        box.setStackDisplayName(spec.name);
        NBTTagCompound root = box.getTagCompound();
        if (root == null) {
            root = new NBTTagCompound();
        }
        NBTTagCompound blockEntity = new NBTTagCompound();
        blockEntity.setTag("Items", items);
        root.setTag("BlockEntityTag", blockEntity);
        box.setTagCompound(root);
        return new BoxResult(box, skipped);
    }

    private static void give(EntityPlayerMP player, ItemStack stack) {
        ItemStack copy = stack.copy();
        if (!player.inventory.addItemStackToInventory(copy)) {
            player.dropItem(copy, false);
        }
    }

    private static Entry item(String id) {
        return item(id, 0);
    }

    private static Entry item(String id, int meta) {
        return new Entry(id, meta, null);
    }

    private static Entry potion(String id, PotionType potion) {
        return new Entry(id, 0, potion);
    }

    private static final class BoxSpec {
        private final String name;
        private final Entry[] entries;

        private BoxSpec(String name, Entry... entries) {
            this.name = name;
            this.entries = entries;
        }
    }

    private static final class Entry {
        private final String id;
        private final int meta;
        private final PotionType potion;

        private Entry(String id, int meta, PotionType potion) {
            this.id = id;
            this.meta = meta;
            this.potion = potion;
        }

        private ItemStack createStack() {
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(id));
            if (item == null) {
                return ItemStack.EMPTY;
            }
            ItemStack stack = new ItemStack(item, 1, meta);
            if (potion != null) {
                PotionUtils.addPotionToItemStack(stack, potion);
            }
            return stack;
        }
    }

    private static final class BoxResult {
        private final ItemStack stack;
        private final int skipped;

        private BoxResult(ItemStack stack, int skipped) {
            this.stack = stack;
            this.skipped = skipped;
        }
    }
}
