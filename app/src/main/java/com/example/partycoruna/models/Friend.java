package com.example.partycoruna.models;

import com.google.gson.annotations.SerializedName;

/* Friend
 - Modelo para representar un amigo del usuario.
 - Alineado con UserProfileResponse/Backend.
 */
public class Friend {
    
    @SerializedName("id")
    private int id; 

    @SerializedName("fullName") 
    private String fullName;

    @SerializedName("username")
    private String username;

    @SerializedName("name")
    private String name;

    @SerializedName("avatarUrl")
    private String avatarUrl;

    public Friend(int id, String name, String avatarUrl) {
        this.id = id;
        this.fullName = name;
        this.avatarUrl = avatarUrl;
    }

    @SerializedName("userId")
    private String serverUserId;

    // ...

    public int getId() {
        if (id != 0) return id;
        try {
            if (serverUserId != null && serverUserId.startsWith("u_")) {
                return Integer.parseInt(serverUserId.replace("u_", ""));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        // Lógica de fallback para recuperar el nombre disponible
        if (fullName != null && !fullName.isEmpty()) return fullName;
        if (username != null && !username.isEmpty()) return username;
        if (name != null && !name.isEmpty()) return name;
        return "Usuario (" + id + ")";
    }

    public void setName(String name) {
        this.fullName = name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
