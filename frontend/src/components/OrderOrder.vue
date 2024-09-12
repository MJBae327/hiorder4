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
          <span class="font-weight-bold text-h6">Order # {{ decode(value._links.self.href.split('/').pop()) }}</span>
        </div>
        <div v-else>
          <span class="font-weight-bold text-h6">New Order</span>
        </div>
      </v-card-title>
  
      <!-- Card Content -->
      <v-card-text>
        <v-container fluid>
          <!-- Number of People Field -->
          <v-row>
            <v-col>
              <Number
                label="Number of People"
                v-model="value.numberOfPeople"
                :editMode="editMode"
                :inputUI="''"
              />
            </v-col>
          </v-row>
  
          <!-- Table ID Field -->
          <v-row>
            <v-col>
              <Number
                label="Table ID"
                v-model="value.tableId"
                :editMode="editMode"
                :inputUI="''"
              />
            </v-col>
          </v-row>
  
          <!-- Order Time Field -->
          <v-row>
            <v-col>
              <Date
                label="Order Time"
                v-model="value.orderTime"
                :editMode="editMode"
                :inputUI="''"
              />
            </v-col>
          </v-row>
  
          <!-- Menu ID Field -->
          <v-row>
            <v-col>
              <Number
                label="Menu ID"
                v-model="value.menuId"
                :editMode="editMode"
                :inputUI="''"
              />
            </v-col>
          </v-row>
  
          <!-- Quantity Field -->
          <v-row>
            <v-col>
              <Number
                label="Quantity"
                v-model="value.quantity"
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
  
          <!-- Status Field -->
          <v-row>
            <v-col>
              <String
                label="Status"
                v-model="value.status"
                :editMode="editMode"
                :inputUI="''"
              />
            </v-col>
          </v-row>
  
          <!-- Is Orderable Field -->
          <v-row>
            <v-col>
              <String
                label="Is Orderable"
                v-model="value.isOrderable"
                :editMode="editMode"
                :inputUI="''"
              />
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
          수정
        </v-btn>
        <div v-else>
          <v-btn
            color="primary"
            class="mx-2"
            @click="save"
          >
            {{ isNew ? 'Create Order' : 'Update Order' }}
          </v-btn>
          <v-btn
            color="error"
            class="mx-2"
            @click="remove"
          >
            삭제
          </v-btn>
          <v-btn
            color="secondary"
            class="mx-2"
            @click="editMode = false"
          >
            취소
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
      name: 'OrderOrder',
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
                temp = await axios.post(axios.fixUrl('/orders'), this.value);
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
  
