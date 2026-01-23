/*
 * This file is part of ViaBackwards - https://github.com/ViaVersion/ViaBackwards
 * Copyright (C) 2016-2026 ViaVersion and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.viaversion.viabackwards.protocol.v1_21_9to1_21_7.rewriter;

import com.viaversion.nbt.tag.CompoundTag;
import com.viaversion.nbt.tag.StringTag;
import com.viaversion.nbt.tag.Tag;
import com.viaversion.viabackwards.api.BackwardsProtocol;
import com.viaversion.viabackwards.api.rewriters.text.NBTComponentRewriter;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.data.StructuredDataKey;
import com.viaversion.viaversion.protocols.v1_21_7to1_21_9.packet.ClientboundPacket1_21_9;
import java.util.HashMap;
import java.util.Map;

public final class ComponentRewriter1_21_9 extends NBTComponentRewriter<ClientboundPacket1_21_9> {
    private static final Map<String, String> SPRITE_MAP = new HashMap<>();

    static {
        SPRITE_MAP.put("minecraft:item/arrow", "arrow");
        SPRITE_MAP.put("minecraft:item/spectral_arrow", "arrow");
        SPRITE_MAP.put("minecraft:item/experience_bottle", "XP");
        SPRITE_MAP.put("minecraft:item/music_disc_5", "disc");
        SPRITE_MAP.put("minecraft:item/music_disc_11", "disc");
        SPRITE_MAP.put("minecraft:item/music_disc_13", "disc");
        SPRITE_MAP.put("minecraft:item/music_disc_blocks", "disc");
        SPRITE_MAP.put("minecraft:item/music_disc_cat", "disc");
        SPRITE_MAP.put("minecraft:item/music_disc_chirp", "disc");
        SPRITE_MAP.put("minecraft:item/music_disc_creator", "disc");
        SPRITE_MAP.put("minecraft:item/music_disc_creator_music_box", "disc");
        SPRITE_MAP.put("minecraft:item/music_disc_far", "disc");
        SPRITE_MAP.put("minecraft:item/music_disc_lava_chicken", "disc");
        SPRITE_MAP.put("minecraft:item/music_disc_mall", "disc");
        SPRITE_MAP.put("minecraft:item/music_disc_mellohi", "disc");
        SPRITE_MAP.put("minecraft:item/music_disc_otherside", "disc");
        SPRITE_MAP.put("minecraft:item/music_disc_pigstep", "disc");
        SPRITE_MAP.put("minecraft:item/music_disc_precipice", "disc");
        SPRITE_MAP.put("minecraft:item/music_disc_relic", "disc");
        SPRITE_MAP.put("minecraft:item/music_disc_stall", "disc");
        SPRITE_MAP.put("minecraft:item/music_disc_strad", "disc");
        SPRITE_MAP.put("minecraft:item/music_disc_tears", "disc");
        SPRITE_MAP.put("minecraft:item/music_disc_wait", "disc");
        SPRITE_MAP.put("minecraft:item/music_disc_ward", "disc");
    }

    public ComponentRewriter1_21_9(final BackwardsProtocol<ClientboundPacket1_21_9, ?, ?, ?> protocol) {
        super(protocol);
    }

    @Override
    protected void processCompoundTag(final UserConnection connection, final CompoundTag tag) {
        super.processCompoundTag(connection, tag);

        // Throw out the new object type and its properties
        final String type = tag.getString("type");

        // Try to use the 26.1+ fallback value if present, otherwise just show an empty string
        Tag fallback = tag.get("fallback");
        if (fallback == null) {
            fallback = new StringTag("");
        }

        if ("object".equals(type)) {
            tag.put("text", fallback);
            tag.remove("type");
        }
        final var sprite = tag.remove("sprite");
        if (sprite != null) {
            final var mapped = SPRITE_MAP.getOrDefault(sprite.asRawString(), fallback);
            tag.putString("text", mapped);
        }
        if (tag.remove("player") != null) {
            tag.put("text", fallback);
        }
        tag.remove("atlas");
    }

    @Override
    protected void handleShowItem(final UserConnection connection, final CompoundTag itemTag, final CompoundTag componentsTag) {
        super.handleShowItem(connection, itemTag, componentsTag);
        if (componentsTag == null) {
            return;
        }

        removeDataComponents(componentsTag, StructuredDataKey.ENTITY_DATA1_21_9, StructuredDataKey.BLOCK_ENTITY_DATA1_21_9);
    }
}
