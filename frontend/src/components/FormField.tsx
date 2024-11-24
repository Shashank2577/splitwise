import React from 'react';
import { TextField, TextFieldProps } from '@mui/material';
import { Controller, Control } from 'react-hook-form';

interface FormFieldProps extends Omit<TextFieldProps, 'name'> {
  name: string;
  control: Control<any>;
}

const FormField: React.FC<FormFieldProps> = ({ name, control, ...props }) => {
  return (
    <Controller
      name={name}
      control={control}
      render={({ field, fieldState: { error } }) => (
        <TextField
          {...field}
          {...props}
          error={!!error}
          helperText={error?.message}
          fullWidth
          margin="normal"
        />
      )}
    />
  );
};

export default FormField;
