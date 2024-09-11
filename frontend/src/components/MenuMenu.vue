<template>
    <v-card style="width:450px; height:100%;" outlined>
        <template slot="progress">
            <v-progress-linear
                color="primary-darker-1"
                height="10"
                indeterminate
            ></v-progress-linear>
        </template>

        <v-card-title v-if="value._links">
            Menu # {{decode(value._links.self.href.split("/")[value._links.self.href.split("/").length - 1])}}
        </v-card-title >
        <v-card-title v-else>
            Menu
        </v-card-title >        

        <v-card-text>
            <String label="Name" v-model="value.name" :editMode="editMode" :inputUI="''"/>

            <Number label="Price" v-model="value.price" :editMode="editMode" :inputUI="''"/>

            <!-- InventoryId 리스트 입력 -->
            <div>
                <label>InventoryId</label>
                <div v-for="(id, index) in value.inventoryId" :key="index">
                    <v-text-field
                        v-model="value.inventoryId[index]"
                        label="Inventory Id"
                        @click:append="removeInventoryId(index)"
                        v-if="editMode"
                    ></v-text-field>
                </div>
                <v-btn color="primary" text @click="addInventoryId" v-if="editMode">Add Inventory Id</v-btn>
            </div>

            <!-- IngredientUnit 리스트 입력 -->
            <div>
                <label>IngredientUnit</label>
                <div v-for="(unit, index) in value.ingredientUnit" :key="index">
                    <v-text-field
                        v-model="value.ingredientUnit[index]"
                        label="Ingredient Unit"
                        @click:append="removeIngredientUnit(index)"
                        v-if="editMode"
                    ></v-text-field>
                </div>
                <v-btn color="primary" text @click="addIngredientUnit" v-if="editMode">Add Ingredient Unit</v-btn>
            </div>

        </v-card-text>

        <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn
                color="primary"
                text
                @click="edit"
                v-if="!editMode"
            >
                수정
            </v-btn>
            <div v-else>
                <v-btn
                    color="primary"
                    text
                    @click="save"
                >
                    CreateMenu
                </v-btn>
                <v-btn
                    color="primary"
                    text
                    @click="save"
                >
                    UpdateMenu
                </v-btn>
                <v-btn
                    color="primary"
                    text
                    @click="remove"
                >
                    DeleteMenu
                </v-btn>
                <v-btn
                    color="primary"
                    text
                    @click="editMode = false"
                >
                    취소
                </v-btn>
            </div>
        </v-card-actions>

        <v-snackbar
            v-model="snackbar.status"
            :top="true"
            :timeout="snackbar.timeout"
            color="error"
        >
            {{ snackbar.text }}
            <v-btn dark text @click="snackbar.status = false">
                Close
            </v-btn>
        </v-snackbar>
    </v-card>
</template>

<script>
    const axios = require('axios').default;

    export default {
        name: 'MenuMenu',
        components: {},
        props: {
            value: [Object, String, Number, Boolean, Array],
            editMode: Boolean,
            isNew: Boolean,
            offline: Boolean,
        },
        data: () => ({
            snackbar: {
                status: false,
                timeout: 5000,
                text: '',
            },
        }),
        async created() {
        },
        methods: {
            decode(value) {
                return decodeURIComponent(value);
            },
            edit() {
                this.editMode = true;
            },
            async save() {
                try {
                    let temp = null;

                    if (!this.offline) {
                        if (this.isNew) {
                            temp = await axios.post(axios.fixUrl('/menus'), this.value);
                        } else {
                            temp = await axios.put(axios.fixUrl(this.value._links.self.href), this.value);
                        }
                    }

                    if (this.value != null) {
                        for (let k in temp.data) this.value[k] = temp.data[k];
                    } else {
                        this.value = temp.data;
                    }

                    this.editMode = false;
                    this.$emit('input', this.value);

                    if (this.isNew) {
                        this.$emit('add', this.value);
                    } else {
                        this.$emit('edit', this.value);
                    }

                    location.reload();

                } catch (e) {
                    this.snackbar.status = true;
                    if (e.response && e.response.data.message) {
                        this.snackbar.text = e.response.data.message;
                    } else {
                        this.snackbar.text = e;
                    }
                }

            },
            async remove() {
                try {
                    if (!this.offline) {
                        await axios.delete(axios.fixUrl(this.value._links.self.href));
                    }

                    this.editMode = false;
                    this.isDeleted = true;

                    this.$emit('input', this.value);
                    this.$emit('delete', this.value);

                } catch (e) {
                    this.snackbar.status = true;
                    if (e.response && e.response.data.message) {
                        this.snackbar.text = e.response.data.message;
                    } else {
                        this.snackbar.text = e;
                    }
                }
            },
            // InventoryId 리스트 관리
            addInventoryId() {
                this.value.inventoryId.push('');
            },
            removeInventoryId(index) {
                this.value.inventoryId.splice(index, 1);
            },
            // IngredientUnit 리스트 관리
            addIngredientUnit() {
                this.value.ingredientUnit.push('');
            },
            removeIngredientUnit(index) {
                this.value.ingredientUnit.splice(index, 1);
            },
        },
    };
</script>


