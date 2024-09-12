<template>
    <v-card
      style="max-width: 500px; margin: 20px auto; border-radius: 16px;"
      class="elevation-3"
      outlined
    >
      <!-- Loading Indicator -->
      <template v-slot:progress>
        <v-progress-linear
          color="primary"
          height="6"
          indeterminate
          rounded
        ></v-progress-linear>
      </template>
  
      <!-- Card Header -->
      <v-card-title class="d-flex justify-space-between align-center">
        <div v-if="value._links">
          <span class="font-weight-bold text-h5">Edit Menu # {{ decode(value._links.self.href.split('/').pop()) }}</span>
        </div>
        <div v-else>
          <span class="font-weight-bold text-h5">Create New Menu</span>
        </div>
      </v-card-title>
  
      <!-- Card Content -->
      <v-card-text>
        <v-container fluid>
          <!-- Menu Name Field -->
          <v-row>
            <v-col>
              <v-text-field
                v-model="value.name"
                label="Menu Name"
                :disabled="!editMode"
                prepend-icon="mdi-food"
                outlined
                dense
              ></v-text-field>
            </v-col>
          </v-row>
  
          <!-- Price Field -->
          <v-row>
            <v-col>
              <v-text-field
                v-model="value.price"
                label="Price"
                :disabled="!editMode"
                prepend-icon="mdi-currency-usd"
                outlined
                dense
                type="number"
              ></v-text-field>
            </v-col>
          </v-row>
  
          <!-- Inventory IDs -->
          <v-row>
            <v-col>
              <v-divider></v-divider>
              <v-list-item>
                <v-list-item-content>
                  <v-list-item-title class="font-weight-bold">Inventory IDs</v-list-item-title>
                </v-list-item-content>
                <v-btn
                  color="primary"
                  icon
                  v-if="editMode"
                  @click="addInventoryId"
                >
                  <v-icon>mdi-plus</v-icon>
                </v-btn>
              </v-list-item>
              <v-list dense>
                <v-list-item v-for="(id, index) in value.inventoryId" :key="index">
                  <v-text-field
                    v-model="value.inventoryId[index]"
                    label="Inventory ID"
                    outlined
                    dense
                    v-if="editMode"
                    @click:append="removeInventoryId(index)"
                  ></v-text-field>
                </v-list-item>
              </v-list>
            </v-col>
          </v-row>
  
          <!-- Ingredient Units -->
          <v-row>
            <v-col>
              <v-divider></v-divider>
              <v-list-item>
                <v-list-item-content>
                  <v-list-item-title class="font-weight-bold">Ingredient Units</v-list-item-title>
                </v-list-item-content>
                <v-btn
                  color="primary"
                  icon
                  v-if="editMode"
                  @click="addIngredientUnit"
                >
                  <v-icon>mdi-plus</v-icon>
                </v-btn>
              </v-list-item>
              <v-list dense>
                <v-list-item v-for="(unit, index) in value.ingredientUnit" :key="index">
                  <v-text-field
                    v-model="value.ingredientUnit[index]"
                    label="Ingredient Unit"
                    outlined
                    dense
                    v-if="editMode"
                    @click:append="removeIngredientUnit(index)"
                  ></v-text-field>
                </v-list-item>
              </v-list>
            </v-col>
          </v-row>
        </v-container>
      </v-card-text>
  
      <!-- Action Buttons -->
      <v-card-actions class="pa-4 d-flex justify-space-between">
        <v-btn
          color="secondary"
          @click="editMode = !editMode"
          v-if="!editMode"
          class="mx-2"
        >
          <v-icon left>mdi-pencil</v-icon> Edit
        </v-btn>
        <div v-if="editMode">
          <v-btn color="primary" @click="save" class="mx-2">
            <v-icon left>mdi-content-save</v-icon> Save
          </v-btn>
          <v-btn color="error" @click="remove" class="mx-2">
            <v-icon left>mdi-delete</v-icon> Delete
          </v-btn>
          <v-btn color="secondary" @click="editMode = false" class="mx-2">
            <v-icon left>mdi-cancel</v-icon> Cancel
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
            this.snackbar.text =
              e.response && e.response.data.message
                ? e.response.data.message
                : e;
          }
        },
        async remove() {
          try {
            if (!this.offline) {
              await axios.delete(axios.fixUrl(this.value._links.self.href));
            }
  
            this.editMode = false;
            this.$emit('input', this.value);
            this.$emit('delete', this.value);
          } catch (e) {
            this.snackbar.status = true;
            this.snackbar.text =
              e.response && e.response.data.message
                ? e.response.data.message
                : e;
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
    .v-card {
      box-shadow: 0px 6px 20px rgba(0, 0, 0, 0.1);
    }
    .v-card-title {
      background-color: #f5f5f5;
      padding: 16px;
      border-bottom: 1px solid #eee;
    }
    .v-btn {
      border-radius: 8px;
    }
  </style>
  