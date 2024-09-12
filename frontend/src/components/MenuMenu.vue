<template>
    <v-card
      style="width: 450px; height: 100%; border-radius: 12px;"
      class="pa-4"
      outlined
    >
      <!-- Progress bar for loading -->
      <template v-slot:progress>
        <v-progress-linear
          color="primary"
          height="8"
          indeterminate
          rounded
        ></v-progress-linear>
      </template>
  
      <!-- Card Title -->
      <v-card-title class="d-flex justify-space-between">
        <div v-if="value._links">
          <span class="font-weight-bold text-h6">Menu # {{ decode(value._links.self.href.split('/').pop()) }}</span>
        </div>
        <div v-else>
          <span class="font-weight-bold text-h6">New Menu</span>
        </div>
      </v-card-title>
  
      <!-- Card Content -->
      <v-card-text>
        <v-container fluid>
          <!-- Name Field -->
          <v-row>
            <v-col>
              <String
                label="Name"
                v-model="value.name"
                :editMode="editMode"
                :inputUI="''"
              />
            </v-col>
          </v-row>
  
          <!-- Price Field -->
          <v-row>
            <v-col>
              <Number
                label="Price"
                v-model="value.price"
                :editMode="editMode"
                :inputUI="''"
              />
            </v-col>
          </v-row>
  
          <!-- InventoryId List -->
          <v-row>
            <v-col>
              <label class="subtitle-2 font-weight-bold">Inventory IDs</label>
              <v-row v-for="(id, index) in value.inventoryId" :key="index" class="d-flex align-center mb-2">
                <v-col cols="9">
                  <v-text-field
                    v-model="value.inventoryId[index]"
                    label="Inventory Id"
                    dense
                    v-if="editMode"
                    outlined
                    hide-details
                  />
                </v-col>
                <v-col cols="3">
                  <v-btn
                    icon
                    @click="removeInventoryId(index)"
                    v-if="editMode"
                  >
                    <v-icon color="red darken-2">mdi-delete</v-icon>
                  </v-btn>
                </v-col>
              </v-row>
              <v-btn color="primary" outlined small @click="addInventoryId" v-if="editMode">
                + Add Inventory ID
              </v-btn>
            </v-col>
          </v-row>
  
          <!-- IngredientUnit List -->
          <v-row>
            <v-col>
              <label class="subtitle-2 font-weight-bold">Ingredient Units</label>
              <v-row v-for="(unit, index) in value.ingredientUnit" :key="index" class="d-flex align-center mb-2">
                <v-col cols="9">
                  <v-text-field
                    v-model="value.ingredientUnit[index]"
                    label="Ingredient Unit"
                    dense
                    v-if="editMode"
                    outlined
                    hide-details
                  />
                </v-col>
                <v-col cols="3">
                  <v-btn
                    icon
                    @click="removeIngredientUnit(index)"
                    v-if="editMode"
                  >
                    <v-icon color="red darken-2">mdi-delete</v-icon>
                  </v-btn>
                </v-col>
              </v-row>
              <v-btn color="primary" outlined small @click="addIngredientUnit" v-if="editMode">
                + Add Ingredient Unit
              </v-btn>
            </v-col>
          </v-row>
        </v-container>
      </v-card-text>
  
      <!-- Action Buttons -->
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn
          color="primary"
          class="mx-2"
          @click="edit"
          v-if="!editMode"
        >
          Edit
        </v-btn>
        <div v-else>
          <v-btn
            color="primary"
            @click="save"
            class="mx-2"
          >
            {{ isNew ? 'Create Menu' : 'Update Menu' }}
          </v-btn>
          <v-btn
            color="error"
            @click="remove"
            class="mx-2"
          >
            Delete Menu
          </v-btn>
          <v-btn
            color="secondary"
            @click="editMode = false"
            class="mx-2"
          >
            Cancel
          </v-btn>
        </div>
      </v-card-actions>
  
      <!-- Snackbar for error messages -->
      <v-snackbar
        v-model="snackbar.status"
        :timeout="snackbar.timeout"
        color="error"
        top
        right
      >
        {{ snackbar.text }}
        <v-btn
          dark
          text
          @click="snackbar.status = false"
        >
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
        addInventoryId() {
          this.value.inventoryId.push('');
        },
        removeInventoryId(index) {
          this.value.inventoryId.splice(index, 1);
        },
        addIngredientUnit() {
          this.value.ingredientUnit.push('');
        },
        removeIngredientUnit(index) {
          this.value.ingredientUnit.splice(index, 1);
        },
      },
    };
  </script>
  
  <style scoped>
    .subtitle-2 {
      margin-bottom: 10px;
      font-weight: 500;
    }
    .v-card {
      border-radius: 12px;
      box-shadow: 0px 3px 10px rgba(0, 0, 0, 0.1);
    }
  </style>
  