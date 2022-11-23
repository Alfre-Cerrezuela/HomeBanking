const { createApp } = Vue;

createApp({
    data() {
        return {
            // arrayClientes: [],
            /*  cuentas: [] */
            cliente: {},
            tarjetasDebito: [],
            tarjetasCredito: [],
            id_borrar_cuenta: "",

        }
    },
    mounted() { /* es cuando se creo la parte visual cuando este renderizado */

    },

    created() {
    },

    methods: {
        prueba() {
            Swal.fire({
                title: 'Submit your Github username',
                input: 'password',
                inputAttributes: {
                    autocapitalize: 'off'
                },
                showCancelButton: true,
                confirmButtonText: 'Look up',
                showLoaderOnConfirm: true,
                preConfirm: (login) => {
                    return fetch(`//api.github.com/users/${login}`)
                        .then(response => {
                            if (!response.ok) {
                                throw new Error(response.statusText)
                            }
                            return response.json()
                        })
                        .catch(error => {
                            Swal.showValidationMessage(
                                `Request failed: ${error}`
                            )
                        })
                },
                allowOutsideClick: () => !Swal.isLoading()
            }).then((result) => {
                if (result.isConfirmed) {
                    Swal.fire({
                        title: `${result.value.login}'s avatar`,
                        imageUrl: result.value.avatar_url
                    })
                }
            })
            // const { value: password } = Swal.fire({
            //     title: 'Enter your password',
            //     input: 'password',
            //     inputLabel: 'Password',
            //     inputPlaceholder: 'Enter your password',
            //     inputAttributes: {
            //         maxlength: 10,
            //         autocapitalize: 'off',
            //         autocorrect: 'off'
            //     }
            // })

            // if (password) {
            //     Swal.fire(`Entered password: ${password}`)
            // }
        }




    },

    computed: {

    },

}).mount("#app");